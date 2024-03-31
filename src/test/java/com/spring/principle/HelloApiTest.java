package com.spring.principle;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.spring.principle.examples.boot.section4.HelloController;

public class HelloApiTest {
    @Test
    void helloApi() {
        // http
        TestRestTemplate rest = new TestRestTemplate();

        // web 응답에 모든 요소를 가진 ResponseEntity
        ResponseEntity<String> res =
            rest.getForEntity("http://localhost:8080/hello?name={name}", String.class, "Spring");

        // 검증할 3가지
        // status code 200
        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        // header -> content-type : text/plain
        Assertions.assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE))
            .startsWith(MediaType.TEXT_PLAIN_VALUE);

        // body Hello Spring
        Assertions.assertThat(res.getBody()).isEqualTo("Hello Spring");

        // web 응답에 세가지 요소를 모두 검증했다.
    }

    @Test
    void helloController() {
        // 익명 클래스를 인자로 받는다 -> 그냥 람다로 치환한다.
        HelloController helloController = new HelloController(name -> name);
        String test = helloController.hello("Test");
        Assertions.assertThat(test).isEqualTo("Test");
    }

    @Test
    void failHelloController() {
        HelloController helloController = new HelloController(name -> name);

        Assertions.assertThatThrownBy(() -> helloController.hello(null))
            .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> helloController.hello(""))
            .isInstanceOf(IllegalArgumentException.class);
    }
}


