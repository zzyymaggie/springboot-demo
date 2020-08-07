package com.example.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Value("${hello.log}")
    private boolean log;

    public boolean isLog() {
        return log;
    }

    @Bean
    public MyBeanTest myBeanTest() {
        System.out.println("myBeanTest:" + log);
        return new MyBeanTest();
    }
}
