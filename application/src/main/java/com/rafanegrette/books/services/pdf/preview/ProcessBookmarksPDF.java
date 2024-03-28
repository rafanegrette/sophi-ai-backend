package com.rafanegrette.books.services.pdf.preview;

import com.rafanegrette.books.model.ContentIndex;
import com.rafanegrette.books.services.NotContentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ProcessBookmarksPDF {

    public List<ContentIndex> getBookmarks(PDDocument bookPdf) throws IOException{
        List<ContentIndex> outlines = new ArrayList<>();
        Optional<PDDocumentOutline> documentOutline = Optional.ofNullable(bookPdf.getDocumentCatalog().getDocumentOutline());
        int index = 0;
        PDOutlineItem bookMark = documentOutline.orElse(new PDDocumentOutline()).getFirstChild();
        if (bookMark == null) {
            outlines.add(new ContentIndex(index, "content", 0, bookPdf.getNumberOfPages() - 1, 1));
        } else {
            fillContentIndexHierarchically(outlines, bookMark, index, bookPdf);
        }
        return outlines;
    }

    private void fillContentIndexHierarchically(List<ContentIndex> outlines, PDOutlineItem bookMark, int index, PDDocument bookPdf) throws IOException{

        ChapterId chapterId = new ChapterId();
        TreeMap<Integer, Integer> startPageChapters = getStartPageChapters(bookMark, bookPdf);

        Stack<PDOutlineItem> parentsOutlines = new Stack<>();

        do {

            index = addOutline(outlines, bookMark, index, startPageChapters, chapterId, bookPdf);

            while (bookMark.hasChildren()) {
                index =  addOutline(outlines, bookMark.getFirstChild(), index, startPageChapters, chapterId, bookPdf);
                parentsOutlines.add(bookMark);
                bookMark = bookMark.getFirstChild();
            }

            bookMark = bookMark.getNextSibling();

            while(bookMark == null && !parentsOutlines.isEmpty()) {
                bookMark = parentsOutlines.pop().getNextSibling();
            }
        } while (bookMark != null);

        log.info(startPageChapters.toString());

    }

    private TreeMap<Integer, Integer> getStartPageChapters(PDOutlineItem bookMark, PDDocument bookPdf) throws IOException {
        TreeMap<Integer, Integer> pageChapters = new TreeMap<>();

        var firstPageNo = getPageNumber(bookMark, bookPdf);
        pageChapters.put(firstPageNo, null);
        Stack<PDOutlineItem> parentsOutlines = new Stack<>();

        do {

            while (bookMark.hasChildren()) {
                parentsOutlines.add(bookMark);
                bookMark = bookMark.getFirstChild();
                var pageNo = getPageNumber(bookMark, bookPdf);
                addPageToPageChapter(pageChapters, pageNo);
            }

            bookMark = bookMark.getNextSibling();
            var pageNo = getPageNumber(bookMark, bookPdf);
            addPageToPageChapter(pageChapters, pageNo);

            while(bookMark == null && !parentsOutlines.isEmpty()) {
                bookMark = parentsOutlines.pop().getNextSibling();
                pageNo = getPageNumber(bookMark, bookPdf);
                addPageToPageChapter(pageChapters, pageNo);
            }

        } while (bookMark != null);

        var lastKey = pageChapters.lastKey();
        pageChapters.computeIfAbsent(lastKey, v -> bookPdf.getNumberOfPages());
        return pageChapters;
    }

    private static void addPageToPageChapter(TreeMap<Integer, Integer> pageChapters, int pageNo) {
        if (!pageChapters.isEmpty() && pageNo >= 0)  {
            pageChapters.compute(pageChapters.lastKey(), (k, v) -> pageNo);
        }
        if (pageNo >= 0) {
            pageChapters.put(pageNo, null);
        }
    }

    private static int addOutline(List<ContentIndex> outlines,
                                  PDOutlineItem bookMark,
                                  int index,
                                  TreeMap<Integer, Integer> stackPage,
                                  ChapterId chapterId,
                                  PDDocument bookPdf) throws IOException {
        if (bookMark.getTitle() != null && !bookMark.getTitle().isBlank()) {
            var pageNumberStart = getPageNumber(bookMark, bookPdf);
            Integer startPage = null, endPage = null;
            if (stackPage.containsKey(pageNumberStart)) {
                var pageIndex = stackPage.pollFirstEntry();
                startPage = pageIndex.getKey();
                endPage = pageIndex.getValue();
                chapterId.increaseChapter();
            }
            outlines.add(new ContentIndex(index,
                    bookMark.getTitle(),
                    startPage,
                    endPage,
                    chapterId.getChapterId()));
            index++;
        }
        return index;
    }

    private static int getPageNumber(PDOutlineItem bookMark, PDDocument bookPdf) throws IOException {
        if (bookMark == null) {
            return -1;
        }
        var destination = bookMark.getDestination();
        if (bookMark.getAction() instanceof PDActionGoTo destAction) {
            destination = destAction.getDestination();
        }

        if (destination instanceof PDPageDestination pd) {
            return pd.retrievePageNumber() + 1;
        } else if (destination instanceof PDNamedDestination pdn && bookPdf.getDocumentCatalog().findNamedDestinationPage(pdn) != null) {
            return bookPdf.getDocumentCatalog().findNamedDestinationPage(pdn).retrievePageNumber() + 1;
        }
        throw new NotContentException();
    }

    private static class ChapterId {
        Integer chapterId;

        ChapterId() {
            this.chapterId = 0;
        }

        void increaseChapter() {
            chapterId++;
        }

        Integer getChapterId() {
            return chapterId;
        }
    }
}
