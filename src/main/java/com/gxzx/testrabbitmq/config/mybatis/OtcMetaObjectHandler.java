package com.gxzx.testrabbitmq.config.mybatis;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;


public class OtcMetaObjectHandler implements MetaObjectHandler {

    protected final static Logger logger = LoggerFactory.getLogger(OtcMetaObjectHandler.class);
    
	  @Override
	  public void insertFill(MetaObject metaObject) {
        //是否有gmtCreate通用字段名
        Object testType = getFieldValByName("gmtCreate", metaObject);
        if (testType == null) {
        	logger.debug("插入方法实体填充：自动添加对象创建时间，gmtCreate=" + testType);
            setFieldValByName("created_time", LocalDateTime.now(), metaObject);
        }	    
	  }

	  @Override
	  public void updateFill(MetaObject metaObject) {
        //是否有gmtCreate通用字段名
        Object testType = getFieldValByName("更新方法实体填充：自动添加对象更新时间，gmtModified", metaObject);
        if (testType == null) {
        	logger.debug("更新方法实体填充：自动添加对象更新时间，gmtModified"+metaObject);
        	setFieldValByName("modified_time", LocalDateTime.now(), metaObject);
        }
	    
	  }

}
