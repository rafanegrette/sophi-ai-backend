package com.rafanegrette.books.services.langchain;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.model.mother.ChapterMother;
import com.rafanegrette.books.model.mother.PageMother;
import com.rafanegrette.books.model.mother.ParagraphMother;
import com.rafanegrette.books.services.langchain.config.LangchainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RagServiceTest {

    @InjectMocks
    RagService ragService;

    @Mock
    LangchainService langchainService;
    @Mock
    LangchainService.VectorStore vectorStore;

    @Test
    void embedDocument_givenBookWith2Sentences_shouldFlattenSentences()  {

        // given
        Book potter = BookMother.harryPotter1()
                .chapters(List.of(ChapterMother.potterChapter1()
                    .pages(List.of(PageMother.page1().paragraphs(
                             List.of(ParagraphMother.paragraph1()
                                .sentences(List.of(new Sentence(0, "This is boring story"),
                                    new Sentence(1, "This is another boring story")))
                                     .build()))
                            .build()))
                        .build()))
                .build();

        try {
            given(langchainService.getStorage(potter.title())).willReturn(vectorStore);

            // when
            ragService.embedDocument(potter);

            // then

            verify(langchainService).getStorage(potter.title());
            verify(vectorStore, times(1)).embedText(anyString());
        } catch(ExecutionException | InterruptedException e) {
            fail("Should not throw exception");
        }

    }

    @Test
    void embedDocument_givenBookErrorBackend_shouldCatchError() throws ExecutionException, InterruptedException {

        // given

        Book potter = BookMother.harryPotter1().build();
        doThrow(ExecutionException.class).when(langchainService).getStorage(potter.title());

        // when
        ragService.embedDocument(potter);

        // then
        verify(vectorStore, never()).embedText(anyString());
    }
}