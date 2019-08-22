package com.gxzx.testrabbitmq.config.interceptor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextListener implements ServletContextListener {

    /** 日志记录 */
    private Logger logger = LoggerFactory.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	logger.info("初始化WEB容器");
    }

    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("销毁dubbo实例中....");
        //DubboRegistryFactory.destroyAll();
        DubboProtocol.getDubboProtocol().destroy();
        logger.info("销毁dubbo服务完成！");
    }    

}
