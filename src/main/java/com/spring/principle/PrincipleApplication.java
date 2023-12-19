package com.spring.principle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class PrincipleApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrincipleApplication.class, args);
	}
}
