package com.example.springcloud.producer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@RestController
public class HelloController {
	@Autowired
    private DiscoveryClient client;

	@Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/hello")
    public String index(@RequestParam String name) {
        ServiceInstance instance = client.getLocalServiceInstance();
        return "hello "+name+"，this is first message,port:" + instance.getPort();
    }

    @RequestMapping("/health")
    public String health() {
        return "I'm alive.";

    }
}