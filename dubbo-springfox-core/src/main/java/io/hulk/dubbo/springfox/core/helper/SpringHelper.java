package io.hulk.dubbo.springfox.core.helper;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring工具类
 *
 * @author zhaojigang
 * @date 2018/5/16
 */
public class SpringHelper {

    /**
     * 获取clazz的BeanDefinition并注册到BeanDefinitionRegistry中
     *
     * @param clazz
     * @param context
     */
    public static void registerBeanDefinition(Class<?> clazz, ApplicationContext context) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanDefinitonRegistry.registerBeanDefinition(clazz.getCanonicalName(), beanDefinitionBuilder.getBeanDefinition());
    }
}
