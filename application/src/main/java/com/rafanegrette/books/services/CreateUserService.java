package com.rafanegrette.books.services;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.port.out.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }
}
