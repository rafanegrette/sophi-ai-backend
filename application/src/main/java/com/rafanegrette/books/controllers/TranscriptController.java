package com.rafanegrette.books.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rafanegrette.books.model.Transcript;
import com.rafanegrette.books.port.out.SpeechToTextService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TranscriptController {

	private final SpeechToTextService speechToTextService;
	
	public TranscriptController(@Qualifier("WhisperService") SpeechToTextService speechToTextService) {
		this.speechToTextService = speechToTextService;
	}
	
	@CrossOrigin
	@PostMapping(value = "/transcript")
	public ResponseEntity<Transcript> transcript(@RequestParam("file")
								MultipartFile file){
		
		if (file == null)
			return new ResponseEntity<>(new Transcript("Bad Error occurred"), HttpStatus.BAD_REQUEST);

		log.info("File size : " + file.getSize());
		var transcriptResponse = "";
		try {
			transcriptResponse = speechToTextService.wavToVec(file.getBytes());
			
		} catch (IllegalStateException | IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			transcriptResponse = "Error happens";
		}
		
		return new ResponseEntity<>(new Transcript(transcriptResponse), HttpStatus.OK);
	}
}
