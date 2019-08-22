package com.gxzx.testrabbitmq.dto;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseDTO implements Serializable,Cloneable{
	
	private static final long serialVersionUID = 6823574800580153656L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Override
	public String toString(){
		String returnValue = "";
		try {
			returnValue = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("",e);
		}
		return returnValue;
	}

	
	/**
	 * 克隆实例
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public Object cloneThis() throws CloneNotSupportedException {
		return super.clone();
	}


}
