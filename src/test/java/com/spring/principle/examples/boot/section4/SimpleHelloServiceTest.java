package com.spring.principle.examples.boot.section4;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@UnitTest
@interface FastTest {

}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Test
@interface UnitTest {
}

class SimpleHelloServiceTest {

    @UnitTest
    void simpleHelloService() {
        SimpleHelloService service = new SimpleHelloService();
        String ret = service.sayHello("Test");
        Assertions.assertThat(ret).isEqualTo("Hello Test");

        // server 를 미리 띄울 필요도 없다.
        // 고립된 테스트가 가능하다.
        // 그저 자바 코드를 실행하는 것이기 때문에 별도의 Httpie 를 요청할 필요도 없다.
    }

    @Test
    void helloDecorator() {
        HelloDecorator helloDecorator = new HelloDecorator(name -> name);
        String ret = helloDecorator.sayHello("TEST");
        Assertions.assertThat(ret).isEqualTo("**TEST**");
    }

}