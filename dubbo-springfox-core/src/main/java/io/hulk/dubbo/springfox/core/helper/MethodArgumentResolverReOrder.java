package io.hulk.dubbo.springfox.core.helper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 调整springmvc参数解析器的作用顺序
 *
 * 详细信息见：https://www.cnblogs.com/java-zhao/p/9119258.html
 *
 * @author zhaojigang
 * @date 2018/5/17
 */
@Configuration
public class MethodArgumentResolverReOrder {
    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @PostConstruct
    public void reorder() {
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new RequestModelArgumentResolver());
        argumentResolvers.addAll(adapter.getArgumentResolvers());
        adapter.setArgumentResolvers(argumentResolvers);
    }
}