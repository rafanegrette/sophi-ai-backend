package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.port.out.DeleteBookStateService;
import com.rafanegrette.books.port.out.WriteBookUserStateRepository;
import com.rafanegrette.books.repositories.entities.BookStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookWriteStateDyna;
import com.rafanegrette.books.repositories.mappers.BookStateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository("WriteBookUserStateDynamo")
public class WriteBookUserStateDynamo implements WriteBookUserStateRepository, DeleteBookStateService {


    private final DynamoDbTable<UserBookWriteStateDyna> bookUserStateTable;

    @Override
    public void create(String userEmail, BookCurrentState bookCurrentState) {


        var userWriteState = getUserWriteState(userEmail);
        var bookWriteStateDyna = BookStateMapper.INSTANCE.map(bookCurrentState);
        addBookWriteStateDyna(userWriteState, bookWriteStateDyna);
        bookUserStateTable.updateItem(r -> r.item(userWriteState));
    }

    @Override
    public BookCurrentState getState(String userId, String bookId) {

        var userWriteState = getUserWriteState(userId);

        return userWriteState.getBookStateDynas()
                .stream()
                .filter(s -> s.getBookId().equals(bookId))
                .map(BookStateMapper.INSTANCE::map)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void saveState(String userId, BookCurrentState bookCurrentState) {
        var userWriteState = getUserWriteState(userId);
        var bookWriteStateDyna = BookStateMapper.INSTANCE.map(bookCurrentState);
        List<BookStateDyna> newUserBookList = new ArrayList<>();
        userWriteState.getBookStateDynas()
                .forEach(rb -> {
                    if (rb.getBookId().equals(bookCurrentState.bookId())) {
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

    private void addBookWriteStateDyna(UserBookWriteStateDyna userBookWriteStateDyna, BookStateDyna bookStateDyna) {
        if (userBookWriteStateDyna.getBookStateDynas() == null) {
            userBookWriteStateDyna.setBookStateDynas(new ArrayList<>());
        }
        userBookWriteStateDyna.getBookStateDynas().add(bookStateDyna);
    }


    @Override
    public void delete(String bookId) {

    }
}
