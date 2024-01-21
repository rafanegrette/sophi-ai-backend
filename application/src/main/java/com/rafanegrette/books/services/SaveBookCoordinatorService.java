package com.rafanegrette.books.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.port.out.SaveBookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("SaveBookCoordinatorService")
//@RequiredArgsConstructor
public class SaveBookCoordinatorService implements SaveBookService {

    private final SaveBookService saveBookDBService;
    private final SaveBookService saveBookAudioService;
    private final SaveBookService saveBookWriteUserStateService;

    public SaveBookCoordinatorService(@Qualifier("SaveBookDBService")
                                      SaveBookService saveBookDBService,
                                      @Qualifier("SaveBookAudioService")
                                      SaveBookService saveBookAudioService,
                                      @Qualifier("SaveBookWriteUserStateService")
                                      SaveBookService saveBookWriteUserStateService) {
        this.saveBookAudioService = saveBookAudioService;
        this.saveBookDBService = saveBookDBService;
        this.saveBookWriteUserStateService = saveBookWriteUserStateService;

    }

    @Override
    public void save(Book book) {
        log.info("Entering coordinator save");

        var bookWithId = new Book(UUID.randomUUID().toString(),
                book.title(),
                book.label(),
                book.contentTable(),
                book.chapters());

        saveBookDBService.save(bookWithId);
        saveBookAudioService.save(bookWithId);
        saveBookWriteUserStateService.save(bookWithId);
        log.info("Saved book id: {}", bookWithId.id());
    }

}
