package com.spring.principle;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


@Configuration
@ComponentScan
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
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
            @Override
            protected void onRefresh() {
                super.onRefresh();

                // 아래 두 오브젝트는 애플리케이션의 기능을 담당하는 것은 아니지만, 이 둘이 없다면 스프링이 시작할 수가 없다.
                // 이 오브젝트들도 스프링 빈으로 만들어서 관리해보자.
                ServletWebServerFactory tomcatServletWebServerFactory = this.getBean(ServletWebServerFactory.class);
                DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);
                // dispatcherServlet.setApplicationContext(this); 이 코드가 없어도 dispatcherServlet 의 생성자에 this (applicationContext) 가 자동으로 주입된다.
                // 어떻게? 스프링 컨테이너가 자동으로 dispatcherServlet 에 applicationContext 를 주입한 것

                // DispatcherServlet 의 hierarchy 를 살펴보면 ApplicationContextAware Interface 를 구현하는 것을 볼 수 있는데
                // 해당 인터페이스의 setApplicationContext() method 를 보면 ApplicationContext 타입을 인자로 받고 있는 것을 볼 수 있다.
                // 때문에 자동으로 이게 들어간다(?) 논리가 약하군.


                WebServer webServer = tomcatServletWebServerFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet", dispatcherServlet)
                        .addMapping("/*");
                });
                webServer.start();
            }
        };

        applicationContext.register(PrincipleApplication.class);
        applicationContext.refresh();
    }
}
