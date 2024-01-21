package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.repositories.entities.BookWriteStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookWriteStateDyna;
import com.rafanegrette.books.repositories.entities.UserDyna;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookUserStateDynamoTest {

    @InjectMocks
    BookUserStateDynamo bookUserStateDynamo;

    @Mock
    DynamoDbTable<UserBookWriteStateDyna> bookUserStateTable;

    @Test
    void saveUpdating() {
        // given
        var bookUserState = new BookWriteState("4d5sdd4f5f", 0, 2, 3,0);
        var userEmail = "fulanio@gmail.com";
        var bookWriteStateDyna = new BookWriteStateDyna("4d5sdd4f5f", 0, 2, 3,0);
        //var bookUserStateDyna = new UserBookWriteStateDyna(userEmail, List.of(bookWriteStateDyna));
        given(bookUserStateTable.getItem(any(Consumer.class))).willReturn(new UserBookWriteStateDyna(userEmail, new ArrayList<>()));

        // when
        bookUserStateDynamo.save(userEmail, bookUserState);

        // then
       verify(bookUserStateTable, times(1)).updateItem(any(Consumer.class));
    }

    @Test
    void saveCreatingNewRegistry() {
        // given
        var bookUserState = new BookWriteState("4d5sdd4f5f", 0, 2, 3,0);
        var userEmail = "fulanio@gmail.com";
        var bookWriteStateDyna = new BookWriteStateDyna("4d5sdd4f5f", 0, 2, 3,0);
        //var bookUserStateDyna = new UserBookWriteStateDyna(userEmail, List.of(bookWriteStateDyna));
        //given(bookUserStateTable.getItem(any(Consumer.class))).willReturn(new UserBookWriteStateDyna(userEmail, new ArrayList<>()));

        // when
        bookUserStateDynamo.save(userEmail, bookUserState);

        // then
        verify(bookUserStateTable, times(1)).updateItem(any(Consumer.class));
    }

    @Test
    void getBookWriteState() {
        // given
        var userId = "fulano@gmail.com";
        var bookId = "dskljkhjfkhv7dsf";
        given(bookUserStateTable.getItem(any(Consumer.class)))
                .willReturn(new UserBookWriteStateDyna(userId,
                        List.of(new BookWriteStateDyna(bookId, 2, 1, 1,1))));

        // when
        var bookWriteState = bookUserStateDynamo.getState(userId, bookId);

        //then

        assertNotNull(bookWriteState);
        assertEquals(bookId, bookWriteState.bookId());
        assertEquals(2, bookWriteState.chapterId());
        assertEquals(1, bookWriteState.pageNo());
        assertEquals(1, bookWriteState.paragraphId());
        assertEquals(1, bookWriteState.sentenceId());
    }
}