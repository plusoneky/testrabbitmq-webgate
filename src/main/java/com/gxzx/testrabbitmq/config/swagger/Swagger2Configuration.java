package com.gxzx.testrabbitmq.config.swagger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import io.swagger.annotations.Api;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	
    // 设置默认TOKEN，方便测试一年有效
    private static final String MOCK_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJvdGNVc2VySWRcIjoxLFwib3RjVXNlck5hbWVcIjpcIjEzNTkwMzgyNTE1XCIsXCJwYXNzd29yZFwiOlwiODY4ZjM2YTEzMzNmZDU3OTJjZjVjYjFhZTFkNjMzZDdcIixcInVzZXJUeXBlXCI6MCxcInJlYWxcIjp0cnVlfSIsImV4cCI6MTU3ODcyNTMyN30.mDbRsQLnia_IJw0k5gs3x8Y53fkfVowQbhndXESTjrqTxl_OKjmufSMUQI1has3_Qtdqk83bcql-0lXirQOfFg";

	/**
	 * 屏蔽掉类似basic-error-controller的接口说明 
	 * @return
	 */
	@Bean
	public Docket demoApi() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> listParmeter = new ArrayList<Parameter>();
        parameterBuilder.name("Authorization")
	        .description("OTC登录令牌")
	        .defaultValue(MOCK_TOKEN)
	        .modelRef(new ModelRef("string"))
	        .parameterType("header").required(false).build();
        listParmeter.add(parameterBuilder.build());		
		
	    return new Docket(DocumentationType.SWAGGER_2)
	    		.directModelSubstitute(LocalDateTime.class, Long.class)   //日期类型设定为long类型的参数，毫秒数
	    		.directModelSubstitute(Date.class, Long.class)   //日期类型设定为long类型的参数，毫秒数
	            .select()
	            //.apis(Swagger2Configuration.basePackage("com.gxzx.otc.web.modular,"))  //多包路径
	            .apis(RequestHandlerSelectors.basePackage("com.gxzx.testrabbitmq.web.controller"))
	            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
	            .paths(Predicates.not(PathSelectors.regex("/error.*")))
	            .build()
	            .globalOperationParameters(listParmeter);
	}
	
	
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                return declaringClass(input).transform(handlerPackage(basePackage)).or(true);
            }
        };
    }

    /**
     * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开 
     * @param basePackage 扫描包路径
     * @return Function
     */
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return new Function<Class<?>, Boolean>() {

            @Override
            public Boolean apply(Class<?> input) {
                for (String strPackage : basePackage.split(",")) {
                    boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                    if (isMatch) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * @param input RequestHandler
     * @return Optional
     */
    @SuppressWarnings("deprecation")
	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

	

}
