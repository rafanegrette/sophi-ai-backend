package com.rafanegrette.books.controllers;

import java.io.IOException;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.model.PronunciationRequest;
import com.rafanegrette.books.services.activities.PronunciationService;
import com.rafanegrette.books.services.activities.ReadBookUserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rafanegrette.books.model.Transcript;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pronunciation")
@RestController
public class PronunciationController {

	private final PronunciationService pronunciationService;
	private final ReadBookUserStateService readBookUserStateService;


	@PostMapping
	public ResponseEntity<Transcript> transcript(
								@RequestParam("file")
								MultipartFile file,
								@RequestParam("sentence") InputText inputText,
								@RequestParam("idBook") String idBook){
		
		if (file == null)
			return new ResponseEntity<>(new Transcript("Bad Error occurred", false), HttpStatus.BAD_REQUEST);

		log.info("File size : " + file.getSize());
		Transcript transcriptResponse;
		try {
			transcriptResponse = pronunciationService.evaluate(
					new PronunciationRequest(idBook, file.getBytes(), inputText.originText()));
		} catch (IllegalStateException | IOException e) {
			log.error(e.getMessage());
			transcriptResponse = new Transcript("Error happens", false);
		}
		
		return new ResponseEntity<>(transcriptResponse, HttpStatus.OK);
	}

	@PostMapping("/{bookId}/increaseState")
	public void increaseState(@PathVariable("bookId") String bookId) {
		readBookUserStateService.advanceState(bookId);
	}

	@GetMapping("/{bookId}")
	public BookCurrentState getState(@PathVariable("bookId") String bookId) {
		return readBookUserStateService.getState(bookId);
	}

	public record InputText(String originText){}
}
