package com.gxzx.testrabbitmq.web.biz;

import com.gxzx.testrabbitmq.exception.IErrCode;
import com.gxzx.testrabbitmq.web.entity.EntrustOrder;
import com.gxzx.testrabbitmq.web.entity.Market;

public interface IEntrustOrderService {
	/**
	 *该异常枚举非模板自动生成，使用时service直接向上抛出
	 *如： throw new OtcException(IEntrustOrderServiceErrCode.ParamIsMissing);
	 */
	public enum IEntrustOrderServiceErrCode implements IErrCode {
        ParamIsMissing("ParamIsMissing", "必填的行情配置表业务参数不能为空"),
        ParamIsWrong("ParamIsWrong", "业务参数异常"),
        ;

        private String errCode;
        private String errMsg;

        private IEntrustOrderServiceErrCode(String errCode, String errMsg) {
            this.setErrCode(errCode);
            this.setErrMsg(errMsg);
        }

        @Override
        public String getErrCode() {
            return this.getClass().getSimpleName() + "." + errCode;
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
	
	boolean createOrder(EntrustOrder entity, Market market);

}
