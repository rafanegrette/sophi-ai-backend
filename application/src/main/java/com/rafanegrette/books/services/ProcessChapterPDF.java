package com.rafanegrette.books.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.FormParameter;
import com.rafanegrette.books.string.formats.StringFormatFunctions;

@Service
public class ProcessChapterPDF {

    private final ProcessContentPagePDF processContentPage;

    public ProcessChapterPDF(ProcessContentPagePDF processContentPage) {
        this.processContentPage = processContentPage;
    }

    private StringWriter getContentFromChapter(PDDocument document, PDOutlineItem item) throws IOException {
        StringWriter out;
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartBookmark(item);
        pdfStripper.setEndBookmark(item.getNextSibling());
        out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        pdfStripper.writeText(document, writer);
        return out;
    }

    protected Chapter getChapterByIndex(PDDocument document, int noChapter, FormParameter formParameter) throws IOException {

        List<Page> pages = getPages(document, noChapter, formParameter);
        String title;

        try {
            PDOutlineItem currentChapter = getCurrentChapter(document, noChapter);
            StringWriter contentWriter = getContentFromChapter(document, currentChapter);
            //
            switch (formParameter.bookMarkType()) {

            case BOOKMARK:
                title = getChapterTitle(document, noChapter);
                break;
            default:
                title = getChapterTitle(StringFormatFunctions.formatTitles(contentWriter.toString()));
            }
        } catch (NullPointerException e) {
            title = "Default Content";
        }

        return new Chapter(noChapter - 2, title, pages);
    }

    private String getChapterTitle(PDDocument document, int noChapter) {
        Iterator<PDOutlineItem> ite = document.getDocumentCatalog().getDocumentOutline().children().iterator();
        int currentInd = 0;
        String title = "";
        try {
            while (currentInd < noChapter) {
                title = ite.next().getTitle();
                currentInd++;
            }
        } catch (NoSuchElementException e) {
            title = "Title No Available";
        }

        return title;
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

    private PDOutlineItem getCurrentChapter(PDDocument document, int noChapter) {
        PDOutlineItem item = document.getDocumentCatalog().getDocumentOutline().getFirstChild();
        PDOutlineItem currentChapter = item;
        for (int i = 1; i < noChapter; i++) {
            currentChapter = currentChapter.getNextSibling();
        }
        return currentChapter;
    }

    List<Page> getPages(PDDocument document, int noChapter, FormParameter formParameter) throws IOException {
        List<Page> pages = new ArrayList<>();
        Integer firstNoPage;
        Integer lastNoPage;
        try {
            firstNoPage = findFirstPageInChapter(document, noChapter, formParameter.firstPageOffset());
            lastNoPage = findLastPageInChapter(document, noChapter);
        } catch (NullPointerException e) {
            firstNoPage = 0;
            lastNoPage = document.getNumberOfPages();
        }

        for (int i = firstNoPage; i < lastNoPage; i++) {
            Page page;
            if (i == firstNoPage && formParameter.fixTitleHP1())
            {
            	page = processContentPage.getContentPageFirstPage(document, i, formParameter.paragraphSeparator());
            } else {
            	page = processContentPage.getContentPage(document, i, formParameter.paragraphSeparator());
            }
            
            page = new Page(i - firstNoPage + 1, page.paragraphs());
            pages.add(page);
        }

        return pages;
    }

    private Integer findFirstPageInChapter(PDDocument document, int noChapter, FirstPageOffset firstPageOffset)
            throws IOException {
        PDOutlineItem currentChapter = getCurrentChapter(document, noChapter);
        PDPage firstPage = currentChapter.findDestinationPage(document);
        return document.getPages().indexOf(firstPage) + firstPageOffset.getValue();
    }

    private Integer findLastPageInChapter(PDDocument document, int noChapter) throws IOException {
        PDOutlineItem currentChapter = getCurrentChapter(document, noChapter);
        PDOutlineItem nextChapter = currentChapter.getNextSibling();
        Integer lastNoPage = 0;
        if (nextChapter != null) {
            PDPage lastPage = nextChapter.findDestinationPage(document);

            lastNoPage = document.getPages().indexOf(lastPage) + 1;
        } else {
            lastNoPage = document.getNumberOfPages() + 1;
        }
        return lastNoPage;
    }

}
