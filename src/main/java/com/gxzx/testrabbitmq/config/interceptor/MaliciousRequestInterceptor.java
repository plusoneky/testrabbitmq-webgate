package com.gxzx.testrabbitmq.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.gxzx.testrabbitmq.util.WebUtil;
	
/**
 * 恶意请求拦截器
 */
public class MaliciousRequestInterceptor extends BaseInterceptor {
	
	/** 上次请求地址 */
	static final String PREREQUEST = "PREREQUEST";
	/** 上次请求时间 */
	static final String PREREQUEST_TIME = "PREREQUEST_TIME";
	/** 非法请求次数 */
	static final String MALICIOUS_REQUEST_TIMES = "MALICIOUS_REQUEST_TIMES";	
	
	private boolean allRequest = false;
	
	private long minRequestIntervalTime;
	
	private long maxMaliciousTimes;
	
	@Autowired
	public MaliciousRequestInterceptor(long minRequestIntervalTime, long maxMaliciousTimes){
		this.minRequestIntervalTime = minRequestIntervalTime;
		this.maxMaliciousTimes = maxMaliciousTimes;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getServletPath();
		if (url.endsWith("/unauthorized") || url.endsWith("/forbidden")) {
			return super.preHandle(request, response, handler);
		}	
		
		HttpSession session = request.getSession();
		String preRequest = (String) session.getAttribute(PREREQUEST);
		Long preRequestTime = (Long) session.getAttribute(PREREQUEST_TIME);
		if (preRequestTime != null && preRequest != null) { // 过滤频繁操作
			if ((url.equals(preRequest) || allRequest)
					&& System.currentTimeMillis() - preRequestTime < minRequestIntervalTime) {
				Long maliciousRequestTimes = (Long) session.getAttribute(MALICIOUS_REQUEST_TIMES);
				if (maliciousRequestTimes == null) {
					maliciousRequestTimes = 1L;
				} else {
					maliciousRequestTimes++;
				}
				session.setAttribute(MALICIOUS_REQUEST_TIMES, maliciousRequestTimes);
				if (maliciousRequestTimes > maxMaliciousTimes) {
					
					response.setStatus(HttpStatus.OK.value());
		    		response.setContentType("application/json;charset=UTF-8");
		    		response.getWriter().write("{\"code\":\"CommErrCode.BadRequestFrequency\",\"msg\":\"系统服务异常中断，请稍后重试\"}");
					logger.error("malicious-request: ip="+WebUtil.getHost(request)+",url=" +url);
					return false;
				}
			} else {
				session.setAttribute(MALICIOUS_REQUEST_TIMES, 0L);
			}
		}
		session.setAttribute(PREREQUEST, url);
		session.setAttribute(PREREQUEST_TIME, System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	}

}
