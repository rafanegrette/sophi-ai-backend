package com.rafanegrette.books.services.audioprocess;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.services.SaveBookService;

import lombok.RequiredArgsConstructor;

@Service
@Qualifier("SaveBookAudioService")
@RequiredArgsConstructor
public class SaveBookAudioService implements SaveBookService {

    @Autowired
    private final AzureSpeechService azureSpeechService;

    @Autowired
    private final SaveAudioFileService saveAudioService;
    
    @Override
    public void save(Book book) {
        
        book.chapters().forEach(chapter -> {
            chapter.pages().forEach(page -> {
                page.paragraphs().forEach(paragraph -> {
                    paragraph.sentences().forEach(sentence -> {
                        var audioParam = new AudioParams(book.id(), 
                                            chapter.id(), 
                                            page.number(), 
                                            paragraph.id(), 
                                            sentence.id());
                        process(audioParam,  sentence);
                    });
                });
            });
        });
    }

    private void process(AudioParams audioParams, Sentence sentence) {
        byte[] file =  azureSpeechService.getBinaryFile(sentence.text());
        saveAudioService.save(audioParams.getPath(), file);
    }
    
}
