package com.gxzx.testrabbitmq.config.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxzx.testrabbitmq.web.entity.EntrustMsgErrLog;
import com.gxzx.testrabbitmq.web.mapper.EntrustMsgErrLogMapper;

/**
 * Return Listener用于处理一些不可路由的消息。当前的exchange不存在或者指定的路由key路由不到，这个时候我们需要监听这种不可达的消息，就要使用return listener。
 * 若消息找不到对应的Exchange会先触发 Returncallback，然后还会继续触发 ConfirmCallback，并且ack为true
 */
public class CallBackListenerOnReturn implements ReturnCallback{

	private static final Logger logger = LoggerFactory.getLogger(CallBackListenerOnReturn.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EntrustMsgErrLogMapper entrustMsgErrLogMapper;
	
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {	
		//向系统管理员发送告警短信和告警邮件通知
		logger.error("消息丢失了，此类都是RabbitMQ的配置异常导致的，问题非常严重:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
		
     	//将异常事件登记到预估值账户异常表（订单ID，金额，冷热账户ID，是否扣费，是否完成核对，创建时间，核对时间）。此情况需要进一步检查订单数据库，定时核对订单是否入库，确认没有入库时，执行回滚预估值账户。  
    	EntrustMsgErrLog entrustMsgErrLog = null;
		try {
			entrustMsgErrLog = objectMapper.readValue(message.getBody().toString(), EntrustMsgErrLog.class);
		} catch (JsonParseException e) {
			logger.error("",e);
		} catch (JsonMappingException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}
    	
    	logger.info("发生异常事件入库： "
    	+"etrustId="+entrustMsgErrLog.getEntrustId()
    	+",amount="+entrustMsgErrLog.getAmount()
    	+",availableBalanceAccountId="+entrustMsgErrLog.getAvailableBalanceAccountId()
    	+",freezingBalanceAccountId="+entrustMsgErrLog.getFreezingBalanceAccountId());   
    	
    	entrustMsgErrLogMapper.insert(entrustMsgErrLog);  		
	}

}
