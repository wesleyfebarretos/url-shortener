package com.spring.app.urlshorter;

import org.springframework.boot.SpringApplication;

public class TestUrlshorterApplication {

	public static void main(String[] args) {
		SpringApplication.from(UrlshorterApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
