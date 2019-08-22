package com.gxzx.testrabbitmq.exception;

import java.io.Serializable;

public class CustomException extends RuntimeException  {





	private static final long serialVersionUID = 822524209672412825L;

	private String errCode;
	
	private String errMsg;

	private Object[] objects;

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}
	public  CustomException(){

	}
	/**
	 * 使用自定义异常枚举中的错误提示
	 * @param errObj
	 */
	public CustomException(IErrCode errObj)  
    {  
        super(errObj.getErrMsg());
		this.errCode = errObj.getErrCode();
		this.errMsg = errObj.getErrMsg();
    }


    public CustomException(IErrCode errObj, Object[] objects) {
        super(errObj.getErrMsg());
		this.errCode = errObj.getErrCode();
		this.errMsg = errObj.getErrMsg();
		this.objects = objects;
    }

	
	/**
	 * 使用自定义异常枚举中的错误提示，和传入的错误提示相结合
	 * @param errObj
	 * @param args
	 */
	public CustomException(IErrCode errObj,String templateStr, Object ... args)  
    {  
        super(errObj.getErrMsg());
		this.errCode = errObj.getErrCode();
		this.errMsg = String.format(errObj.getErrMsg()+templateStr, args);
    }
	
	/**
	 * 动态错误信息
	 * @param errMsg
	 * @param errCode
	 */
	public CustomException(String errMsg,String errCode) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
    @Override
    public String toString() {
        return "errCode=" + this.errCode + ",errMsg=" + this.errMsg;
    }
	
	public enum CommErrCode implements IErrCode ,Serializable{
		UserNameIsNotExist("UserNameIsNotExist","用户名不存在"),
		UserNameOrPwdIsWrong("UserNameOrPwdIsWrong","用户名或密码错误"),
		BadCredentials("BadCredentials","凭证错误"),
		ValidateCodeIsInvalid("ValidateCodeIsInvalid","校验码无效"),
		CommParamIsMissing("CommParamIsMissing","缺少必要参数"),
	    ParamIsInvalid("ParamIsInvalid","参数格式无效"),
	    BadReqParam("BadReqParam","请求参数错误"),
	    Unauthenticated("Unauthenticated","未通过身份认证"),
		NeedReal("NeedReal","请先完成实名认证"),
	    Unauthorized("Unauthorized","未通过权限认证"),    
	    NotFound("NotFound","请求的资源不存在"),
	    DBRecordNotFound("NotFound","请求的记录不存在"),
	    AppSecretIsIncorrect("AppSecretIsIncorrect","APP秘钥无效"),
	    AppDevIdIsNotExist("AppDevIdIsNotExist","APP设备ID不存在"),
	    SysErr("SysErr","后台系统异常"),
		NOTLOGIN("NOTLOGIN","尚未登录，或登录失效"),
	    NullString("NullString",""),
	    KickOutByAnotherLogin("KickOutByAnotherLogin","您已经在其他地方登录，请重新登录！"),
	    UpdateDataFailed("UpdateDataFailed","更新数据失败"),
	    IllegalArgument("IllegalArgument","请求的参数不正确"),
	    HttpRequestMethodNotSupportedException("HttpRequestMethodNotSupportedException","不支持此HTTP方法，请参考接口说明"),
	    LoginedSessionExpired("LoginedSessionExpired","登录会话超时，请重新登录"),
		BadRequestFrequency("BadRequestFrequency","过快的请求频次"),
		NotEcologicalFundAccount("NotEcologicalFundAccount","您不是生态基金用户不能登录"),
		LoginPwdErrCount("LoginPwdErrCount","登录密码已锁定，请1小时后再试"),
		UserForbidden("UserForbidden","用户已被禁用"),
		Zhanghaoweijihuo("Zhanghaoweijihuo","新用户请进入邮箱查看激活邮件"),
	    ;

		//loginPwdErrCount 登录密码已锁定，请1小时后再试
		//user_forbidden 用户已被禁用
		//zhanghaoweijihuo 新用户请进入邮箱查看激活邮件
		
		private String errCode;
		private String errMsg;

	    private CommErrCode(String errCode, String errMsg) {
	        this.setErrCode(errCode);
	        this.setErrMsg(errMsg);
	    }

	    @Override
	    public String getErrCode() {
			return this.getClass().getSimpleName()+"."+errCode;
		}

	    @Override
		public void setErrCode(String errCode) {
			this.errCode = errCode;
		}

	    @Override
		public String getErrMsg() {
			return errMsg;
		}

	    @Override
		public void setErrMsg(String errMsg) {
			this.errMsg = errMsg;
		}
	}	
	    
//
//	public static void main(String[] args)
//	{
//
//
//
//
//
//		RuntimeException e1 = new OtcException(CommErrCode.SysErr);
//		e1.printStackTrace();
//
//		RuntimeException e2 = new OtcException(CommErrCode.SysErr,"：%s？%s！%s？是的","这是哪里来的异常提示信息","是自定义的提示","可以用于透传第三方的错误提示");
//		e2.printStackTrace();
//	}

}
