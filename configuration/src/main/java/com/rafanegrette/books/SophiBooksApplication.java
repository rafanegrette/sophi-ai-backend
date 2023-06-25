package com.rafanegrette.books;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
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

	//@EventListener(ApplicationReadyEvent.class)
	/*public void firstExecution() {
		byte[] result = azureSpeechService.getBinaryFile("I'm tired of being used");
		Path path = Path.of("./result.wav");
		try {
			Files.deleteIfExists(path);
			Files.createFile(path);
			Files.write(path, result);
		} catch (IOException ex) {
			log.error("Exception saving file {}", ex.getMessage());
		}
		

	}*/

}
