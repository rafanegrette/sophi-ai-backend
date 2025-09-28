package com.rafanegrette.books.services.audiosavefiles;

import com.rafanegrette.books.model.mother.BookMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveBookAudioServiceTest {
    
    @Mock
    SpeechService speechService;

    @Mock
    SaveAudioFileS3Service saveAudioService;

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
        when(speechService.getBinaryFile(textSentence)).thenReturn(byteFile);
        saveBookAudioService.save(book);

        // then 
        verify(speechService, atLeastOnce()).getBinaryFile(textSentence);
        verify(saveAudioService, atLeastOnce()).save(pathFirstSentence, byteFile);
        
    }
}
