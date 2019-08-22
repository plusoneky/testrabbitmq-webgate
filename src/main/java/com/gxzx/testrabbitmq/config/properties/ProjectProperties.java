package com.gxzx.testrabbitmq.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
@Component
@ConfigurationProperties(prefix="project-properties")
public class ProjectProperties {

	/**
	 * 系统参数对象
	 */
	private SysProperties sysProperties = new SysProperties();
	
	/**
	 * 公共参数对象
	 */		
	private BizProperties bizProperties = new BizProperties();

	public BizProperties getBizProperties() {
		return bizProperties;
	}

	public void setBizProperties(BizProperties bizProperties) {
		this.bizProperties = bizProperties;
	}
	
	public SysProperties getSysProperties() {
		return sysProperties;
	}

	public void setDemo1Biz(SysProperties sysProperties) {
		this.sysProperties = sysProperties;
	}
	
}
