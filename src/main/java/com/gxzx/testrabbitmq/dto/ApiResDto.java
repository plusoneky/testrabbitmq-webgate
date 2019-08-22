package com.gxzx.testrabbitmq.dto;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gxzx.testrabbitmq.exception.CustomException;
import com.gxzx.testrabbitmq.exception.CustomException.CommErrCode;
import com.gxzx.testrabbitmq.dto.MsgResources;

import io.swagger.annotations.ApiModelProperty;


/**
 * @author 04
 * API接口返回参数对象
 * Include.NON_NULL用在实体类的方法类的头上  作用是实体类的参数查询到的为null的不显示
 */
@JsonInclude(Include.NON_NULL)
public class ApiResDto extends BaseDTO{

	private static final long serialVersionUID = -6726901716811552894L;		

	/**
	 * 编码，默认success为成功，非success失败。
	 */
	@ApiModelProperty(value = "编码", allowableValues="{success,...,CommErrCode.SysErr}", required = true, example="success") 	
	private String code = "success";
	
	/**
	 * 提示信息，默认为成功
	 */
	@ApiModelProperty(value = "提示信息", allowableValues="{成功,...,后台系统异常}", required = true, example="成功")
	private String msg = "成功"; 
	
	/**
	 * 返回的数据
	 */
	@ApiModelProperty(value = "Map<String,Object>中封装返回的数据对象", allowableValues="", required = false, example="是否返回该参数，以及返回的map中key的列表请在控制器中举例，进一步对该对象格式进行补充说明")
	private Map<String,Object> resBizMap = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getResBizMap() {
		return resBizMap;
	}

	public void setResBizMap(Map<String, Object> resBizMap) {
		this.resBizMap = resBizMap;
	}
	
	/**
	 * 向已创建的返回的对象的map中增加元素
	 * @param key
	 * @param obj
	 */
	public void put2ResBizMap(String key,Object obj){
		if(this.resBizMap == null){
			this.resBizMap = new HashMap<String,Object>();
		}
		this.resBizMap.put(key, obj);
	}
	
	/**
	 * 向已创建的返回的对象的map中增加元素
	 * @param key
	 * @param resBizMap
	 */
	public void put2ResBizMap(Map<String,Object> mapValue){
		if(this.resBizMap == null){
			this.resBizMap = new HashMap<String,Object>();
		}		
		this.resBizMap.putAll(mapValue);
	}
	
	/**
	 * 只返回成功
	 */
	public ApiResDto(){
	}
	
	
	/**
	 * 返回成功，并附加业务数据
	 * @param errorCode
	 * @param msg
	 */
	public ApiResDto(Map<String, Object> bizDataMap) {
		this.resBizMap = bizDataMap;
	}	
	
	/**
	 * 返回成功，并附加业务数据
	 * @param errorCode
	 * @param msg
	 */
	public ApiResDto(Object data) {
		if(this.resBizMap == null){
			this.resBizMap = new HashMap<String,Object>();
		}		
		this.resBizMap.put("data", data);
	}
	
	/**
	 * 返回成功，并附加业务数据
	 * @param errorCode
	 * @param msg
	 */
	public ApiResDto(String objName, Object data) {
		if(this.resBizMap == null){
			this.resBizMap = new HashMap<String,Object>();
		}		
		this.resBizMap.put(objName, data);
	}	
	
	/**
	 * 通过异常构造返回信息，根据异常设置失败错误码和错误提示信息
	 * @param errorCode
	 * @param msg
	 */
	public ApiResDto(Exception e) {
		CustomException renrphException = null;
		if(e instanceof CustomException){
			renrphException = (CustomException)e;
		}else{
			if(e instanceof HttpMessageNotReadableException){
				renrphException = new CustomException(CommErrCode.ParamIsInvalid);
			}else if(e instanceof HttpRequestMethodNotSupportedException){
				renrphException = new CustomException(CommErrCode.HttpRequestMethodNotSupportedException);
			}else{
				renrphException = new CustomException(CommErrCode.SysErr);
			}
		}
		
		this.code = renrphException.getErrCode();
		this.msg = getErrMsg(this.code,renrphException);
	}
	
	
	/**
	 * 错误提示先读取国际化的文件，读取不到，再使用程序的异常枚举中默认提示
	 * @param errCode
	 * @param renrphException
	 * @return
	 */
	private static String getErrMsg(String errCode, CustomException renrphException){
		String returnValue = MsgResources.getMessage(errCode);

		if(returnValue==null||"".equals(returnValue)){
			returnValue = renrphException.getErrMsg();
		}

		/*MessageFormat.format("the cell [{0}] value must in {1}", columnName, in);*/
		/*MessageFormat.format("原始密码错误，您今天还有{0}次机会", new Object[]{(setPwdCount-pwdCount)});*/
		returnValue= MessageFormat.format(returnValue, renrphException.getObjects());

		return returnValue;
	}
}
