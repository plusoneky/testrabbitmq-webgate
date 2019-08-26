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
 *  本地委托单服务实现类
 * </p>
 *
 * @author administrator
 * @since 2019-08-14
 */
@Service
public class EntrustOrderServiceLocalImpl implements IEntrustOrderService {
    
	private static final Logger logger = LoggerFactory.getLogger(EntrustOrderServiceLocalImpl.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SnowflakeIdWorker snowflakeIdWorker;
	
    @Autowired
    private RabbitTemplate rabbitTemplate;

	/* 
	 * 
	 */
	@Override
    public boolean createOrder(EntrustOrder entity, Market market) {
		boolean returnValue = false;
		if(null == entity.getUserId()){
			throw new CustomException(IEntrustOrderServiceErrCode.ParamIsWrong);
		}
		
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
		
		//填充订单总额、用户冷热资金账户ID
		entity.setAvailableBalanceAccountId(availableBalanceAccountId);
		entity.setFreezingBalanceAccountId(freezingBalanceAccountId);
		entity.setEntrustAmount(amount);
		
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
