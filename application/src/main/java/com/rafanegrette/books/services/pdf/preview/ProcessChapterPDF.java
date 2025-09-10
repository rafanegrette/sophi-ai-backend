package com.rafanegrette.books.services.pdf.preview;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.services.NotContentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.string.formats.StringFormatFunctions;

@Slf4j
@Service
public class ProcessChapterPDF {

    private final ProcessContentPagePDF processContentPage;

    public ProcessChapterPDF(ProcessContentPagePDF processContentPage) {
        this.processContentPage = processContentPage;
    }

    private StringWriter getContentFromChapter(PDDocument document, ContentIndex contentIndex) throws IOException {
        StringWriter out;
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(contentIndex.pageStart());
        pdfStripper.setEndPage(contentIndex.pageEnd());
        out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        pdfStripper.writeText(document, writer);
        return out;
    }

    protected Chapter getChapter(PDDocument document, ContentIndex contentIndex, FormParameter formParameter) throws IOException {

        List<Page> pages = getPages(document, contentIndex, formParameter);
        String title;

        try {
            StringWriter contentWriter = getContentFromChapter(document, contentIndex);
            //
            switch (formParameter.bookMarkType()) {

            case BOOKMARK:
                title = contentIndex.title();
                break;
            default:
                title = getChapterTitle(StringFormatFunctions.formatTitles(contentWriter.toString()));
            }
        } catch (NullPointerException e) {
            title = "Default Content";
        }

        return new Chapter(contentIndex.index(), title, pages);
    }

    private String getChapterTitle(String firstParagraph) {
        int indexOne = firstParagraph.indexOf("\n \n");
        int indexTwo = firstParagraph.indexOf("\n \n", indexOne + 1) == -1 ? firstParagraph.indexOf("\n ", indexOne + 1)
                : firstParagraph.indexOf("\n \n", indexOne + 1);
        try {
            return firstParagraph.substring(indexOne, indexTwo).trim();
        } catch (Exception e) {
            return e.getMessage() + "";
        }
    }

    List<Page> getPages(PDDocument document, ContentIndex contentIndex, FormParameter formParameter) throws IOException {
        List<Page> pages = new ArrayList<>();
        Integer firstNoPage;
        Integer lastNoPage;
        try {
            firstNoPage = contentIndex.pageStart();
            lastNoPage = contentIndex.pageEnd();
            if (firstNoPage == null || lastNoPage == null) {
                log.error("No page start/ends found");
                throw new NotContentException();
            }
        } catch (NullPointerException e) {
            firstNoPage = 0;
            lastNoPage = document.getNumberOfPages();
        }

        for (int i = firstNoPage, j = 1; i < lastNoPage; i++, j++) {
            Page page;
            if (i == firstNoPage && formParameter.fixTitleHP2())
            {
            	page = processContentPage.getContentPageFirstPage(document, i, formParameter.paragraphFormats());
            } else {
            	page = processContentPage.getContentPage(document, i, formParameter.paragraphFormats());
            }
            
            page = new Page(j, page.paragraphs());
            pages.add(page);
        }

        return pages;
    }

}
