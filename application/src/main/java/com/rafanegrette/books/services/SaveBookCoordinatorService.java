package com.rafanegrette.books.services;

import java.util.UUID;

import com.rafanegrette.books.port.out.BookRepository;
import com.rafanegrette.books.port.out.PhoneticService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.port.out.SaveBookService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("SaveBookCoordinatorService")
//@RequiredArgsConstructor
public class SaveBookCoordinatorService implements SaveBookService {

    private final SaveBookService saveBookDBService;
    private final SaveBookService saveBookAudioService;
    private final SaveBookService saveBookUserStateService;
    private final BookRepository saveBookPhoneticService;
    private final PhoneticService phoneticService;

    public SaveBookCoordinatorService(@Qualifier("SaveBookDBService")
                                      SaveBookService saveBookDBService,
                                      @Qualifier("SaveBookAudioService")
                                      SaveBookService saveBookAudioService,
                                      @Qualifier("SaveBookUserStateService")
                                      SaveBookService saveBookUserStateService,
                                      @Qualifier("BookPhoneticDynamoService")
                                      BookRepository saveBookPhoneticService,
                                      @Qualifier("PhoneticIpaService")
                                      PhoneticService phoneticService) {
        this.saveBookAudioService = saveBookAudioService;
        this.saveBookDBService = saveBookDBService;
        this.saveBookUserStateService = saveBookUserStateService;
        this.saveBookPhoneticService = saveBookPhoneticService;
        this.phoneticService = phoneticService;
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
        saveBookUserStateService.save(bookWithId);

        saveBookPhoneticService.save(phoneticService.getPhoneticBook(bookWithId));
        log.info("Saved book id: {}", bookWithId.id());
    }

}
