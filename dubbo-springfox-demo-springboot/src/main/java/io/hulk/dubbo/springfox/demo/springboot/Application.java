package io.hulk.dubbo.springfox.demo.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zhaojigang
 * @date 2018/3/13
 */
@SpringBootApplication
@EnableSwagger2
@ImportResource({ "classpath:*.xml" })
@ComponentScan({ "io.hulk.dubbo.springfox.demo.springboot", "io.hulk.dubbo.springfox.core" })
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}