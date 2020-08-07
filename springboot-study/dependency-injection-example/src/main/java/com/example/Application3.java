package com.example;
import com.example.util.MyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
