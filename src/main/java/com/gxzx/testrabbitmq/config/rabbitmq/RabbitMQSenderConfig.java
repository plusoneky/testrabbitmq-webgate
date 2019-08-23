package com.gxzx.testrabbitmq.config.rabbitmq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 
 *                                                      |<--------------------------------4、async to save db-------------------------------------------------------------->|       
 *                                            |---------|                                                                                                                         
 *                                            |         |                ----------------              -------------                 ---------------                              
 *                                            |         | ---subscript-> |   matcher    |              |           |                 |   slave     |                                                                          
 *                                            |queue1   |  none confirm  | (consumer)   |---publish -->|ring buffer| ---subscript--> |  consumer   |-------on event fanout start------------                                                                        
 *                                            |         |                |              |              |           |                 |             | (cleaing saved history orders then stop)                                                                         
 *                                            |         |                ----------------              -------------                 ---------------                                                                          
 *                                            |         |                                                                                   ▲
 *                                            |         |                                                                                   ▲
 *                                            |         |                                                                            timing fanout latest saved orderId
 *                                            |         |                                                                                   ▲
 * --------------                -----------------------|                ----------------              -------------                 ---------------           ---------                      
 * |  webgate   | -1、publish--> |  fanout    |		    | ---subscript-> |   matcher    |              |           |                 |   master    | on event  |       |                      
 * | (publisher)|                |  broker    |queue2   | none confirm   | (consumer)   |---publish -->|ring buffer| ---subscript--> |  consumer   |---save--> | mysql |                      
 * |            | <-3、confirm---|            |		    |                |              |              |           |                 |             |	       |       |                      
 * --------------          |-▶▶▶▶----------------------|                ----------------              -------------                 ---------------	           ---------                      
 *                         |               |  |		    |                                                                                   ▼                                                   
 *                         |               |  |		    |                                                                             timing fanout latest saved orderId                                                  
 *                         |--2、durable --|  |		    |                                                                                   ▼
 *                                            |         |                                                                                   ▼                                                                                   
 *                                            |		    |                ----------------              -------------                 ---------------                                                                          
 *                                            |queue3	| ---subscript-> |   matcher    |              |           |                 |   slave     |                                                                         
 *                                            | 	    |  none confirm  | (consumer)   |---publish -->|ring buffer| ---subscript--> |  consumer   |-------on event fanout start------------                                                                         
 *                                            | 	    |                |              |              |           |                 |             | (cleaing saved history orders then stop)                                                                           
 *                                            | 	    |                ----------------              -------------                 ---------------                                                                          
 *                                            |---------|
 * 
 *
 * 
 * 异常情况分析：
 * 1、publisher：Publisher Confirms。确保发送消息的可靠性，失败了需要回滚预估值账户。
 * publisher未收到confirm时，MQ服务器持久化消息已成功，消息生产者挂了。无需处理，因为 用户预估值账户准确，返回到前端用户的是受理成功或者请求超时。
 * publisher未收到confirm时，MQ服务器持久化消息已成功，MQ服务器挂了。无需处理，因为 用户预估值账户准确，返回到前端是委托受理成功。（数据库会产生一条异常对账记录。）
 * publisher未收到confirm时，MQ服务器持久化消息未成功，消息生产者挂了或者消息生产者将异常入数据库不成功。需要补偿机制处理：依靠基ELK分析日志，需要定时回滚预估值账户。返回到前端的是请求超时异常。
 * publisher未收到confirm时，MQ服务器持久化消息未成功，MQ服务器挂了。需要补偿机制处理：依靠数据库日志，定时回滚预估值账户。（数据库会产生一条异常对账记录。）
 *                                                                                                                                          
 * 2、consumer：Auto Confirm。因为广播类型的交换机上会创建多个MQ队列，处理运行时异常的逻辑后，结论是采用自动确认ACK机制。前提是MQ不能推送重复的消息。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，已成功入库，消息消费者挂了。 无需处理，不影响交换机上其他队列，系统会选举新的主节点继续运行。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，已成功入库，MQ服务器挂了。 无需处理。重启MQ服务器就行了。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，由于数据库异常，未成功入库。需要补偿机制处理：将消息丢到一个单独的MQ队列，延迟持久化。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，由于业务异常，比如数据库余额不足，不允许入库。需要补偿机制处理：作废此委托，以及对手单委托，并重新同步这两个用户的预估值账户。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，由于消息消费者挂了，未成功入库。 无需处理。此消费者内存中数据全部丢失了，但系统会选举新的主节点继续运行，其上的入库进程会找到最近的入库消息后，顺序进行。   
 *
 * 
 * 
 * MQ业务逻辑说明：
 * RabbitMQ的交换机创建使用手动命令脚本的方式进行创建，交换机为广播类型。
 * 业务数据库币对表，配置每个币对所属的的交换机，将币对与交换机进行绑定，所以市场（币对）表有一个所属交换机字段。
 * 币对如果需要修改交所属的交换机前需要停止该币对的撮合，发送订单消息时，将发送到对应的交换机上的所有队列。
 * 撮合服务，配置一个负责监听的交换机，以及消息队列名（队列名隐式的用机架名和服务器名）。
 * 也就是说同一个币对，对应的撮合的一主多从，每个节点配置的交换机名一样，但消息队列不同。
 * 
 * @author Administrator
 *
 */
@Configuration
public class RabbitMQSenderConfig {
	
	@Autowired
	public ConnectionFactory connectionFactory;
	
	@Bean
	public CallBackListenerOnReturn returnCallBackListener(){
		return new CallBackListenerOnReturn();
	}
	
	@Bean
	public CallBackListenerOnConfirm confirmCallBackListener(){
		return new CallBackListenerOnConfirm();
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