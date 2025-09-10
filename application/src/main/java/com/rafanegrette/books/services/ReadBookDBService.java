package com.rafanegrette.books.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.PhoneticBook;
import com.rafanegrette.books.model.PhoneticChapter;
import com.rafanegrette.books.model.PhoneticPage;
import com.rafanegrette.books.model.PhoneticParagraph;
import com.rafanegrette.books.model.PhoneticSentence;
import com.rafanegrette.books.model.Sentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.controllers.PageDTO;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.BookNotFoundException;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.port.out.BookRepository;

@Service
public class ReadBookDBService implements ReadBookService {
    
    private final Logger LOGGER = LoggerFactory.getLogger(ReadBookDBService.class);
    private final BookRepository bookRepository;
    private final BookRepository phoneticBookRepository;
    
    public ReadBookDBService(@Qualifier("BookDynamoService") BookRepository bookRepository,
                             @Qualifier("BookPhoneticDynamoService") BookRepository phoneticBookRepository) {
        this.bookRepository = bookRepository;
        this.phoneticBookRepository = phoneticBookRepository;
    }

    @Override
    public Optional<PhoneticBook> getPhoneticBook(String name) {
        var book = bookRepository.findById(name).orElse(Book.EMPTY_BOOK);
        var phonetic = phoneticBookRepository.findById(name).orElse(Book.EMPTY_BOOK);


        var phoneticBook = new PhoneticBook(book.id(),
                book.title(),
                book.label(),
                book.contentTable(),
                phoneticChapters(book.chapters(), phonetic.chapters()));
        return Optional.of(phoneticBook);
    }

    public Optional<Book> getBook(String bookName) {
        return bookRepository.findById(bookName);
    }
    
    public Optional<Chapter> getChapter(String bookName, int indexChapter) {
        Optional<Book> book = bookRepository.findById(bookName);
        Chapter chapter = null;
        if (book.isPresent()) {
            chapter = book.get().chapters().get(indexChapter);
        }
        return Optional.ofNullable(chapter);
    }

    @Override
    public List<Title> getAllTitles() {
        return bookRepository.findTitlesBy();
    }

    @Override
    public PageDTO getPage(String bookName, int indexChapter, int noPage) {
        Optional<Book> bookOpt = bookRepository.findById(bookName);
        Book book = bookOpt.orElseThrow(BookNotFoundException::new);
        Page page;
        int totalPages = -1;
        try {
            page = book.chapters().get(indexChapter).pages().get(noPage);
            totalPages = book.chapters().get(indexChapter).pages().size();
            LOGGER.info("Page No. : " + page.number());
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.error("Get Page of : " + bookName, ex);
            page = Page.EMPTY_PAGE;
        }
        return new PageDTO(page, totalPages);
    }

    private List<PhoneticChapter> phoneticChapters(List<Chapter> chapters, List<Chapter> phoneticChapters) {
        List<PhoneticChapter> chaptersTransformed = new ArrayList<>();

        for (int i = 0; i < chapters.size(); i++) {
            var chapter = chapters.get(i);
            var chapterNew = new PhoneticChapter(chapter.id(), chapter.title(), phoneticPages(chapter.pages(), phoneticChapters.get(i).pages()));

            chaptersTransformed.add(chapterNew);
        }
        return chaptersTransformed;
    }

    private List<PhoneticPage> phoneticPages(List<Page> pages, List<Page> phoneticPages) {
        List<PhoneticPage> pagesTransformed = new ArrayList<>();

        for (int i = 0; i < pages.size(); i++) {
            var page = pages.get(i);
            var pageNew = new PhoneticPage(page.number(), phoneticParagraphs(page.paragraphs(), phoneticPages.get(i).paragraphs()));
            pagesTransformed.add(pageNew);
        }

        return pagesTransformed;
    }

    private List<PhoneticParagraph> phoneticParagraphs(List<Paragraph> paragraphs, List<Paragraph> phoneticParagraphs) {
        List<PhoneticParagraph> paragraphTransformed = new ArrayList<>();

        for (int i = 0; i < paragraphs.size(); i++) {
            var paragraph = paragraphs.get(i);
            var paragraphNew = new PhoneticParagraph(paragraph.id(), phoneticSentences(paragraph.sentences(), phoneticParagraphs.get(i).sentences()));
            paragraphTransformed.add(paragraphNew);
        }

        return paragraphTransformed;
    }

    private List<PhoneticSentence> phoneticSentences(List<Sentence> sentences, List<Sentence> phoneticSentences) {
        List<PhoneticSentence> sentencesNew = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++) {
            var sentence = sentences.get(i);
            sentencesNew.add(new PhoneticSentence(sentence.id(), sentence.text(), phoneticSentences.get(i).text()));
        }
        return sentencesNew;
    }

}
