package com.gxzx.testrabbitmq.config.properties;

/**
 * DEMO业务配置参数
 */
public class SysProperties {
	/**
	 * APPID
	 */
	public String appId;
	
	/**
	 * 数据中心ID(0~31)
	 */
	public long dataCenterId;	
	
	/**
	 * 工作机器ID(0~31)
	 */
	public long workMachineId;			

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public long getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(long dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

	public long getWorkMachineId() {
		return workMachineId;
	}

	public void setWorkMachineId(long workMachineId) {
		this.workMachineId = workMachineId;
	}



	
	
}
