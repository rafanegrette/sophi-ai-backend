package com.rafanegrette.books.services.langchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rafanegrette.books.services", "com.rafanegrette.books.services.langchain", "com.rafanegrette.books.repositories"})
public class SpringApp {


    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }
}
