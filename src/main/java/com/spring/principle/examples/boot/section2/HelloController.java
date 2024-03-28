package com.spring.principle.examples.boot.section2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class HelloController {

    @GetMapping("/hello")
    public String hello(String name) {
        return "hello" + name;
    }

}

