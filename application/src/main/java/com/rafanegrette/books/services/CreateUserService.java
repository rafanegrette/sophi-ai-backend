package com.rafanegrette.books.services;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateUserService {

    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }
}
