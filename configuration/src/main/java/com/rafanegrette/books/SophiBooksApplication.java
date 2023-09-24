package com.rafanegrette.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.rafanegrette.books", "com.rafanegrette.books.controllers"})
public class SophiBooksApplication {


	public static void main(String[] args) {
		SpringApplication.run(SophiBooksApplication.class, args);
		
	}

}
