package com.rafanegrette.books.controllers;

import java.io.IOException;

import com.rafanegrette.books.services.VoiceMatchingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rafanegrette.books.model.Transcript;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TranscriptController {

	private final VoiceMatchingService voiceMatchingService;
	
	public TranscriptController(VoiceMatchingService voiceMatchingService) {
		this.voiceMatchingService = voiceMatchingService;
	}
	
	@CrossOrigin
	@PostMapping(value = "/transcript")
	public ResponseEntity<Transcript> transcript(
								@RequestParam("file")
								MultipartFile file,
								@RequestParam("sentence") InputText inputText){
		
		if (file == null)
			return new ResponseEntity<>(new Transcript("Bad Error occurred"), HttpStatus.BAD_REQUEST);

		log.info("File size : " + file.getSize());
		var transcriptResponse = "";
		try {
			transcriptResponse = voiceMatchingService.process(file.getBytes(), inputText.originText());
			
		} catch (IllegalStateException | IOException e) {
			log.error(e.getMessage());
			transcriptResponse = "Error happens";
		}
		
		return new ResponseEntity<>(new Transcript(transcriptResponse), HttpStatus.OK);
	}

	public record InputText(String originText){}
}
