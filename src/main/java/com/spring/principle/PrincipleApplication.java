package com.spring.principle;

import java.awt.*;
import java.io.IOException;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;

import com.google.common.net.HttpHeaders;
import com.spring.principle.examples.boot.section2.HelloController;
import com.spring.principle.examples.consumer.AvroConsumer;
import com.spring.principle.examples.consumer.AvroJavaConsumer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


public class PrincipleApplication {
	public static void main(String[] args) {
		System.out.println("Start from bottom of springboot");
		ServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
		WebServer webServer = tomcatServletWebServerFactory.getWebServer(servletContext -> {

			HelloController helloController = new HelloController();
			servletContext.addServlet("frontController", new HttpServlet() {
				@Override
				public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
					// 매핑과 바인딩을 담당 -> 매핑은 웹 uri 의 정보를 가지고 어떤 로직을 수행하는 코드를 호출할지 결정하는 과정
					// 인증, 보안, 다국어, 공통 기능 -> front controller 가 담당
					if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
						String name = req.getParameter("name");
						String hello = helloController.hello(name); // 복잡한 로직()은 hello() 메서드 내에 모두 옮겼다.
						res.setStatus(HttpStatus.OK.value());
						res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
						res.getWriter().println(hello);
					} else if (req.getRequestURI().equals("/user")) {
						//
					} else {
						res.setStatus(HttpStatus.NOT_FOUND.value());
					}

				}
			}).addMapping("/*"); // "/hello" 로 들어오는 요청이 있으면 여기서 익명클래스로 만든 오브젝트가 처리하겠다는 의미.
		});
		webServer.start(); // tomcat servlet container 가 실행된다.
	}
}
