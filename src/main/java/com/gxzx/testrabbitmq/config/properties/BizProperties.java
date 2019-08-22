package com.gxzx.testrabbitmq.config.properties;

/**
 * 通用配置参数
 */
public class BizProperties {
	/**
	 * 间隔时间（单位:毫秒）
	 */		
	private long minRequestIntervalTime;
	
	/**
	 * 请求间隔周期时间（单位:次数）
	 */		
	private long maxMaliciousTimes;	
	
	/**
	 * 顶级域名
	 */	
	private String firstClassDomainName;	
	
	/**
	 * 运维级秘钥
	 */	
	private String operationSecret;
	
	/**
	 * tokenName
	 */	
	private String tokenName;	

	public long getMinRequestIntervalTime() {
		return minRequestIntervalTime;
	}

	public void setMinRequestIntervalTime(long minRequestIntervalTime) {
		this.minRequestIntervalTime = minRequestIntervalTime;
	}

	public long getMaxMaliciousTimes() {
		return maxMaliciousTimes;
	}

	public void setMaxMaliciousTimes(long maxMaliciousTimes) {
		this.maxMaliciousTimes = maxMaliciousTimes;
	}

	public String getFirstClassDomainName() {
		return firstClassDomainName;
	}

	public void setFirstClassDomainName(String firstClassDomainName) {
		this.firstClassDomainName = firstClassDomainName;
	}

	public String getOperationSecret() {
		return operationSecret;
	}

	public void setOperationSecret(String operationSecret) {
		this.operationSecret = operationSecret;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
}
