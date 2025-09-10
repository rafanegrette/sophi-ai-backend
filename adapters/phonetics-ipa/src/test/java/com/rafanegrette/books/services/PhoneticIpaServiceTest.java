package com.rafanegrette.books.services;

import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.services.model.InputText;
import com.rafanegrette.books.services.model.TranslatedText;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhoneticIpaServiceTest {

    @Mock
    PhoneticApiClient phoneticApiClient;

    @InjectMocks
    PhoneticIpaService phoneticIpaService;

    @Test
    void getPhoneticBook() {
        // given
        var book = BookMother.harryPotter1().build();
        var sentenceNo = book.chapters()
                .stream()
                .flatMap(c -> c.pages().stream())
                .flatMap(page -> page.paragraphs().stream())
                .mapToInt(paragraph -> paragraph.sentences().size())
                .sum();

        given(phoneticApiClient.transform(any(InputText.class))).willReturn(new TranslatedText("original", "transformed"));

        // when
        var phoneticBook = phoneticIpaService.getPhoneticBook(book);
        // then

        assertNotNull(phoneticBook);
        verify(phoneticApiClient, times(sentenceNo)).transform(any(InputText.class));
    }
}