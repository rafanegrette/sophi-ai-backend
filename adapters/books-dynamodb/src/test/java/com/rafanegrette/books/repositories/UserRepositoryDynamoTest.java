package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.repositories.entities.UserDyna;
import com.rafanegrette.books.repositories.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryDynamoTest {

    @Mock
    DynamoDbTable<UserDyna> userTable;

    @InjectMocks
    UserRepositoryDynamo service;

    @Test
    void testSave() {
        // given
        var user = new User("fulano", "fulano@gmail.com");
        service.save(user);

        verify(userTable, times(1)).putItem(any(com.rafanegrette.books.repositories.entities.UserDyna.class));

    }
}
