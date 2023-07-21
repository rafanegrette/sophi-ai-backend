package com.rafanegrette.books.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.services.ReadBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private final ReadBookService readBookService;

    @CrossOrigin
    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable("bookId") String bookId) throws IOException {
        return this.readBookService.getBook(bookId).get();
    }
    
    @CrossOrigin
    @GetMapping("/{bookId}/chapters/{chapterId}")
    public Chapter getChapter(@PathVariable("bookId") String bookId,
                                @PathVariable("chapterId") int indexChapter) throws IOException {
        return this.readBookService.getChapter(bookId, indexChapter).get();
    }

    @CrossOrigin
    @GetMapping("/titles")
    public List<Title> getAllTitles() {
        return this.readBookService.getAllTitles();
    }
    
    @CrossOrigin
    @GetMapping("/{bookId}/chapters/{chapterId}/pages/{pageId}")
    public PageDTO getPage(@PathVariable("bookId") String bookId,
                        @PathVariable("chapterId") int indexChapter,
                        @PathVariable("pageId") int noPage) {
    	return this.readBookService.getPage(bookId, indexChapter, noPage);
    }
}
