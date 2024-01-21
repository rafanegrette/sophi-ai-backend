package com.rafanegrette.books.services;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.port.out.FindBookUserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FindBookUserStateService {

    private final FindBookUserStateRepository findBookUserStateRepository;
    private final UserSecurityService userSecurityService;

    public BookWriteState getState(String bookId) {
        var userId = userSecurityService.getUser().email();

        return findBookUserStateRepository.getState(userId,bookId);
    }
}
