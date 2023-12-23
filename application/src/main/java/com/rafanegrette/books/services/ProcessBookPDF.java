package com.rafanegrette.books.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.ContentIndex;
import com.rafanegrette.books.model.FormParameter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessBookPDF implements LoadPDFService {

    private final ProcessChapterPDF processChapterPDF;
    private final ProcessBookmarksPDF processBookmarksPDF;

    @Override
    public Book getBookFromByteFile(byte[] bookFile, FormParameter formParameter) throws IOException {
        Book book;
        try (PDDocument document = PDDocument.load(bookFile)) {
            book = getBook(document, formParameter);
        }
        return book;
    }

    public File getCoverImage(byte[] byteFile) throws IOException {
        return null;
    }

    private Book getBook(PDDocument document, FormParameter formParameter) throws IOException {
        List<ContentIndex> bookMarks = processBookmarksPDF.getBookmarks(document);
        String originalTitle = document.getDocumentInformation().getTitle();
        List<Chapter> chapters = new ArrayList<>();
        for (ContentIndex contentIndex: bookMarks.stream().filter(content -> content.pageStart() != null).toList()) {
            Chapter chapter = processChapterPDF.getChapter(document, contentIndex, formParameter);
            if (validTitle(chapter.title())) {
                chapters.add(chapter);
            }
        }
        Book book = new Book(formParameter.labelName(), 
        		originalTitle, 
        		formParameter.labelName(),
                bookMarks,
        		chapters);
        document.close();
        return book;
    }

    private boolean validTitle(String title) {
        return title != null && !title.isBlank();
    }
    record BookMarkPage(int index, String title, PDOutlineItem outlineItem){

    }
}
