package io.hulk.dubbo.springfox.core.helper;

import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;

import springfox.documentation.spi.annotations.RequestModel;

/**
 * springmvc自定义参数注解解析器
 *
 * springfox.documentation.spi.annotations.RequestModel 该参数注解是自定义的注解，集成在https://github.com/zhaojigang/springfox项目中。
 * 该注解的作用是为了打破@RequestBody的单体限制，详细信息见这里：https://www.cnblogs.com/java-zhao/p/9119258.html
 *
 * @author zhaojigang
 * @date 2018/5/2
 */
public class RequestModelArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String parameterJson = webRequest.getParameter(parameter.getParameterName());

        final Type type = parameter.getGenericParameterType();
        final Object o = JSON.parseObject(parameterJson, type);
        return o;
    }
}
