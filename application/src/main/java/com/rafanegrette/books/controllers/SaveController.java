package com.rafanegrette.books.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.port.out.SaveBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("books")
public class SaveController {

    private SaveBookService saveBookService;

    @Autowired
    public SaveController(@Qualifier("SaveBookCoordinatorService") SaveBookService saveBookService) {
        this.saveBookService = saveBookService;
    }

    @CrossOrigin
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(@RequestBody Book book) {
        try {
            saveBookService.save(book);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error("Controller error {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
