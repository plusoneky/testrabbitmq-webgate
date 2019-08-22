package com.gxzx.testrabbitmq.web.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxzx.testrabbitmq.exception.CustomException;
import com.gxzx.testrabbitmq.util.idworker.SnowflakeIdWorker;
import com.gxzx.testrabbitmq.web.entity.EntrustMsgErrLog;
import com.gxzx.testrabbitmq.web.entity.EntrustOrder;
import com.gxzx.testrabbitmq.web.entity.Market;
import com.gxzx.testrabbitmq.web.mapper.MarketMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author administrator
 * @since 2019-08-14
 */
@Service
public class EntrustOrderServiceImpl implements IEntrustOrderService {
    
	private static final Logger logger = LoggerFactory.getLogger(EntrustOrderServiceImpl.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SnowflakeIdWorker snowflakeIdWorker;
	
    @Autowired
    private RabbitTemplate rabbitTemplate;
	
	@Autowired
	MarketMapper marketMapper;
	
	/* 
	 * redis账户记账和发MQ订单消息这两个步骤当然是希望能放到一个事务中，即记账失败了，就不能发MQ消息，发MQ消息失败了要回滚记账。
	 * 这里我的思路是：
	 * 1、关闭进程要实现优雅停服，让所有工作线程跑完，减少人为导致数据异常的概率。
	 * 2、数据库保存一张MQ异常事件表，记录超时的MQ消息，或者回滚预估值账户失败的异常订单的订单号和订单金额等信息，
	 * 		待一定延迟后（确保MQ中没有改用户的委托单）与数据库的订单数据进行核对，如果订单在数据库存在
	 * 3、打印各个环节的日志，通过ELK检查和分析日志，找出业务没有闭环的订单，这些订单的用户预估值账户可能出现脏数据，需要重新同步做进一步检查。 
	 * 4、预估值账户，需要实现一个定时任务。定时例如每隔几分钟，遍历检查30分钟前的异常事件订单，数据库订单表是否存在此订单号。
	 */
    public boolean sendEntrustOrder(EntrustOrder entity) {
		boolean returnValue = false;
		if(null == entity.getUserId()){
			throw new CustomException(IEntrustOrderServiceErrCode.ParamIsWrong);
		}
		
		//数据校验及获取相关这里应当优化为从Redis缓存读取
		Market market = marketMapper.selectById(entity.getMarketId());		
		if(null==market || StringUtils.isBlank(market.getMatcherExchangeName())){
			logger.error("market table data have no data");
			return returnValue;
		}
		
		//雪花算法填充ID
		long entrustId = snowflakeIdWorker.nextId();
		entity.setId(entrustId);
		//委托时间以网关时间为准
		entity.setEntrustTime(new Date());
		
		// 1、预估值记账。需要基于redis的预估值账户记账封装一个jar包，操作redis预估值账户：扣减热账户余额、增加冷账户余额 
		//打印本地操作日志
		logger.info("1.will update user estimation account in redis, id=%s",entity.getId());		
		long availableBalanceAccountId = 10000;
		long freezingBalanceAccountId = 20000;
		BigDecimal amount = entity.getEntrustPrice().multiply(entity.getEntrustVolume());
		amount = amount.setScale(10, RoundingMode.HALF_UP);
		
		//打印本地操作日志
		logger.info("2.have successed to update user estimation account, now will send msg to rabbitmq, id=%s",entity.getId());

		//2、发送委托单消息到MQ。如果发送超时或失败，异步处理中，通过记录EntrustAccountDto消息，定时程序检查回滚该用户的预估值账户，而不是重发该消息。
		EntrustMsgErrLog entrustMsgErrLog = new EntrustMsgErrLog();
		entrustMsgErrLog.setEntrustId(entrustId);
		entrustMsgErrLog.setUserId(entity.getUserId());
		entrustMsgErrLog.setAvailableBalanceAccountId(availableBalanceAccountId);
		entrustMsgErrLog.setFreezingBalanceAccountId(freezingBalanceAccountId);
		entrustMsgErrLog.setAmount(amount);
		
		CorrelationData correlationId = null;
		String msg = null;
		try {
			correlationId = new CorrelationData(objectMapper.writeValueAsString(entrustMsgErrLog));
			msg = objectMapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			logger.error("",e);
		}

		try {
			//交换机是从币对配置表中读取的
			rabbitTemplate.convertAndSend(market.getMatcherExchangeName(), "" , msg, correlationId);
			logger.info("3.dont't know rabbitmq ack,but have finished send msg to rabbitmq, id=%s",entity.getId());
			returnValue = true;
		} catch (AmqpException e) {
			//发送超时，会抛出异常 org.springframework.amqp.AmqpIOException: java.net.SocketTimeoutException  
			//连接超时的时间是通过在配置文件spring.rabbitmq.connection-timeout参数指定
			//将异常事件登记到预估值账户异常表（订单ID，金额，冷热账户ID，是否扣费，是否完成核对，创建时间，核对时间）。此情况需要进一步检查订单数据库，定时核对订单是否入库，确认没有入库时，执行回滚预估值账户。        
			logger.error("send msg to rabbitmq：entrustMsgErrLog="+entrustMsgErrLog, e);
		}
	
		return returnValue;
    }
}
