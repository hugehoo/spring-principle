package com.spring.principle.examples.boot.section6;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ConfigurationTest {

    @Test
    void configuration() {
        Assertions.assertThat(new Common()).isNotSameAs(new Common());
        // 서로 다른 Common 객체가 생성된다,

        Common common = new Common();
        Assertions.assertThat(common).isSameAs(common);
    }

    @Test
    void configurationPojo() {
        MyConfig config = new MyConfig();
        Bean1 bean1 = config.bean1();
        Bean2 bean2 = config.bean2();

        Assertions.assertThat(bean1.common).isNotSameAs(bean2.common);
    }

    @Test
    void configurationApplicationContext() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(MyConfig.class);
        ac.refresh();

        Bean1 bean1 = ac.getBean(Bean1.class);
        Bean2 bean2 = ac.getBean(Bean2.class);

        Assertions.assertThat(bean1.common).isSameAs(bean2.common);
    }

    @Test // spring 내부의 동작 방식을 흉내낸 것
    void proxyCommonMethod() {
        MyConfigProxy myConfigProxy = new MyConfigProxy();
        Bean1 bean1 = myConfigProxy.bean1();
        Bean2 bean2 = myConfigProxy.bean2();
        Assertions.assertThat(bean1.common).isSameAs(bean2.common);
    }

    // 하나의 빈을 두개 이상의 다른 빈에서 의존하고 있다면, 팩토리 멕서드를 호출할 때마다 새로운 빈이 생성되게 된다.
    // 이 점을 해결하기 위해 스프링에서는ㄴ @Configuration 어노테이션이 붙은 빈은 프록시를 만들어 기능을 확장한다(한번만 생성되도록).

    // SchedulingConfiguration 클래스를 보면, proxyBeanMethods=false 로 설정돼 있는데, 이는 해당 Bean 이 다른 Bean 을 의존하지 않기 때문에 굳이 프록시를 만들 필요가 없어서 이다.
    static class MyConfigProxy extends MyConfig {
        private Common common;

        @Override
        Common common() {
            if (this.common == null)
                this.common = super.common();
            return this.common;
        }
    }


    @Configuration
    static class MyConfig {
        @Bean
        Common common() {
            return new Common();
        }

        @Bean
        Bean1 bean1() {
            return new Bean1(common());
        }

        @Bean
        Bean2 bean2() {
            return new Bean2(common());
        }
    }

    static class Bean2 {
        private final Common common;
        public Bean2(Common common) {
            this.common = common;
        }
    }

    static class Bean1 {
        private final Common common;
        public Bean1(Common common) {
            this.common = common;
        }
    }

    static class Common {
    }

    // Bean1 <-- common
    // Bean2 <-- common
    // spring 의 bean 은 대부분 singleton 으로 등록된다.
    // 그말은 즉슨, Bean1, Bean2 가 의존하는 Common 이란 bean 은 동일한 bean 이라는 뜻.
}
