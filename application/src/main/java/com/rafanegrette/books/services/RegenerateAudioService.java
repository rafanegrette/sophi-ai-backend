package com.rafanegrette.books.services;

import com.rafanegrette.books.model.SentenceReference;
import com.rafanegrette.books.port.out.SaveAudioFileService;
import com.rafanegrette.books.port.out.TextToSpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegenerateAudioService {

    private final TextToSpeechService textToSpeechService;
    private final SaveAudioFileService saveAudioFileService;

    public void regenerate(SentenceReference sentenceReference) {

        var audioBinary = textToSpeechService.speech(sentenceReference.text());
        saveAudioFileService.save(sentenceReference.idFull(), audioBinary);
    }
}
