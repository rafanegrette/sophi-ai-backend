package com.rafanegrette.books.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class BookPDFTest {

    static String[][] pages = {
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

    public static PDDocument getBookNestedBookmarks() {

        return getDocumentWithNestedBookMarks(pages);
    }

    public static PDDocument getBookMultiNested() {

        return generateWithNestedBookMarks(pages);
    }

    public static PDDocument getBookNestedBookmarksEmptyPages() {
        String[][] pages = { };
        PDDocument document = new PDDocument();
        getDocumentWithPages(pages, document);
        return document;
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

    private static List<PDPage> getDocumentWithPages(String[][] pagesContent, PDDocument document) {
        List<PDPage> pages = new ArrayList<>();

        for (int i = 0; i < pagesContent.length; i++) {
            PDPage page = new PDPage();
            document.addPage(page);

            setPageContent(document, page, pagesContent[i]);
            pages.add(page);
        }

        return pages;
    }
    private static PDDocument getDocumentWithNestedBookMarks(String[][] pagesContent) {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        List<PDPage> pages = getDocumentWithPages(pagesContent, document);

        PDPageXYZDestination dest1 = new PDPageXYZDestination();
        dest1.setPage(pages.get(0));

        PDPageXYZDestination dest2 = new PDPageXYZDestination();
        dest2.setPage(pages.get(2));

        PDPageXYZDestination dest3 = new PDPageXYZDestination();
        dest3.setPage(pages.get(2));

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

    private static PDDocument generateWithNestedBookMarks(String[][] pagesContent) {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        List<PDPage> pages = new ArrayList<>();

        for (int i = 0; i < pagesContent.length; i++) {
            PDPage page = new PDPage();
            document.addPage(page);

            setPageContent(document, page, pagesContent[i]);
            pages.add(page);
        }

        PDPageXYZDestination dest1 = new PDPageXYZDestination();
        dest1.setPage(pages.get(0));

        PDPageXYZDestination dest2 = new PDPageXYZDestination();
        dest2.setPage(pages.get(2));

        PDPageXYZDestination dest3 = new PDPageXYZDestination();
        dest3.setPage(pages.get(2));

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

        PDOutlineItem bookmark21 = new PDOutlineItem();
        bookmark21.setDestination(dest2);
        bookmark21.setTitle("Bookmark " + 21);
        bookmark2.addLast(bookmark21);

        PDOutlineItem bookmark22 = new PDOutlineItem();
        bookmark22.setDestination(dest2);
        bookmark22.setTitle("Bookmark " + 22);
        bookmark2.addLast(bookmark22);

        PDOutlineItem bookmark221 = new PDOutlineItem();
        bookmark221.setDestination(dest2);
        bookmark221.setTitle("Bookmark " + 221);
        bookmark22.addLast(bookmark221);

        PDOutlineItem bookmark23 = new PDOutlineItem();
        bookmark23.setDestination(dest2);
        bookmark23.setTitle("Bookmark " + 23);
        bookmark2.addLast(bookmark23);

        PDOutlineItem bookmark3 = new PDOutlineItem();
        bookmark3.setDestination(dest3);
        bookmark3.setTitle("Bookmark " + 3);
        bookmark.addLast(bookmark3);

        PDOutlineItem bookmark31 = new PDOutlineItem();
        bookmark31.setDestination(dest3);
        bookmark31.setTitle("Bookmark " + 31);
        bookmark3.addLast(bookmark31);

        outline.addLast(bookmark);

        return document;
    }

    private static void setPageContent(PDDocument document, PDPage page, String[] pagesContent) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
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
