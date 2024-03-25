package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.repositories.entities.BookStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookReadStateDyna;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReadBookUserStateDynamoTest {

    @InjectMocks
    ReadBookUserStateDynamo readBookUserStateDynamo;

    @Mock
    DynamoDbTable<UserBookReadStateDyna> bookUserStateTable;

    @Test
    void saveUpdating() {
        // given
        var bookUserState = new BookCurrentState("4d5sdd4f5f", 0, 2, 3,0, false);
        var userEmail = "fulanio@gmail.com";
        var bookWriteStateDyna = new BookStateDyna("4d5sdd4f5f", 0, 2, 3,0, false);
        //var bookUserStateDyna = new UserBookWriteStateDyna(userEmail, List.of(bookWriteStateDyna));
        given(bookUserStateTable.getItem(any(Consumer.class))).willReturn(new UserBookReadStateDyna(userEmail, new ArrayList<>()));

        // when
        readBookUserStateDynamo.create(userEmail, bookUserState);

        // then
       verify(bookUserStateTable, times(1)).updateItem(any(Consumer.class));
    }

    @Test
    void saveCreatingNewRegistry() {
        // given
        var bookUserState = new BookCurrentState("4d5sdd4f5f", 0, 2, 3,0, false);
        var userEmail = "fulanio@gmail.com";
        var bookWriteStateDyna = new BookStateDyna("4d5sdd4f5f", 0, 2, 3,0, false);
        //var bookUserStateDyna = new UserBookWriteStateDyna(userEmail, List.of(bookWriteStateDyna));
        //given(bookUserStateTable.getItem(any(Consumer.class))).willReturn(new UserBookWriteStateDyna(userEmail, new ArrayList<>()));

        // when
        readBookUserStateDynamo.create(userEmail, bookUserState);

        // then
        verify(bookUserStateTable, times(1)).updateItem(any(Consumer.class));
    }

    @Test
    void getBookWriteState() {
        // given
        var userId = "fulano@gmail.com";
        var bookId = "dskljkhjfkhv7dsf";
        given(bookUserStateTable.getItem(any(Consumer.class)))
                .willReturn(new UserBookReadStateDyna(userId,
                        List.of(new BookStateDyna(bookId, 2, 1, 1,1, false))));

        // when||
        var bookWriteState = readBookUserStateDynamo.getState(userId, bookId);

        //then

        assertNotNull(bookWriteState);
        assertEquals(bookId, bookWriteState.bookId());
        assertEquals(2, bookWriteState.chapterId());
        assertEquals(1, bookWriteState.pageNo());
        assertEquals(1, bookWriteState.paragraphId());
        assertEquals(1, bookWriteState.sentenceId());
    }

    @Test
    void updateBookReadState() {
        var userId = "fulano@gmail.com";
        var bookId = "dskljkhjfkhv7dsf";
        BookCurrentState bookCurrentState = new BookCurrentState(bookId, 0, 0,1,1, false);
        readBookUserStateDynamo.saveState(userId, bookCurrentState);

        verify(bookUserStateTable).updateItem(any(Consumer.class));
    }


    // TODO the data structure should change, it should be {userEmail, bookDyna} not {userEmail, List<bookDyna>}
    @Test
    @Disabled
    void deleteBookReadState() {
        var userId = "fulano@gmail.com";
        var bookId = "dskljkhjfkhv7dsf";
        BookCurrentState bookCurrentState = new BookCurrentState(bookId, 0, 0,1,1, false);
        //given(bookUserStateTable.query(any())).willReturn()
        readBookUserStateDynamo.delete(bookId);

        verify(bookUserStateTable).deleteItem(any(Consumer.class));
    }
}