package com.rafanegrette.books.services;

import com.rafanegrette.books.model.SentenceReference;
import com.rafanegrette.books.port.out.SaveAudioFileService;
import com.rafanegrette.books.port.out.TextToSpeechService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegenerateAudioServiceTest {

    @Mock
    TextToSpeechService textToSpeechService;

    @Mock
    SaveAudioFileService saveAudioFileService;

    @InjectMocks
    RegenerateAudioService regenerateAudioService;

    @Test
    void regenerate_should_Call_SpeechService() {
        // given
        var sentenceReference = new SentenceReference("book/chapterId/pageId/paragraphId/sentenceId", "Text to regenerate audio");

        // when
        regenerateAudioService.regenerate(sentenceReference);
        // then
        verify(textToSpeechService).speech(sentenceReference.text());
    }

    @Test
    void regenerate_should_Call_SaveFileService() {
        // given
        var sentenceReference = new SentenceReference("book/chapterId/pageId/paragraphId/sentenceId", "Text to regenerate audio");
        var audioBinary = sentenceReference.text().getBytes(StandardCharsets.UTF_8);
        given(textToSpeechService.speech(sentenceReference.text())).willReturn(audioBinary);

        // when
        regenerateAudioService.regenerate(sentenceReference);
        // then
        verify(saveAudioFileService).save(sentenceReference.idFull(), audioBinary);
    }
}