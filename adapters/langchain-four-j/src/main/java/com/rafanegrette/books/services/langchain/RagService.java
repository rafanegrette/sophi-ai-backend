package com.rafanegrette.books.services.langchain;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.Sentence;


import com.rafanegrette.books.services.langchain.config.LangchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class RagService {

    private final LangchainService langchainService;


    public void embedDocument(Book book) {
        try {
            var vectorStorage = langchainService.getStorage(book.title());
            embedParagraphs(vectorStorage, book.chapters().stream().flatMap(chapter -> chapter.pages().stream().flatMap(page -> page.paragraphs().stream())).toList());
        } catch (ExecutionException | InterruptedException e) {
            log.error("Supper error in Rag embedDocument with book {}", book.title(), e);
        }

    }

    public String findText(String bookName, String text) {
        try {
            return langchainService.getStorage(bookName).search(text);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Supper error in Rag findText with book {}", bookName, e);
            return "ERROR";
        }

    }

    private void embedParagraphs(LangchainService.VectorStore vectorStorage, List<Paragraph> paragraphs) {
        IntStream.range(0, paragraphs.size())
                        .filter(i -> i % 2 == 0)
                                .mapToObj(i -> {
                                    var firstParagraph = paragraphs.get(i).sentences().stream()
                                            .map(Sentence::text)
                                            .reduce(" ", String::concat);
                                    var secondParagraph = i+ 1 < paragraphs.size() ? paragraphs.get(i + 1 ).sentences().stream()
                                            .map(Sentence::text)
                                            .reduce(" ", String::concat) : "";

                                    return firstParagraph + " " + secondParagraph;
                                })
                .forEach(vectorStorage::embedText);
    }
}
