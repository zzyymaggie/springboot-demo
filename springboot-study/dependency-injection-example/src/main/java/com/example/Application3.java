package com.example;

import com.example.issue.MyConfiguration2;
import com.example.util.MyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application3 implements ApplicationListener<ContextRefreshedEvent> {

	public static void main(String[] args) {
		SpringApplication.run(Application3.class, args);
	}

	@Autowired
	private MyConfiguration myConfiguration;

	@Autowired
	private MyConfiguration2 myConfiguration2;

	@GetMapping("/test")
	public String test() {
		return "test: " + myConfiguration.isLog();
	}


	@GetMapping("/test2")
	public String test2() {
		return "test2:" + myConfiguration2.isLog();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		System.out.println("myConfiguration.isLog:" + myConfiguration.isLog());
		System.out.println("myConfiguration2.isLog:" + myConfiguration2.isLog());
	}
}
