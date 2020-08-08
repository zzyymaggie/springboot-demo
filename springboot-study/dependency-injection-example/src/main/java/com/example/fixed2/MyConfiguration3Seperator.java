package com.example.fixed2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration3Seperator {
    @Bean
    public MyPriorityPost3 myPriorityPost3(){
        return new MyPriorityPost3();
    }
}
