package com.rafanegrette.books.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.port.out.SaveBookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Qualifier("SaveBookCoordinatorService")
public class SaveBookCoordinatorService implements SaveBookService {

    @Qualifier("SaveBookDBService")
    SaveBookService saveBookDBService;
    @Qualifier("SaveBookAudioService")
    SaveBookService saveBookAudioService;

    @Override
    public void save(Book book) {
        
        saveBookDBService.save(book);
        saveBookAudioService.save(book);
    }
    
}
