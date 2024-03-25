package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.port.out.DeleteBookStateService;
import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.repositories.entities.BookStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookReadStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookWriteStateDyna;
import com.rafanegrette.books.repositories.mappers.BookStateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository("ReadBookUserStateDynamo")
public class ReadBookUserStateDynamo implements ReadBookUserStateRepository, DeleteBookStateService {


    private final DynamoDbTable<UserBookReadStateDyna> bookUserStateTable;

    @Override
    public void create(String userEmail, BookCurrentState bookCurrentState) {


        var userReadState = getUserReadState(userEmail);
        var bookStateDyna = BookStateMapper.INSTANCE.map(bookCurrentState);
        addBookReadStateDyna(userReadState, bookStateDyna);
        bookUserStateTable.updateItem(r -> r.item(userReadState));
    }

    @Override
    public BookCurrentState getState(String userId, String bookId) {

        var userWriteState = getUserReadState(userId);

        return userWriteState.getBookStateDynas()
                .stream()
                .filter(s -> s.getBookId().equals(bookId))
                .map(BookStateMapper.INSTANCE::map)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void saveState(String userId, BookCurrentState bookCurrentState) {
        var userReadState = getUserReadState(userId);
        var bookStateStateDyna = BookStateMapper.INSTANCE.map(bookCurrentState);
        List<BookStateDyna> newUserBookList = new ArrayList<>();
        userReadState.getBookStateDynas()
                .forEach(bsDyna -> {
                    if (bsDyna.getBookId().equals(bookCurrentState.bookId())) {
                        newUserBookList.add(bookStateStateDyna);
                    } else {
                        newUserBookList.add(bsDyna);
                    }
                });

        UserBookReadStateDyna userBookReadStateDyna = new UserBookReadStateDyna(userId, newUserBookList);
        bookUserStateTable.updateItem(r -> r.item(userBookReadStateDyna));
    }

    @Override
    public void delete(String bookId) {
        Key key = Key.builder()
                .partitionValue(bookId)
                .build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();
        var userReadState = bookUserStateTable.query(queryRequest);
        List<BookStateDyna> newUserBookList = new ArrayList<>();
        if (userReadState != null) {
            userReadState.items().forEach(user -> {
                user.getBookStateDynas()
                        .forEach(bsDyna -> {
                            if (!bsDyna.getBookId().equals(bookId)) {
                                newUserBookList.add(bsDyna);
                            }
                        });
                UserBookReadStateDyna userBookReadStateDyna = new UserBookReadStateDyna(user.getUserEmail(), newUserBookList);
                bookUserStateTable.updateItem(r -> r.item(userBookReadStateDyna));
            });
        }

    }

    private UserBookReadStateDyna getUserReadState(String userEmail) {
        Key key = Key.builder()
                .partitionValue(userEmail)
                .build();
        var userWriteState = bookUserStateTable.getItem(r -> r.key(key));

        if (userWriteState == null) {
            return new UserBookReadStateDyna(userEmail, new ArrayList<>());
        }
        return userWriteState;
    }

    private void addBookReadStateDyna(UserBookReadStateDyna userBookReadStateDyna, BookStateDyna bookStateDyna) {
        if (userBookReadStateDyna.getBookStateDynas() == null) {
            userBookReadStateDyna.setBookStateDynas(new ArrayList<>());
        }
        userBookReadStateDyna.getBookStateDynas().add(bookStateDyna);
    }

}
