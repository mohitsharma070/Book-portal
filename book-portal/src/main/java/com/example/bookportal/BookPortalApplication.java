package com.example.bookportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class BookPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookPortalApplication.class, args);
	}

}
