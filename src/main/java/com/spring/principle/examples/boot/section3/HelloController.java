package com.spring.principle.examples.boot.section3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class HelloController {
    public String hello() {
        return "hello";
    }
}

