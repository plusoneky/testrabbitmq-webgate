package com.gxzx.testrabbitmq.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gxzx.testrabbitmq.dto.ApiReqDto;
import com.gxzx.testrabbitmq.dto.ApiResDto;
import com.gxzx.testrabbitmq.exception.CustomException;
import com.gxzx.testrabbitmq.exception.CustomException.CommErrCode;
import com.gxzx.testrabbitmq.web.biz.IEntrustOrderService;
import com.gxzx.testrabbitmq.web.entity.EntrustOrder;
import com.gxzx.testrabbitmq.web.entity.Market;
import com.gxzx.testrabbitmq.web.service.IMarketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
/**
 * <p>
 * 委托单 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2019-08-21
 */
@Api(value = "委托单", tags = "委托单")
@RestController
@RequestMapping("/entrust-order")
public class EntrustOrderController {

	@Autowired
	IEntrustOrderService entrustOrderServiceImpl;
	
	@Autowired
	IMarketService marketServiceImpl;

	/**
	 * 推荐使用构造器注入
	 * @param entrustOrderServiceImpl
	 */	
	@Autowired
	EntrustOrderController(IEntrustOrderService entrustOrderServiceImpl) {
		this.entrustOrderServiceImpl = entrustOrderServiceImpl;
	}
	
	@ApiOperation(value = "用户挂单", notes = "用户挂单",  response = ApiResDto.class, httpMethod = "POST")
	@PostMapping(produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ApiResDto insert(@ApiParam(required = true, value = "委托单") @RequestBody ApiReqDto<EntrustOrder> apiReqDto) {
		if(apiReqDto==null || apiReqDto.getReqBizObj()==null){
			return new ApiResDto(new CustomException(CommErrCode.ParamIsInvalid));
		}
		
		//数据校验及获取相关这里应当优化为从Redis缓存读取
		Market market = marketServiceImpl.getById(apiReqDto.getReqBizObj().getMarketId());

		//预估值记账后把订单发送到MQ
		boolean insertResult = entrustOrderServiceImpl.createOrder(apiReqDto.getReqBizObj(), market);
		if(!insertResult){
			return new ApiResDto(new CustomException(CommErrCode.SysErr));
		}
		return new ApiResDto();
	}		

}
