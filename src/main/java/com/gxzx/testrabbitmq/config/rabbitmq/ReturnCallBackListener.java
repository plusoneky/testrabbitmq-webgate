package com.gxzx.testrabbitmq.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.stereotype.Component;

/**
 * Return Listener用于处理一些不可路由的消息。当前的exchange不存在或者指定的路由key路由不到，这个时候我们需要监听这种不可达的消息，就要使用return listener。
 * 若消息找不到对应的Exchange会先触发 Returncallback，然后还会继续触发 ConfirmCallback，并且ack为true
 */
public class ReturnCallBackListener implements ReturnCallback{

	private static final Logger logger = LoggerFactory.getLogger(ReturnCallBackListener.class);
	
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {	
		//向系统管理员发送告警短信和告警邮件通知
		logger.error("=====消息丢失了======此类都是RabbitMQ的配置异常导致的，问题非常严重");
		logger.error("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
	}

}
