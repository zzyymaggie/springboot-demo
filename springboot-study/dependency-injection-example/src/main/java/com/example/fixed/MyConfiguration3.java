package com.example.fixed;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration3 {

    @Bean
    public HelloProperties helloProperties() {
        return new HelloProperties();
    }

    @Bean
    public MyPost2 myPost2(HelloProperties helloProperties){
        System.out.println("MyPost2:" + helloProperties.isLog());
        return new MyPost2();
    }

    @Bean
    public MyPriorityPost2 myPriorityPost2(){
        return new MyPriorityPost2();
    }
}
