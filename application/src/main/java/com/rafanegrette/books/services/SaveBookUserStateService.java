package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.port.out.SaveBookService;
import com.rafanegrette.books.port.out.SaveBookUserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("SaveBookWriteUserStateService")
public class SaveBookUserStateService implements SaveBookService  {

    private final SaveBookUserStateRepository stateRepository;
    private final UserSecurityService userSecurityService;

    @Override
    public void save(Book book) {
        var bookWriteState = new BookWriteState(book.id(),
                book.chapters().get(0).id(),
                book.chapters().get(0).pages().get(0).number(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).id(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).sentences().get(0).id());
        var userEmail = userSecurityService.getUser().email();
        stateRepository.save(userEmail, bookWriteState);
    }
}
