package com.rafanegrette.books.controllers;

import java.io.IOException;
import java.util.List;

import com.rafanegrette.books.model.PhoneticBook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.services.ReadBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final ReadBookService readBookService;

    @GetMapping("/{bookId}")
    public PhoneticBook getBook(@PathVariable("bookId") String bookId) throws IOException {
        return this.readBookService.getPhoneticBook(bookId).get();
    }
    
    @GetMapping("/{bookId}/chapters/{chapterId}")
    public Chapter getChapter(@PathVariable("bookId") String bookId,
                                @PathVariable("chapterId") int indexChapter) throws IOException {
        return this.readBookService.getChapter(bookId, indexChapter).get();
    }

    @GetMapping("/titles")
    public List<Title> getAllTitles() {
        return this.readBookService.getAllTitles();
    }
    
    @GetMapping("/{bookId}/chapters/{chapterId}/pages/{pageId}")
    public PageDTO getPage(@PathVariable("bookId") String bookId,
                        @PathVariable("chapterId") int indexChapter,
                        @PathVariable("pageId") int noPage) {
    	return this.readBookService.getPage(bookId, indexChapter, noPage);
    }
}
