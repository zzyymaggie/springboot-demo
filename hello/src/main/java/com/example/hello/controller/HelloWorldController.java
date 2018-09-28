package com.example.hello.controller;

import com.example.hello.entity.User;
import com.example.hello.util.HelloProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @Autowired
    private HelloProperties helloProperties;
    @RequestMapping("/hello")
    public String index() {
        System.out.println(helloProperties.getTitle());
        return  "Hello World, " +  helloProperties.getTitle();
    }

    @RequestMapping("/getUser")
    public User getUser() {
        User user=new User();
        user.setUserName("小明");
        user.setPassWord("xxxx");
        return user;
    }
}
