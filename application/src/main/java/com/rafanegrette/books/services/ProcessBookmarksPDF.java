package com.rafanegrette.books.services;

import com.rafanegrette.books.model.ContentIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
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
            fillContentIndexHierarchically(outlines, bookMark, index, bookPdf.getNumberOfPages());
        }
        return outlines;
    }

    private void fillContentIndexHierarchically(List<ContentIndex> outlines, PDOutlineItem bookMark, int index, int lastPageNo) throws IOException{

        ChapterId chapterId = new ChapterId();
        TreeMap<Integer, Integer> startPageChapters = getStartPageChapters(bookMark, lastPageNo);
        index = addOutline(outlines, bookMark, index, startPageChapters, chapterId);

        while (bookMark.hasChildren() || bookMark.getNextSibling() != null) {
            if (bookMark.hasChildren()) {

                index =  addOutline(outlines, bookMark.getFirstChild(), index, startPageChapters, chapterId);
                bookMark = bookMark.getFirstChild();
            }

            if (bookMark.getNextSibling() != null) {
                index =  addOutline(outlines, bookMark.getNextSibling(), index, startPageChapters, chapterId);
                bookMark = bookMark.getNextSibling();
            }
        }

        log.info(startPageChapters.toString());

    }

    private TreeMap<Integer, Integer> getStartPageChapters(PDOutlineItem bookMark, Integer lastPageNo) throws IOException {
        TreeMap<Integer, Integer> pageChapters = new TreeMap<>();

        var firstPageNo = getPageNumber(bookMark);
        pageChapters.put(firstPageNo, null);

        while (bookMark.hasChildren() || bookMark.getNextSibling() != null) {
            if (bookMark.hasChildren()) {
                bookMark = bookMark.getFirstChild();
                var pageNo = getPageNumber(bookMark);
                if (!pageChapters.isEmpty()) {
                    pageChapters.compute(pageChapters.lastKey(), (k, v) -> pageNo);
                }
                pageChapters.put(pageNo, null);
            }

            if (bookMark.getNextSibling() != null) {
                bookMark = bookMark.getNextSibling();
                var pageNo = getPageNumber(bookMark);
                if (!pageChapters.isEmpty()) {
                    pageChapters.compute(pageChapters.lastKey(), (k, v) -> pageNo);
                }
                 pageChapters.put(pageNo, null);
            }
        }
        var lastKey = pageChapters.lastKey();
        pageChapters.computeIfAbsent(lastKey, v -> lastPageNo);
        return pageChapters;
    }

    private static int addOutline(List<ContentIndex> outlines,
                                  PDOutlineItem bookMark,
                                  int index, TreeMap<Integer,
                                  Integer> stackPage,
                                  ChapterId chapterId) throws IOException {
        if (bookMark.getTitle() != null && !bookMark.getTitle().isBlank()) {
            var pageNumberStart = getPageNumber(bookMark);
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

    private static int getPageNumber(PDOutlineItem bookMark) throws IOException {
        return ((PDPageDestination) bookMark.getDestination()).retrievePageNumber() + 1;
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
