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
 * ->箭头表示同步消息。▶◀实体箭头表示异步消息
 * 
 *                                                        |<---------------------------------------------------4、async to save db------------------------------------------------------------>|       
 *                                              |---------|                                                                                                                         
 *                                              |         |                  ----------------              -------------                   ---------------                              
 *                                              |         | ---subscript-▶▶ -|   matcher    |              |           |                   |   slave     |                                                                          
 *                                              |queue1   |  not confirm     | (consumer)   |---publish -->|ring buffer| ---subscript-▶▶- |  consumer   |-------on fanout event start------------                                                                        
 *                                              |         |                  |              |              |           |                   |             | (cleaing saved history orders then stop)                                                                         
 *                                              |         |                  ----------------              -------------                   ---------------                                                                          
 *                                              |         |                                                                                   ▲
 *                                              |         |                                                                                   ▲
 *                                              |         |                                                                            timing fanout latest saved orderId
 *                                              |         |                                                                                   ▲
 * --------------                  -----------------------|                   ----------------             -------------                   ---------------           ---------                      
 * |  webgate   | -1、publish----->|  fanout    |		  | ---subscript-▶▶-|   matcher    |              |           |                   |   master    | on event  |       |                      
 * | (publisher)|                  |  broker    |queue2   |  not confirm     | (consumer)   |---publish -->|ring buffer| ---subscript-▶▶- |  consumer   |---save--> | mysql |                      
 * |            |-◀◀---3、confirm--|            |		  |                  |              |              |           |                   |             |	         |       |                      
 * --------------          |-----> |----------------------|                  ----------------              -------------                   ---------------	         ---------                      
 *       |                 |               |    |         |                                                                                   ▼                                                   
 *       |                 |               |    |         |                                                                             timing fanout latest saved orderId                                                  
 *       |                 |--2、durable --|    |         |                                                                                   ▼
 *       |                                      |         |                                                                                   ▼                                                                                   
 *       |                                      |         |                  ----------------              -------------                  ---------------                                                                          
 *       |                                      |queue3	  | ---subscript-▶▶-|   matcher    |              |           |                   |   slave     |                                                                         
 *       |                                      |         |  not confirm     | (consumer)   |---publish -->|ring buffer| ---subscript-▶▶- |  consumer   |-------on fanout event start------------                                                                         
 *       |                                      | 	      |                  |              |              |           |                   |             | (cleaing saved history orders then stop)                                                                           
 *       |                                      | 	      |                  ----------------              -------------                   ---------------                                                                          
 *       |                                      |---------|
 *       |
 *       |                       ------------     --------------        ------------       ------------------------------                         -------------------------------------------------------------------    
 *       ----------------------->|gang sheng|---->|create order|        |  redis    |      |                            |  entrustOrder finished  |原因是成交记录也是异步推送                                                                                                             |    
 *                               | create   |1、	  | save to    |        |(estimation|      |   gang sheng server        |--------------------▶▶- |所以需要判断订单剩余数量与推送数据是否一致，如果一致则更新订单为已完成 |    
 *                               | order    |	  |  mysql     |        |account)   |      |                            |----|                    |如果不一致，则将该订单全部成交记录先添加到临时表中，定期再更新                    |    
 *                               ------------	  --------------     --->------------    |-> ----------------------------    |                    -------------------------------------------------------------------    
 *                                    |------------------------------|                   |                           ▲       |        		                                  
 *                                    | 2、update user estimation account to redis       |                           |       |			    	                      
 *                                    |                                                  |                           |       |			    	                      
 *                                    |--------------------------------------------------|                           |       |               ----------------  
 *                                      3、netty client send message gang sheng server over Internet,                |       |    trade log  |update order   | 目前是检查到存在匹配的订单才保存到临时表
 *                                         sync return gangshengOrderId,                                             |       |----------▶▶- |create tradelog| 会有一个进程扫描这张表，构造一个特殊的toTrade消息，包括对手单
 *                                         if failed then rollback estimation account.                               |            subscript  |save to mysql  | 
 *                                                                                                                   |                       ----------------- 
 *                                                                                                                   |
 *                                                                                                                   |-----------------------|---------|
 *                                                                                                                                           | 单笔订单 |
 *                                                                                                                                           | 定时查询 |
 *                                                                                                                                           | 成交记录 |
 *                                                                                                                                           |---------|                                                                                                                                                  
 *                                                                                                                                            
 * 
 *                                                                                                                                                   
 *  1、委托挂单的系统延迟时间在10毫秒以内。撤单按用户分两种情况，机构或专业用户采用异步撤单机制，请求延迟在10毫秒以内直接返回受理结果，
 *     撤单结果需要机构用户自己主动查询。普通用户撤单，响应延迟在100毫秒内内，返回撤单是否成功。
 *  2、撮合引擎支持一主多从，建议配置至少一主二从以上，撮合系统主从切换的时间不超过3秒。即使发生主撮合引擎宕机的极端情况时，
 *     不会影响用户的挂单、撤单、提现、转账等委托请求。但会影响行情、盘口数据的推送，延迟不超过3秒。
 *  3、支持机构或专业用户的高频量化交易，下单首先经过基于Redis内存的账户记账，在撮合之前，账户资金变更与订单等不入库持久化，同时做到确保数据最终一致性。
 *  4、基于本地内存进行流水线撮合，单笔撮合的时间开销10毫秒，根据不同选型，基于MQ的撮合引擎的吞吐量可以达到每秒5000笔，这里主要实现这种方案。
 *     另一方案撮合性能预计可以达到每秒10万，与上交所技术区别是不需要专用服务器和软件，能够部署在云平台通用服务器。
 *  5、支持按币对配置不同的撮合引擎，网关启动初始化时加载币对配置信息，获取币对对应的撮合引擎交换机，将消息发送给指定的交换机。
 *  6、撮合引擎在配置文件中定义交换机标识。
 *  7、系统中的所有服务器，在配置文件中需要配置各自的区域ID和机器ID，同一个区域ID的机器ID不用重复，将基于此采用分布式算法生成订单ID，以及MQ队列名称。
 *     挂单的消息统一为广播类型，将发送给绑定该交换机的所有撮合引擎，每个撮合引擎有自己独立的消息队列，同一个交换机下的不同消息队列的内容完全一致。
 *  8、挂单请求发送到网关，网关调用基于Redis的账户记账扣款后，将委托单发送到MQ，但只发送一次消息，如果发送失败了并不再重发，
 *     只记录异常事件，等待定期做redis内存账户回滚操作。
 *  9、撮合引擎完成一笔撮合后，首先进行行情数据的更新，只更新本地内存，由单独的同步进程定期每100毫秒刷新到Redis，再由接口适配器发布具体格式的数据推送到前端。
 *  10、待行情数据更新本地完毕后，进行持久化入库。
 *  11、待持久化入库完成后，发送撮合成交记录消息到MQ，供后续相关业务操作。例如后续可以扩展，向前端推送最近24小时成交记录，
 *      实现交易挖矿功能，以及发一些提示信息，将这些扩展业务与撮合系统解耦。
                                                                                                                                                   
 *                                                                                                                                                   
 *                                                                                                                                                   
 * 一、记账服务
 * 为了确保用户资金不透支需要遵循一条原则：如果是增加余额，那必须先走数据库，成功之后再增加预估值账户余额。如果是扣减余额，应该先扣减预估值账户，  在保存数据库。    
 * 解决基于Redis内存的预估值账户的数据一致性问题：
 * 有两种方法，一是先在数据库创建订单，然后再扣减预估值账户。通过定期统计超时订单，作废掉超时订单，来补偿预估值账户金额。港盛第三方下单，由于港盛提供的jar包屏蔽了发送订单的异常，所以我们采用这种方案机制。  
 * 另一种方法是先扣减预估值账户，经过一些列业务处理最终入库。这要求我们要捕获一系列的业务中的异常，发生异常时，记录相关信息，最终进行补偿。由于出现异常时，可能出现记录异常这一操作也是失败的，所以更完善的做法是过程中打印各个环节的日志，通过日志做进一步分析。      
 * 
 * 二、Rabbit MQ异常情况分析：
 * 1、publisher： Publisher Confirms。确保发送消息的可靠性，失败了需要回滚预估值账户。
 * publisher未收到confirm时，MQ服务器持久化消息已成功，消息生产者挂了。无需处理，因为 用户预估值账户准确，返回到前端用户的是受理成功或者请求超时。
 * publisher未收到confirm时，MQ服务器持久化消息已成功，MQ服务器挂了。无需处理，因为 用户预估值账户准确，返回到前端是委托受理成功。（数据库会产生一条异常对账记录。）
 * publisher未收到confirm时，MQ服务器持久化消息未成功，消息生产者挂了或者消息生产者将异常入数据库不成功。需要补偿机制处理：依靠基ELK分析日志，需要定时回滚预估值账户。返回到前端的是请求超时异常。
 * publisher未收到confirm时，MQ服务器持久化消息未成功，MQ服务器挂了。需要补偿机制处理：依靠数据库日志，定时回滚预估值账户。（数据库会产生一条异常对账记录。）
 *                                                                                                                                          
 * 2、consumer： Auto Confirm。因为广播类型的交换机上会创建多个MQ队列，处理运行时异常的逻辑后，结论是采用自动确认ACK机制。前提是MQ不能推送重复的消息。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，已成功入库，消息消费者挂了。 无需处理，不影响交换机上其他队列，系统会选举新的主节点继续运行。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，已成功入库，MQ服务器挂了。 无需处理。重启MQ服务器就行了。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，由于数据库服务器异常，如数据库网卡故障、磁盘满了等，未成功入库。需要补偿机制处理：将消息丢到一个单独的MQ队列，异步持久化。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，由于消费者服务器网卡故障等，未成功入库。无需处理。其他从机会接管。
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，由于消息消费者挂了，未成功入库。 无需处理。此消费者内存中数据全部丢失了，但系统会选举新的主节点继续运行，其上的入库进程会找到最近的入库消息后，顺序进行。   
 * consumer收到推送消息，服务器已删除该条消息，已成功撮合，出现业务异常，比如数据库该用户余额不足导致不允许此委托入库。需要补偿机制处理：丢掉此委托，增加对手的单委托相应金额的异常撤单（需要通知到该用户），并重新同步这两个用户的预估值账户。
 *
 * 三、Rabbit MQ业务逻辑说明：
 * RabbitMQ的交换机创建使用手动命令脚本的方式进行创建，交换机为广播类型。
 * 业务数据库币对表，配置每个币对所属的的交换机，将币对与交换机进行绑定，所以市场（币对）表有一个所属交换机字段。
 * 币对如果需要修改交所属的交换机前需要停止该币对的撮合，发送订单消息时，将发送到对应的交换机上的所有队列。
 * 撮合服务，配置一个负责监听的交换机，以及消息队列名（队列名隐式的用机架名和服务器名）。
 * 也就是说同一个币对，对应的撮合的一主多从，每个节点配置的交换机名一样，但消息队列不同。
 * 
 * 四、采用disruptor 的ring buffer存储撮合结果。ring buffer consumer的工作原理：latestFinishedOrderId 是接受主服务器定时广播发送的最近一个消息ID
 * 1、if(当前主机的主从状态!=主状态){
 *        if (latestFinishedOrderId !=null)
 *           if(onEventOrderId <latestFinishedOrderId){
 *           	//这里什么都不需要做相当于丢弃掉该消息，处理下一个事件
 *           }
 *           else{
 *           	sleep 1000;
 *              go 1;
 *           }  
 *        else{
 *           sleep 1000;
 *           go 1;
 *        }     
 *    }else{
 *       撮合
 *    }
 *
 *           
 * 2、主机不需要关心latestFinishedOrderId，从机需要同步latestFinishedOrderId，这是一个需要多线程访问的变量，需要同步。           
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