package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.port.out.WriteBookUserStateRepository;
import com.rafanegrette.books.services.ReadBookService;
import com.rafanegrette.books.services.UserSecurityService;
import org.springframework.stereotype.Service;

@Service
public class WriteBookUserStateService extends BookUserStateService {

    public WriteBookUserStateService(WriteBookUserStateRepository writeBookUserStateRepository,
                                     UserSecurityService userSecurityService,
                                     ReadBookService readBookService) {
        super(writeBookUserStateRepository, userSecurityService, readBookService);
    }
}
