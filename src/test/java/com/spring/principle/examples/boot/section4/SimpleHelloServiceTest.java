package com.spring.principle.examples.boot.section4;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SimpleHelloServiceTest {

    @Test
    void simpleHelloService() {
        SimpleHelloService service = new SimpleHelloService();
        String ret = service.sayHello("Test");
        Assertions.assertThat(ret).isEqualTo("Hello Test");

        // server 를 미리 띄울 필요도 없다.
        // 고립된 테스트가 가능하다.
        // 그저 자바 코드를 실행하는 것이기 때문에 별도의 Httpie 를 요청할 필요도 없다.
    }

}