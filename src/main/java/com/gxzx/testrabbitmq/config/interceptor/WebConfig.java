package com.gxzx.testrabbitmq.config.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gxzx.testrabbitmq.config.properties.BizProperties;
import com.gxzx.testrabbitmq.config.properties.ProjectProperties;
import com.gxzx.testrabbitmq.config.properties.SysProperties;
import com.gxzx.testrabbitmq.util.idworker.SnowflakeIdWorker;

@Configuration
public class WebConfig implements WebMvcConfigurer{ // WebMvcConfigurerAdapter {

	@Autowired
	private ProjectProperties projectProperties;
	
	@Bean
	MaliciousRequestInterceptor maliciousRequestInterceptor(){
		return new MaliciousRequestInterceptor(projectProperties.getBizProperties().getMinRequestIntervalTime(), projectProperties.getBizProperties().getMaxMaliciousTimes());
	}
	
	@Bean
	LocaleInterceptor localeInterceptor(){
		return new LocaleInterceptor();
	}
	
	// 跨域请求处理
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);
	}

	private CorsConfiguration buildConfig() {
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		// 可以自行筛选
		corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
		corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
		corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
		return corsConfiguration;
	}	

	public void addInterceptors(InterceptorRegistry registry) {
		String[] excludePathPatterns = { "/*.ico", "/*/api-docs", "/swagger**", "/webjars/**", "/configuration/**" };

		registry.addInterceptor(maliciousRequestInterceptor()).addPathPatterns(new String[] { "/**" })
				.excludePathPatterns(excludePathPatterns);

		registry.addInterceptor(localeInterceptor()).addPathPatterns(new String[] { "/**" })
				.excludePathPatterns(excludePathPatterns);

	}
	
	@Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
    	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    	//设置日期格式
//    	ObjectMapper objectMapper = new ObjectMapper();
//    	SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd");
//    	objectMapper.setDateFormat(smt);
//    	mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
    	//设置中文编码格式
    	List<MediaType> list = new ArrayList<MediaType>();
    	list.add(MediaType.APPLICATION_JSON_UTF8);
    	mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
    	return mappingJackson2HttpMessageConverter;
    }	
	
	/**
	 * ConfigListener注册
	 */
	@Bean
	public ServletListenerRegistrationBean<ContextListener> configListenerRegistration() {
		return new ServletListenerRegistrationBean<>(new ContextListener());
	}	
	
    /**
     *  雪花算法
     */
    @Bean(name = "snowflakeIdWorker")
    public SnowflakeIdWorker getIdWorker() {
        return new SnowflakeIdWorker(projectProperties.getSysProperties().getSnowflakeIdWorker().getWorkMachineId(), projectProperties.getSysProperties().getSnowflakeIdWorker().getDataCenterId());
    }
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/test1").setViewName("testajax/test1");
		registry.addViewController("/test2").setViewName("testajax/test2");
		registry.addViewController("/test3").setViewName("testajax/test3");
		registry.addViewController("/test4").setViewName("testajax/test4");
		registry.addViewController("/test5").setViewName("testajax/test5");
		registry.addViewController("/test6").setViewName("testajax/test6");
		registry.addViewController("/test7").setViewName("testajax/test7");
		registry.addViewController("/test8").setViewName("testajax/test8");
	}
}
