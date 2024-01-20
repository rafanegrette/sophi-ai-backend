package com.rafanegrette.books.repositories.mappers;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.repositories.entities.UserDyna;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {

    @Test
    void testUserToUserDyna() {
        var user = new User("fulano", "fulano@gmail.com");

        var userDynaResult = UserMapper.INSTANCE.userToUserDyna(user);

        assertNotNull(userDynaResult);
        assertEquals("fulano", userDynaResult.getName());
        assertEquals("fulano@gmail.com", userDynaResult.getEmail());
    }

    @Test
    void testUserDynaToUser() {
        var userDyna = new UserDyna("chavo", "chavo@neighborhood.com");

        var userReturned = UserMapper.INSTANCE.userDynaToUser(userDyna);

        assertNotNull(userReturned);
        assertEquals("chavo", userReturned.name());
        assertEquals("chavo@neighborhood.com", userReturned.email());
    }
}
