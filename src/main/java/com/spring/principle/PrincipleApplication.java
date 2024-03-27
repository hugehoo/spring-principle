package com.spring.principle;


import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;

import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.spring.principle.examples.boot.section4.HelloController;
import com.spring.principle.examples.boot.section4.SimpleHelloService;

public class PrincipleApplication {
    public static void main(String[] args) {
        GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
        applicationContext.registerBean(HelloController.class);
        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.refresh();

        // ServletContainer 를 코드롤 등록하면서 servlet 을 실행한다.
        ServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        WebServer webServer = tomcatServletWebServerFactory.getWebServer(servletContext -> {
            servletContext.addServlet("dispatcherServlet",
                new DispatcherServlet(applicationContext))
                .addMapping("/*");
        });
        webServer.start();
    }
}
