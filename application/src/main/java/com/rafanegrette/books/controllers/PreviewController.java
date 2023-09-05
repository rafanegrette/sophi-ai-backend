package com.rafanegrette.books.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.UploadForm;
import com.rafanegrette.books.services.PreviewBookService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("books")
public class PreviewController {

	private Logger LOGGER = LoggerFactory.getLogger(PreviewController.class);
	
	private PreviewBookService previewService;
	
	@Autowired
	public PreviewController(PreviewBookService previewService) {
		this.previewService = previewService;
	}
	
    @ApiOperation(value = "Preview the loaded file")
    @CrossOrigin
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Book previewPDF(@RequestBody UploadForm uploadForm) {
        try {
            LOGGER.info("upload PDF: {}", uploadForm.bookLabel());
            Book book = previewService.previewPDF(uploadForm);
            return book;
        } catch (IOException e) {
            e.printStackTrace();
        }
		return Book.EMPTY_BOOK;
    }
}
