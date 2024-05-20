package com.spring.principle.examples.boot.section6.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration(proxyBeanMethods = false) // default is true -> proxyBeanMethods 를 false 로 바꾼다는 것은
public @interface MyAutoConfiguration {
}
