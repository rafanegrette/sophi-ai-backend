package com.rafanegrette.books.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

    protected Chapter getChapter(PDDocument document, ProcessBookPDF.BookMarkPage bookMarkPage, FormParameter formParameter) throws IOException {

        List<Page> pages = getPages(document, bookMarkPage.outlineItem(), formParameter);
        String title;

        try {
            StringWriter contentWriter = getContentFromChapter(document, bookMarkPage.outlineItem());
            //
            switch (formParameter.bookMarkType()) {

            case BOOKMARK:
                title = bookMarkPage.title();
                break;
            default:
                title = getChapterTitle(StringFormatFunctions.formatTitles(contentWriter.toString()));
            }
        } catch (NullPointerException e) {
            title = "Default Content";
        }

        return new Chapter(bookMarkPage.index(), title, pages);
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

    List<Page> getPages(PDDocument document, PDOutlineItem outlineItem, FormParameter formParameter) throws IOException {
        List<Page> pages = new ArrayList<>();
        Integer firstNoPage;
        Integer lastNoPage;
        try {
            firstNoPage = findFirstPageInChapter(document, outlineItem, formParameter.firstPageOffset());
            lastNoPage = findLastPageInChapter(document, outlineItem);
        } catch (NullPointerException e) {
            firstNoPage = 0;
            lastNoPage = document.getNumberOfPages();
        }

        for (int i = firstNoPage; i < lastNoPage; i++) {
            Page page;
            if (i == firstNoPage && formParameter.fixTitleHP2())
            {
            	page = processContentPage.getContentPageFirstPage(document, i, formParameter.paragraphFormats());
            } else {
            	page = processContentPage.getContentPage(document, i, formParameter.paragraphFormats());
            }
            
            page = new Page(i - firstNoPage + 1, page.paragraphs());
            pages.add(page);
        }

        return pages;
    }

    private Integer findFirstPageInChapter(PDDocument document, PDOutlineItem outlineItem, FirstPageOffset firstPageOffset)
            throws IOException {
        PDPage firstPage = outlineItem.findDestinationPage(document);
        return document.getPages().indexOf(firstPage) + firstPageOffset.getValue();
    }

    private Integer findLastPageInChapter(PDDocument document, PDOutlineItem outlineItem) throws IOException {
        PDOutlineItem nextChapter = nextOutlineItem(outlineItem);
        Integer lastNoPage = 0;
        if (nextChapter != null) {
            PDPage lastPage = nextChapter.findDestinationPage(document);

            lastNoPage = document.getPages().indexOf(lastPage) + 1;
        } else {
            lastNoPage = document.getNumberOfPages() + 1;
        }
        return lastNoPage;
    }

    private PDOutlineItem nextOutlineItem(PDOutlineItem outlineItem) {
        if (outlineItem.hasChildren()) {
            return outlineItem.getFirstChild();
        } else {
            return outlineItem.getNextSibling();
        }
    }

}
