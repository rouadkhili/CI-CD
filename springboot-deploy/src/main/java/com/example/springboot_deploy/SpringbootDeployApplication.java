package com.example.springboot_deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDeployApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDeployApplication.class, args);
	}
	public String home(){
		return "welcomehome";
	}

}
