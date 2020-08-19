package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@EnableAsync
@RestController
@SpringBootApplication
public class SimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class, args);
    }

    @Autowired
    private AsyncDemo asyncDemo;

    @GetMapping(value = "/asyncDemo")
    public String asyncDemo() throws ExecutionException, InterruptedException {
        asyncDemo.asyncInvokeSimplest();
        asyncDemo.asyncInvokeWithParameter("test");
        Future<String> future = asyncDemo.asyncInvokeReturnFuture(100);
        System.out.println(future.get());
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            asyncDemo.asyncInvokeWithParameter(random.nextInt(10));
        }
        return "ok";
    }
}
