package com.rafanegrette.books.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class BookPDFTest {

    public static PDDocument getBookNestedBookmarks() {
        String[][] pages = {
                {
                        "Title Page 1",
                        "\\n",
                        "This is a sad test story about a little programmer. He didn't know how to speak very well",
                        "English, but he try and try.",
                        "\\n",
                        "Until one day all happens."
                },
                {
                        "Title page two",
                        "\\n",
                        "A great opportunity present itself in an unexpected form; start raining.",
                        "\\n",
                        "The end",
                },
                {
                        "Title page THREE",
                        "\\n",
                        "Things got even more difficult in page 3"
                },
                {
                        "Sad Page Four",
                        "Another good page"
                }
        };
        return getDocumentWithNestedBookMarks(pages);
    }

    public static PDDocument getBookNestedBookmarksEmptyPages() {
        String[][] pages = { };
        return getDocumentWithNestedBookMarks(pages);
    }

    public static PDDocument getDocumentWithoutBookMarks() {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        return document;
    }

    public static PDDocument getDocumentWith17BookMarks() {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i < 17; i++) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Page " + i);
            outline.addLast(bookmark);
        }
        return document;
    }
    private static PDDocument getDocumentWithNestedBookMarks(String[][] pagesContent) {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        PDPage page1 = new PDPage();
        document.addPage(page1);
        PDPage page2 = new PDPage();
        document.addPage(page2);
        PDPage page3 = new PDPage();
        document.addPage(page3);
        PDPage page4 = new PDPage();
        document.addPage(page4);

        PDPage[] pages = {page1, page2, page3, page4};

        for (int i = 0; i < pagesContent.length; i++) {
            setPageContent(document, pages[i], pagesContent[i]);
        }


        PDPageXYZDestination dest1 = new PDPageXYZDestination();
        dest1.setPage(page1);

        PDPageXYZDestination dest2 = new PDPageXYZDestination();
        dest2.setPage(page3);

        PDPageXYZDestination dest3 = new PDPageXYZDestination();
        dest3.setPage(page3);

        PDOutlineItem bookmark = new PDOutlineItem();
        bookmark.setDestination(dest1);
        bookmark.setTitle("BookMarkMain");

        PDOutlineItem bookmark1 = new PDOutlineItem();
        bookmark1.setDestination(dest1);
        bookmark1.setTitle("Bookmark " + 1);
        bookmark.addLast(bookmark1);

        PDOutlineItem bookmark2 = new PDOutlineItem();
        bookmark2.setDestination(dest2);
        bookmark2.setTitle("Bookmark " + 2);
        bookmark.addLast(bookmark2);

        PDOutlineItem bookmark3 = new PDOutlineItem();
        bookmark3.setDestination(dest3);
        bookmark3.setTitle("Bookmark " + 3);
        bookmark.addLast(bookmark3);

        outline.addLast(bookmark);

        return document;
    }

    private static void setPageContent(PDDocument document, PDPage page, String[] pagesContent) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();

            contentStream.newLineAtOffset(50, 700);

            for(String pageContent: pagesContent) {
                contentStream.newLineAtOffset(50, -30);
                contentStream.showText(pageContent);
            }

            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            fail("Getting document with paragraph");
        }

    }
}
