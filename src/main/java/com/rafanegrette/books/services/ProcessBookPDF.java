package com.rafanegrette.books.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.ChapterTitleType;
import com.rafanegrette.books.model.ContentIndex;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.FormParameter;
import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;
import com.rafanegrette.books.model.UploadForm;

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

    List<ContentIndex> getOutline(PDDocument document) {
        List<ContentIndex> outlines = new ArrayList<>();
        Optional<PDDocumentOutline> documentOutline = Optional.ofNullable(document.getDocumentCatalog().getDocumentOutline());
        int index = 0;
        Iterable<PDOutlineItem> bookMarks = documentOutline.orElse(new PDDocumentOutline()).children();
        
        Iterator<PDOutlineItem> bookMarkIterator = bookMarks.iterator();

        while(bookMarkIterator.hasNext()) {
        	PDOutlineItem currentMark = bookMarkIterator.next();
        	if(index == 1 && bookMarkIterator.hasNext()) {
            	outlines.set(index - 1, new ContentIndex(index - 1, currentMark.getTitle()));
            	outlines.add(new ContentIndex(index, bookMarkIterator.next().getTitle()));
            	index++;
        	} else {
            	outlines.add(new ContentIndex(index++, currentMark.getTitle()));
        	}
        }
        
        if (outlines.isEmpty()) {
            outlines.add(new ContentIndex(index, "content"));
        }

        return outlines;
    }

    private Book getBook(PDDocument document, FormParameter formParameter) throws IOException {
        List<ContentIndex> bookMarks = getOutline(document);
        String originalFitle = document.getDocumentInformation().getTitle();
        List<Chapter> chapters = new ArrayList<>();
        for (int i = 2; i <= bookMarks.size() + 1; i++) {
            Chapter chapter = processChapterPDF.getChapterByIndex(document, i, formParameter);
            chapters.add(chapter);
        }
        Book book = new Book(formParameter.title(), originalFitle, bookMarks, chapters);
        document.close();
        return book;
    }

}
