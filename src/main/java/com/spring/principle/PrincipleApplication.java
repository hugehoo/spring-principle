package com.spring.principle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.spring.principle.examples.consumer.AvroConsumer;
import com.spring.principle.examples.consumer.AvroJavaConsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class PrincipleApplication {
	public static void main(String[] args) {
		// var consumer = new AvroConsumer();
		// consumer.consume();
		AvroJavaConsumer avroJavaConsumer = new AvroJavaConsumer();
		avroJavaConsumer.consumerAvro();

		SpringApplication.run(PrincipleApplication.class, args);
	}
}
