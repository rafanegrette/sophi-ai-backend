package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.port.out.PhoneticService;
import com.rafanegrette.books.services.model.InputText;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("PhoneticIpaService")
@AllArgsConstructor
public class PhoneticIpaService implements PhoneticService {

    private final PhoneticApiClient phoneticApiClient;

    @Override
    public Book getPhoneticBook(Book book) {
        return new Book(book.id(),
                book.title(),
                book.label(),
                book.contentTable(),
                transformChapters(book.chapters()));
    }

    private List<Chapter> transformChapters(List<Chapter> chapters) {
        List<Chapter> chaptersTransformed = new ArrayList<>();

        chapters.forEach(chapter -> {
            var chapterNew = new Chapter(chapter.id(), chapter.title(), transformPages(chapter.pages()));
            chaptersTransformed.add(chapterNew);
        });
        return chaptersTransformed;
    }

    private List<Page> transformPages(List<Page> pages) {
        List<Page> pagesTransformed = new ArrayList<>();

        pages.forEach(page -> {
            var pageNew = new Page(page.number(), transformParagraphs(page.paragraphs()));
            pagesTransformed.add(pageNew);
        });

        return pagesTransformed;
    }

    private List<Paragraph> transformParagraphs(List<Paragraph> paragraphs) {
        List<Paragraph> paragraphTransformed = new ArrayList<>();

        paragraphs.forEach(paragraph -> {
            var paragraphNew = new Paragraph(paragraph.id(), transformSentences(paragraph.sentences()));
            paragraphTransformed.add(paragraphNew);
        });

        return paragraphTransformed;
    }

    private List<Sentence> transformSentences(List<Sentence> sentences) {
        List<Sentence> sentencesNew = new ArrayList<>();
        sentences.forEach(sentence -> {
            var transformed = phoneticApiClient.transform(new InputText(sentence.text())).transformed();
            sentencesNew.add(new Sentence(sentence.id(), transformed));
        });
        return sentencesNew;
    }
}
