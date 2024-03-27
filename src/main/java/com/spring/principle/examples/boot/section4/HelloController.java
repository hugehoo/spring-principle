package com.spring.principle.examples.boot.section4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(String name) {
        SimpleHelloService helloService = new SimpleHelloService();
        return helloService.sayHello(name);
    }

}

