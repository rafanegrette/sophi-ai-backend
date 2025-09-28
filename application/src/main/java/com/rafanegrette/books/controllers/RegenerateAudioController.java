package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.SentenceReference;
import com.rafanegrette.books.services.RegenerateAudioService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("regenerate-audio")
public class RegenerateAudioController {

    private final RegenerateAudioService regenerateAudioService;

    @PostMapping
    public ResponseEntity<Void> regenerateAudio(@RequestBody SentenceReference sentenceReference) {
        regenerateAudioService.regenerate(sentenceReference);
        return ResponseEntity.ok().build();
    }

}
