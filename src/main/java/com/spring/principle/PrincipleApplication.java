package com.spring.principle;

import java.io.IOException;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.spring.principle.examples.boot.section2.HelloController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PrincipleApplication {
    public static void main(String[] args) {
        // spring container 를 만들어 보자.
        GenericApplicationContext applicationContext = new GenericApplicationContext();// 얘가 결국 스프링컨테이너가 된다.
        // 스프링 컨테이너는 오브젝트를 직접 생성하여 넣어 줄 수 도 있지만,
        // 어떤 클래스를 이용해서 빈 오브젝트를 생성할 것인가 라는 메타정보를 넣어 주는 방식으로 구성할 수 있다.
        applicationContext.registerBean(HelloController.class); // object 를 넘기는 것이 아니라 클래스 정보만 넘긴다.
        applicationContext.refresh(); // container 를 초기화

        ServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        WebServer webServer = tomcatServletWebServerFactory.getWebServer(servletContext -> {
            servletContext.addServlet("frontController", new HttpServlet() {
                @Override
                public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
                    if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
                        String name = req.getParameter("name");
                        HelloController helloController = applicationContext.getBean(HelloController.class);
                        String hello = helloController.hello(name);
                        // application context 는 HelloController 라는 타입의 빈이 어떻게 생성됐는지는 알지 못해도 상관없다. 그저 가져다 사용만 할 뿐이다.
                        res.setContentType(MediaType.TEXT_PLAIN_VALUE);
                        res.getWriter().println(hello);
                    } else {
                        res.setStatus(HttpStatus.NOT_FOUND.value());
                    }

                }
            }).addMapping("/*"); // "/hello" 로 들어오는 요청이 있으면 여기서 익명클래스로 만든 오브젝트가 처리하겠다는 의미.
        });
        webServer.start(); // tomcat servlet container 가 실행된다.
    }
}
