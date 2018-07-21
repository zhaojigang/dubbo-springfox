package io.hulk.dubbo.springfox.core.scanner;

import com.alibaba.dubbo.config.spring.ServiceBean;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * xml配置的dubbo-api扫描器
 * <p>
 * 原理：
 * 以xml配置的dubbo-api，在dubbo服务启动时，会将dubbo-api转化为ServiceBean注册到ApplicationContext中，
 * 所以从ApplicationContext中获取所有的ServiceBean，就拿到了所有的dubbo-api。
 *
 * @author zhaojigang
 * @date 2018/5/15
 */
public class XmlDubboApiScanner implements ApiScanner<ApplicationContext, Map<String, ServiceBean>> {
    @Override
    public Map<String, ServiceBean> scanFromSpringContext(ApplicationContext context) {
        if (context == null) {
            return new HashMap<>(0);
        }
        return context.getBeansOfType(ServiceBean.class);
    }
}
