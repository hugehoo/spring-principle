package com.spring.principle.examples.boot.section4;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class HelloDecorator implements HelloService {
// SimpleHelloService 에 대해선 모르는 상태
    private final HelloService helloService;

    public HelloDecorator(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public String sayHello(String name) {
        return "**" + helloService.sayHello(name) + "**";
    }

}
