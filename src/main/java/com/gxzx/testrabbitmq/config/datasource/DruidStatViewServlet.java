package com.gxzx.testrabbitmq.config.datasource;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;

@WebServlet(urlPatterns="/druid/*",  
    initParams={  
         @WebInitParam(name="allow",value="192.168.0.0/24,10.0.3.0/24"),// IP白名单(没有配置或者为空，则允许所有访问)  
         @WebInitParam(name="deny",value=""),// IP黑名单 (共同存在时，deny优先于allow)  
         @WebInitParam(name="loginUsername",value="druid@cpct.pro"),// 用户名  
         @WebInitParam(name="loginPassword",value="druid123@abc987"),// 密码
         @WebInitParam(name="resetEnable",value="true")// 禁用HTML页面上的“Reset All”功能  
})  
public class DruidStatViewServlet extends StatViewServlet {  
    private static final long serialVersionUID = -2688872071445249539L;  
} 
