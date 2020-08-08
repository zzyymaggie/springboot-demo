package com.example.issue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration2 {

    @Value("${hello.log}")
    private boolean log;

    public boolean isLog() {
        return log;
    }

    @Bean
    public MyPost myPost(){
        System.out.println("myPost:" + log);
        return new MyPost();
    }

    /**
     *
     * MyPriorityPost实现了PriorityOrdered,影响了AutoConfiguration的@Value的解析
     * 去掉它，可以获取到正确的参数值
     */
    @Bean
    public MyPriorityPost myPriorityPost(){
        return new MyPriorityPost();
    }
}
