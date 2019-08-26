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
 *  港盛委托单服务实现类
 * </p>
 *
 * @author administrator
 * @since 2019-08-14
 */
@Service
public class EntrustOrderServiceGangshengImpl implements IEntrustOrderService {
    
	private static final Logger logger = LoggerFactory.getLogger(EntrustOrderServiceGangshengImpl.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SnowflakeIdWorker snowflakeIdWorker;
	
	
	@Autowired
	MarketMapper marketMapper;
	
	/* 
	 * 
	 */
	@Override
    public boolean createOrder(EntrustOrder entity, Market market) {
		boolean returnValue = false;
		return returnValue;
    }
}
