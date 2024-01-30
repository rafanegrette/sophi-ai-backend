package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.port.out.BookUserStateRepository;
import com.rafanegrette.books.port.out.SaveBookUserStateRepository;
import com.rafanegrette.books.repositories.entities.BookWriteStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookWriteStateDyna;
import com.rafanegrette.books.repositories.mappers.BookWriteStateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BookUserStateDynamo implements SaveBookUserStateRepository, BookUserStateRepository {


    private final DynamoDbTable<UserBookWriteStateDyna> bookUserStateTable;

    @Override
    public void save(String userEmail, BookWriteState bookWriteState) {


        var userWriteState = getUserWriteState(userEmail);
        var bookWriteStateDyna = BookWriteStateMapper.INSTANCE.map(bookWriteState);
        addBookWriteStateDyna(userWriteState, bookWriteStateDyna);
        bookUserStateTable.updateItem(r -> r.item(userWriteState));
    }

    @Override
    public BookWriteState getState(String userId, String bookId) {

        var userWriteState = getUserWriteState(userId);

        return userWriteState.getBookWriteStateDynas()
                .stream()
                .filter(s -> s.getBookId().equals(bookId))
                .map(BookWriteStateMapper.INSTANCE::map)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void saveState(String userId, BookWriteState bookWriteState) {
        var userWriteState = getUserWriteState(userId);
        var bookWriteStateDyna = BookWriteStateMapper.INSTANCE.map(bookWriteState);
        List<BookWriteStateDyna> newUserBookList = new ArrayList<>();
        userWriteState.getBookWriteStateDynas()
                .forEach(rb -> {
                    if (rb.getBookId().equals(bookWriteState.bookId())) {
                        newUserBookList.add(bookWriteStateDyna);
                    } else {
                        newUserBookList.add(rb);
                    }
                });

        UserBookWriteStateDyna userBookWriteStateDyna = new UserBookWriteStateDyna(userId, newUserBookList);
        bookUserStateTable.updateItem(r -> r.item(userBookWriteStateDyna));
    }

    private UserBookWriteStateDyna getUserWriteState(String userEmail) {
        Key key = Key.builder()
                .partitionValue(userEmail)
                .build();
        var userWriteState = bookUserStateTable.getItem(r -> r.key(key));

        if (userWriteState == null) {
            return new UserBookWriteStateDyna(userEmail, new ArrayList<>());
        }
        return userWriteState;
    }

    private void addBookWriteStateDyna(UserBookWriteStateDyna userBookWriteStateDyna, BookWriteStateDyna bookWriteStateDyna) {
        if (userBookWriteStateDyna.getBookWriteStateDynas() == null) {
            userBookWriteStateDyna.setBookWriteStateDynas(new ArrayList<>());
        }
        userBookWriteStateDyna.getBookWriteStateDynas().add(bookWriteStateDyna);
    }


}
