package com.content.content_stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContentStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentStreamApplication.class, args);
	}

}
