package com.spring.principle;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import com.spring.principle.examples.boot.section4.MySpringApplication;
import com.spring.principle.examples.boot.section6.config.MySpringBootApplication;

// @Configuration
// @ComponentScan
@MySpringBootApplication
public class PrincipleApplication {

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    public static void main(String[] args) {
        MySpringApplication.run(PrincipleApplication.class, args);
    }
}
