package com.example;
import com.example.util.MyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试@Value注入的生命周期
 * { @Value }和{ @Autowired} 都是由AutowiredAnnotationBeanPostProcessor解析注入，发生在populateBean时期，
 * 所以实例化阶段无法获取到注入的值
 */
@RestController
@SpringBootApplication
public class Application3 {

	public static void main(String[] args) {
		SpringApplication.run(Application3.class, args);
	}

	@Autowired
	private MyConfiguration myConfiguration;

	@GetMapping("/test")
	public String test() {
		return "controller: " + myConfiguration.isLog();
	}
}
