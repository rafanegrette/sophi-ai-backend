package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.model.PronunciationRequest;
import com.rafanegrette.books.model.Transcript;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PronunciationService {

    private final VoiceMatchingPhrases voiceMatchingPhrases;

    public Transcript evaluate(PronunciationRequest pronunciationRequest) {
        var resultSentence = voiceMatchingPhrases.process(pronunciationRequest.userAudio(), pronunciationRequest.originalSentence());
        if (containsMarks(resultSentence)) {
            return new Transcript(resultSentence, false);
        }
        return new Transcript(resultSentence, true);
    }

    private boolean containsMarks(String resultSentence) {
        return (resultSentence.contains("~~") || resultSentence.contains("<mark>"));
    }


}
