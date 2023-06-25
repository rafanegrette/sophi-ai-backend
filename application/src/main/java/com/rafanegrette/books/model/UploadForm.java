package com.rafanegrette.books.model;

public record UploadForm(String file, 
        String bookName,
        String paragraphSeparator,
        String bookMarkType,
        Integer firstPageOffset,
        boolean fixTitleHP1) {
}
