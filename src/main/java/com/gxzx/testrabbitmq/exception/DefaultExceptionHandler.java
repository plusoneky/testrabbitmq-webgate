package com.gxzx.testrabbitmq.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import com.gxzx.testrabbitmq.dto.ApiResDto;



@ControllerAdvice
public class DefaultExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
     * 全局异常处理器 
     */
    @ExceptionHandler({Exception.class})
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)    
    public ApiResDto processException(NativeWebRequest request, Exception e) {
    	if(e instanceof CustomException){
    		CustomException ex = (CustomException)e;
			logger.info("CustomException: request="+request.toString()+", errCode="+ex.getErrCode());	
    	}else if(e instanceof java.lang.IllegalArgumentException){
			e = new CustomException(CustomException.CommErrCode.IllegalArgument);
			logger.info("IllegalArgumentException: request="+request.toString(),e);			
		}else{
			logger.error("Exception: request="+request.toString(),e);			
		}
		return new ApiResDto(e);
    }  
    
}
