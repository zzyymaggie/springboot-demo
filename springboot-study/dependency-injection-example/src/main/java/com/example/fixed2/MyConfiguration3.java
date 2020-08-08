package com.example.fixed2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration3 {

    @Value("${hello.log}")
    private boolean log;

    public boolean isLog() {
        return log;
    }

    @Bean
    public MyPost3 myPost3(){
        System.out.println("myPost3:" + log);
        return new MyPost3();
    }
}
