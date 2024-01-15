package com.rafanegrette.books.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafanegrette.books.model.SentenceAudio;
import com.rafanegrette.books.port.out.SignedUrlsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("signed-urls")
public class AudioUrlsController {

	private final SignedUrlsService signedUrlsService;
	
	public AudioUrlsController (@Qualifier("SignedUrlsService")
			SignedUrlsService signedUrlsService) {
		this.signedUrlsService = signedUrlsService;
	}
	
	@GetMapping("/")
	public ResponseEntity<List<SentenceAudio>> getSignedUrls(@RequestParam("pagePath") String pagePath) {
		try {
			var sentencUrls = signedUrlsService.generateSignedUrls(pagePath);
			if (sentencUrls.isEmpty()) return new ResponseEntity<>(sentencUrls, HttpStatus.NO_CONTENT);
			return ResponseEntity.ok(sentencUrls);
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
