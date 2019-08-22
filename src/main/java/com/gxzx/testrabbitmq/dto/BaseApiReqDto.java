package com.gxzx.testrabbitmq.dto;

import io.swagger.annotations.ApiModelProperty;

public class BaseApiReqDto extends BaseDTO{

	private static final long serialVersionUID = -8552181719011560598L;

	/**
	 * 设备类型: 1、ios，2、android，3、windows 4、H5
	 */
	@ApiModelProperty(value = "设备类型: 1、ios，2、android，3、wince 4、H5", required = true, example = "1") 
	private int devType;
	
	/**
	 * 设备ID
	 */
	@ApiModelProperty(value = "设备ID", required = true, example = "sfs2342432424sdfsfsdf") 
	private String devId;
	
	/**
	 * 设备版本
	 */
	@ApiModelProperty(value = "设备版本", required = true, example = "1.0.0") 
	private String appVer;	
	
	/**
	 * App产品id编号，传媒固定001
	 */
	@ApiModelProperty(value = "App产品id", required = true, example = "1") 
	private long appId;	
	
//	/**
//	 * 经度
//	 */
//	@ApiModelProperty(value = "经度", required = false, example = "4324.34")
//	private String lon;
//
//	/**
//	 * 纬度
//	 */
//	@ApiModelProperty(value = "纬度", required = false, example = "4334.23")
//	private String lat;
	
	public int getDevType() {
		return devType;
	}
	public void setDevType(int devType) {
		this.devType = devType;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
//	public String getLon() {
//		return lon;
//	}
//	public void setLon(String lon) {
//		this.lon = lon;
//	}
//	public String getLat() {
//		return lat;
//	}
//	public void setLat(String lat) {
//		this.lat = lat;
//	}
	public String getAppVer() {
		return appVer;
	}
	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
	}
	
	
}
