package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.port.out.UserRepository;
import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.UserDyna;
import com.rafanegrette.books.repositories.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@AllArgsConstructor
@Repository
public class UserRepositoryDynamo implements UserRepository {

    private final DynamoDbTable<UserDyna> userTable;

    @Override
    public void save(User user) {
        var userDyna = UserMapper.INSTANCE.userToUserDyna(user);
        userTable.putItem(userDyna);
    }

    @Override
    public User findByEmail(String email) {
        Key key = Key.builder().partitionValue(email).build();
        var userDyna = userTable.getItem(key);
        return UserMapper.INSTANCE.userDynaToUser(userDyna);
    }
}
