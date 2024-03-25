package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.port.out.SaveBookService;
import com.rafanegrette.books.port.out.WriteBookUserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("SaveBookWriteUserStateService")
public class SaveBookUserStateService implements SaveBookService  {

    private final ReadBookUserStateRepository readStateRepository;
    private final WriteBookUserStateRepository writeStateRepository;
    private final UserSecurityService userSecurityService;

    @Override
    public void save(Book book) {
        var bookWriteState = new BookCurrentState(book.id(),
                book.chapters().get(0).id(),
                book.chapters().get(0).pages().get(0).number(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).id(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).sentences().get(0).id(),
                false);
        var userEmail = userSecurityService.getUser().email();
        readStateRepository.create(userEmail, bookWriteState);
        writeStateRepository.create(userEmail, bookWriteState);
    }
}
