package io.hulk.dubbo.springfox.core.scanner;

/**
 * api scanner
 *
 * @author zhaojigang
 * @date 2018/5/16
 */
public interface ApiScanner<T, R> {
    R scanFromSpringContext(T t);
}
