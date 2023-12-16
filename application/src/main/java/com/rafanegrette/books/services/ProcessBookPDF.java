package com.rafanegrette.books.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.ContentIndex;
import com.rafanegrette.books.model.FormParameter;

@Slf4j
@Service
public class ProcessBookPDF implements LoadPDFService {

    private final ProcessChapterPDF processChapterPDF;

    public ProcessBookPDF(ProcessChapterPDF processChapterPDF) {
        this.processChapterPDF = processChapterPDF;
    }
    
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

    List<BookMarkPage> getOutline(PDDocument document) {
        List<BookMarkPage> outlines = new ArrayList<>();
        Optional<PDDocumentOutline> documentOutline = Optional.ofNullable(document.getDocumentCatalog().getDocumentOutline());
        int index = 0;
        PDOutlineItem bookMark = documentOutline.orElse(new PDDocumentOutline()).getFirstChild();

        if (bookMark == null) {
            outlines.add(new BookMarkPage(index, "content", new PDOutlineItem()));
        } else {
            fillContentIndexHierarchically(outlines, bookMark, index);
        }
        return outlines;
    }

    // TODO Convert to Iterative, java doesn't work well on tail recursion
    private void fillContentIndexHierarchically(List<BookMarkPage> outlines, PDOutlineItem bookMark, int index) {

        if (bookMark.getTitle() != null && !bookMark.getTitle().isBlank()) {
            outlines.add(new BookMarkPage(index, bookMark.getTitle(), bookMark));
            index++;
        }


        if (bookMark.hasChildren()) {
            fillContentIndexHierarchically(outlines, bookMark.getFirstChild(), index);
        }
        if (bookMark.getNextSibling() != null) {
            fillContentIndexHierarchically(outlines, bookMark.getNextSibling(), index);
        }

    }

    private Book getBook(PDDocument document, FormParameter formParameter) throws IOException {
        List<BookMarkPage> bookMarks = getOutline(document);
        String originalTitle = document.getDocumentInformation().getTitle();
        List<Chapter> chapters = new ArrayList<>();
        for (BookMarkPage bookMarkPage: bookMarks) {
            Chapter chapter = processChapterPDF.getChapter(document, bookMarkPage, formParameter);
            if (validTitle(chapter.title())) {
                chapters.add(chapter);
            }
        }
        Book book = new Book(formParameter.labelName(), 
        		originalTitle, 
        		formParameter.labelName(),
        		bookMarks.stream().map(BookMarkPage::contentIndex).toList(),
        		chapters);
        document.close();
        return book;
    }

    private boolean validTitle(String title) {
        return title != null && !title.isBlank();
    }
    record BookMarkPage(int index, String title, PDOutlineItem outlineItem){

        public ContentIndex contentIndex() {
            return new ContentIndex(this.index, this.title);
        }
    }
}
