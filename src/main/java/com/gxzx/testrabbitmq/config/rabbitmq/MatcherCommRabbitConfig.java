package com.gxzx.testrabbitmq.config.rabbitmq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * RabbitMQ的交换机创建使用命令脚本的方式进行创建，交换机为广播类型。
 * 业务数据库币对表，配置每个币对所属的的交换机，将币对与交换机进行绑定，所以市场（币对）表有一个所属交换机字段。
 * 币对如果需要修改交所属的交换机前需要停止该币对的撮合，发送订单消息时，将发送到对应的交换机上的所有队列。
 * 撮合服务，配置一个负责监听的交换机，以及消息队列名（队列名隐式的用机架名和服务器名）。
 * 也就是说同一个币对，对应的撮合的一主多从，每个节点配置的交换机名一样，但消息队列不同。
 * @author Administrator
 *
 */
@Configuration
public class MatcherCommRabbitConfig {
	
	@Autowired
	public ConnectionFactory connectionFactory;
	
	@Bean
	public ReturnCallBackListener returnCallBackListener(){
		return new ReturnCallBackListener();
	}
	
	@Bean
	public ConfirmCallBackListener confirmCallBackListener(){
		return new ConfirmCallBackListener();
	}	

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMandatory(true);
		template.setReceiveTimeout(1000);
		template.setReplyTimeout(1000);
		template.setConfirmCallback(confirmCallBackListener());
		template.setReturnCallback(returnCallBackListener());
		return template;
	}

}