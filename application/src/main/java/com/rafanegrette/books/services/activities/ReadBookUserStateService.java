package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.port.out.BookUserStateRepository;
import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.services.ReadBookService;
import com.rafanegrette.books.services.UserSecurityService;
import org.springframework.stereotype.Service;

@Service
public class ReadBookUserStateService extends BookUserStateService {

    public ReadBookUserStateService(ReadBookUserStateRepository readBookUserStateRepository,
                                    UserSecurityService userSecurityService,
                                    ReadBookService readBookService) {
        super(readBookUserStateRepository, userSecurityService, readBookService);
    }
}
