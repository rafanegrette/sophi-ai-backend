package com.rafanegrette.books.services.audioprocess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SaveAudioFileService {

	
    @Value("{app.file.path}")
    private String rootFilePath;

    public void save(String pathFile, byte[] file) {
		Path path = Path.of(rootFilePath + pathFile);
		try {
			Files.deleteIfExists(path);
			Files.createFile(path);
			Files.write(path, file);
		} catch (IOException ex) {
			log.error("Exception saving file {}", ex.getMessage());
		}
    }
    
}
