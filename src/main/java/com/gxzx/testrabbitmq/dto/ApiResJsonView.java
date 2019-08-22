package com.gxzx.testrabbitmq.dto;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import com.gxzx.testrabbitmq.exception.CustomException;
import com.gxzx.testrabbitmq.exception.CustomException.CommErrCode;
import com.gxzx.testrabbitmq.dto.MsgResources;

import io.swagger.annotations.ApiModelProperty;


/**
 * @author 04
 * API接口返回参数对象，与ApiResDto区别在于使用本类，只有一个泛型，数据包装在这个泛型类中。泛型可以是一个实体对象，里面的属性可以使用JsonView注解，屏蔽掉一些不需要返回的字段。
 * 如果使用ApiResDto，对于需要屏蔽的字段需要手动设置为null。
 * Include.NON_NULL用在实体类的方法类的头上  作用是实体类的参数查询到的为null的不显示
 */
@JsonInclude(Include.NON_NULL)
public class ApiResJsonView<T> extends BaseDTO{
	
    public interface IErrorView{};  
  
    public interface ISuccessView extends IErrorView{} ;  	

	private static final long serialVersionUID = -6726901716811552894L;

	/**
	 * 编码，默认success为成功，非success失败。
	 */
	@ApiModelProperty(value = "编码", allowableValues="{success,...,CommErrCode.SysErr}", required = true, example="success") 	
	private String code = "success";
	
	/**
	 * 提示信息，默认为成功
	 */
	@ApiModelProperty(value = "提示信息", allowableValues="{成功,...,后台系统异常}", required = true, example="success")
	private String msg = "成功"; 

	/**
	 * 返回的数据
	 */
	private T data = null;

	@JsonView(IErrorView.class)  
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonView(IErrorView.class)  
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}	

	@JsonView(ISuccessView.class)  
	public T getData() {
		return data;
	}

	public void setResBizMap(T data) {
		this.data = data;
	}
	
	/**
	 * 只返回成功
	 */
	public ApiResJsonView(){
	}
	
	
	/**
	 * 返回成功，并附加业务数据
	 * @param errorCode
	 * @param msg
	 */
	public ApiResJsonView(T data) {
		this.data = data;
	}		
	
	/**
	 * 通过异常构造返回信息，根据异常设置失败错误码和错误提示信息
	 * @param errorCode
	 * @param msg
	 */
	public ApiResJsonView(Exception e) {
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
		if(returnValue==null){
			returnValue = renrphException.getErrMsg();
		}
		return returnValue;
	}
}
