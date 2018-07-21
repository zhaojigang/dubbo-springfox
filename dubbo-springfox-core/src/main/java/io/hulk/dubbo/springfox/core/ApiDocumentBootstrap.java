package io.hulk.dubbo.springfox.core;

import java.util.Map;

import io.hulk.dubbo.springfox.core.common.DubboSpringfoxContants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;
import io.hulk.dubbo.springfox.core.builder.ClassCodeGenerator;
import io.hulk.dubbo.springfox.core.compiler.JdkCompiler;
import io.hulk.dubbo.springfox.core.helper.SpringHelper;
import io.hulk.dubbo.springfox.core.scanner.ApiScanner;
import io.hulk.dubbo.springfox.core.scanner.XmlDubboApiScanner;

/**
 * api文档扫描主类
 *
 * @author zhaojigang
 * @date 2018/5/14
 */
@Configuration
public class ApiDocumentBootstrap implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiDocumentBootstrap.class);

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        try {
            /** 1 获取dubboXmlApi扫描器 */
            final ApiScanner<ApplicationContext, Map<String, ServiceBean>> dubboXmlApiScanner = new XmlDubboApiScanner();

            /** 2 使用dubboXmlApi扫描器从ApplicationContext中获取ServiceBean（dubbo:service配置）*/
            final Map<String, ServiceBean> serviceBeanMap = dubboXmlApiScanner.scanFromSpringContext(context);

            /** 3 获取JdkCompiler实例 */
            final JdkCompiler jdkCompiler = new JdkCompiler();

            String serviceGroup = "";

            for (ServiceBean sb : serviceBeanMap.values()) {

                /** 4 根据ServiceBean的相关信息生成dubbo:service对应的controller类字符串 */
                final String code = ClassCodeGenerator.generateClassCode(sb);

                /** 5 使用JdkCompiler实例编译加载controller类字符串为Class对象 */
                final Class<?> dubboControllerClazz = jdkCompiler.compile(code,
                    ApiDocumentBootstrap.class.getClassLoader());

                /** 6 将生成的controller类Class对象注册到BeanDefinitionRegistry中，接下来供mvc扫描器进行扫描 */
                SpringHelper.registerBeanDefinition(dubboControllerClazz, context);

                serviceGroup = serviceGroup == "" ? sb.getGroup() : serviceGroup;
            }

            /**
             * 7 设置系统属性
             */
            System.setProperty(DubboSpringfoxContants.SERVICE_GROUP, serviceGroup == null ? "" : serviceGroup);
        } catch (Exception e) {
            LOGGER.error("dubbo-springfox generate api-document error, msg：", e);
        }
    }
}