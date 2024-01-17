package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.port.out.UserRepository;
import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.UserDyna;
import com.rafanegrette.books.repositories.mappers.UserMapper;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@AllArgsConstructor
public class UserRepositoryDynamo implements UserRepository {

    private final DynamoDbTable<UserDyna> bookTable;

    @Override
    public void save(User user) {
        var userDyna = UserMapper.INSTANCE.userToUserDyna(user);
        bookTable.putItem(userDyna);
    }
}
