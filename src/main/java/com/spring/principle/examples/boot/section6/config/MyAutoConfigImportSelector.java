package com.spring.principle.examples.boot.section6.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector {
    private final ClassLoader classLoader;
    // 클래스패스에서 파일을 읽어올 때 클래스로더가 필요하다.

    public MyAutoConfigImportSelector(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> autoConfigs = new ArrayList<>();
        // Iterator 와 Iterable 차이?
        ImportCandidates.load(MyAutoConfiguration.class, classLoader)
            .forEach(autoConfigs::add);
        // ImportCandidates 내부를 살펴보면 import candidates 클래스가 어디에 저장돼 있는지 확인할 수 있다.
        return autoConfigs.toArray(String[]::new);


        // return new String[]{
        //     "com.spring.principle.examples.boot.section6.config.autoconfig.DispatcherServletConfig",
        //     "com.spring.principle.examples.boot.section6.config.autoconfig.TomcatWebServerConfig"
        // };
    }
}
