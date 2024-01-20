package com.rafanegrette.books.port.out;

import com.rafanegrette.books.model.User;

public interface UserRepository {

    void save(User user);

    User findByEmail(String email);
}
