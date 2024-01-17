package com.rafanegrette.books.repositories.mappers;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.repositories.entities.UserDyna;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDyna userToUserDyna(User user);

    User userDynaToUser(UserDyna userDyna);
}
