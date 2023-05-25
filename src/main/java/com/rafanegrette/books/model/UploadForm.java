package com.rafanegrette.books.model;

import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;

import lombok.Builder;

public record UploadForm(String file, 
        String bookName,
        String paragraphSeparator,
        String bookMarkType,
        Integer firstPageOffset,
        boolean fixTitleHP1) {
}
