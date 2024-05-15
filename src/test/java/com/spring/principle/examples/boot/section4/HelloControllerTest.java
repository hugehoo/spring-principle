package com.spring.principle.examples.boot.section4;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloControllerTest {

    @Test
    void helloController() {
        HelloController helloController = new HelloController(name -> name);
        String ret = helloController.hello("TEST");
        Assertions.assertThat(ret).isEqualTo("TEST");


    }
    @Test
    void failsHelloController() {
        HelloController helloController = new HelloController(name -> name);
        Assertions.assertThatThrownBy(() -> {
            String ret = helloController.hello(null);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            String ret = helloController.hello("");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}