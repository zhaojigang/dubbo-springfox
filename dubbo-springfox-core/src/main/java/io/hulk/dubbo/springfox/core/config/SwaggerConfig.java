package io.hulk.dubbo.springfox.core.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.hulk.dubbo.springfox.core.common.DubboSpringfoxContants;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Springfox配置类
 *
 * @author zhaojigang
 * @date 2018/4/18
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        final String serviceGroup = System.getProperty(DubboSpringfoxContants.SERVICE_GROUP, "dev");
        if (serviceGroup.equals(DubboSpringfoxContants.SERVICE_GROUP_PRODUCT)) {
            // 生产环境需要将swagger-ui中的接口信息隐藏
            return new Docket(DocumentationType.SWAGGER_2).select()
                    .apis(RequestHandlerSelectors.none())
                    .build();
        }
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfo("", "", "", "", new Contact("", "", ""), "", "", new ArrayList<VendorExtension>()));
    }
}