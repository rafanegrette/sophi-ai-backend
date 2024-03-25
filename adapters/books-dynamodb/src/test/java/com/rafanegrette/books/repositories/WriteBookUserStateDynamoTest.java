package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.repositories.entities.BookStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookWriteStateDyna;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WriteBookUserStateDynamoTest {

    @InjectMocks
    WriteBookUserStateDynamo writeBookUserStateDynamo;

    @Mock
    DynamoDbTable<UserBookWriteStateDyna> bookUserStateTable;

    @Test
    void saveUpdating() {
        // given
        var bookUserState = new BookCurrentState("4d5sdd4f5f", 0, 2, 3,0, false);
        var userEmail = "fulanio@gmail.com";
        var bookWriteStateDyna = new BookStateDyna("4d5sdd4f5f", 0, 2, 3,0, false);
        //var bookUserStateDyna = new UserBookWriteStateDyna(userEmail, List.of(bookWriteStateDyna));
        given(bookUserStateTable.getItem(any(Consumer.class))).willReturn(new UserBookWriteStateDyna(userEmail, new ArrayList<>()));

        // when
        writeBookUserStateDynamo.create(userEmail, bookUserState);

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
        writeBookUserStateDynamo.create(userEmail, bookUserState);

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
                        List.of(new BookStateDyna(bookId, 2, 1, 1,1, false))));

        // when||
        var bookWriteState = writeBookUserStateDynamo.getState(userId, bookId);

        //then

        assertNotNull(bookWriteState);
        assertEquals(bookId, bookWriteState.bookId());
        assertEquals(2, bookWriteState.chapterId());
        assertEquals(1, bookWriteState.pageNo());
        assertEquals(1, bookWriteState.paragraphId());
        assertEquals(1, bookWriteState.sentenceId());
    }

    @Test
    void saveBookWriteState() {
        var userId = "fulano@gmail.com";
        var bookId = "dskljkhjfkhv7dsf";
        BookCurrentState bookCurrentState = new BookCurrentState(bookId, 0, 0,1,1, false);
        writeBookUserStateDynamo.saveState(userId, bookCurrentState);

        verify(bookUserStateTable).updateItem(any(Consumer.class));
    }
}