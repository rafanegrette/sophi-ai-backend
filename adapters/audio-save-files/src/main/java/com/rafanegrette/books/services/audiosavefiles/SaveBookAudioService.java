package com.rafanegrette.books.services.audiosavefiles;


import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.port.out.SaveBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SaveBookAudioService")
@RequiredArgsConstructor
public class SaveBookAudioService implements SaveBookService {

    @Autowired
    private final SpeechService speechService;

    @Autowired
    private final SaveAudioFileService saveAudioService;
    
    @Override
    public void save(Book book) {
        
        book.chapters().parallelStream().forEach(chapter ->
            chapter.pages().parallelStream().forEach(page ->
                page.paragraphs().forEach(paragraph ->
                    paragraph.sentences().forEach(sentence -> {
                        var audioParam = new AudioParams(book.id(), 
                                            chapter.id(), 
                                            page.number(), 
                                            paragraph.id(), 
                                            sentence.id());
                        process(audioParam,  sentence);
                    })
                )
            )
        );
    }

    private void process(AudioParams audioParams, Sentence sentence) {
        byte[] file =  speechService.getBinaryFile(sentence.text());
        saveAudioService.save(audioParams.getPath(), file);
    }
    
}
