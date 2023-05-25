package com.rafanegrette.books.services.audioprocess;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.model.mother.BookMother;

@ExtendWith(MockitoExtension.class)
public class SaveBookAudioServiceTest {
    
    @Mock
    AzureSpeechService azureSpeechService;

    @Mock
    SaveAudioFileService saveAudioService;

    @InjectMocks
    SaveBookAudioService saveBookAudioService;

    @Test
    void testSave() {
        // given
        var book = BookMother.harryPotter1().build();
        var byteFile = new byte[1];
        var pathFirstSentence = "Harry-1/0/1/1/0";
        var textSentence = book.chapters().get(0).pages().get(0).paragraphs().get(0).sentences().get(0).text();
        // when
        when(azureSpeechService.getBinaryFile(textSentence)).thenReturn(byteFile);
        saveBookAudioService.save(book);

        // then 
        verify(azureSpeechService, atLeastOnce()).getBinaryFile(textSentence);
        verify(saveAudioService, atLeastOnce()).save(pathFirstSentence, byteFile);
        
    }
}
