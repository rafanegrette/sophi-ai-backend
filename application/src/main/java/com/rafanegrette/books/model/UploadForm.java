package com.rafanegrette.books.model;

public record UploadForm(String file,
                         String bookLabel,
                         String paragraphSeparator,
                         String bookMarkType,
                         Integer firstPageOffset,
                         boolean fixTitleHP1,
                         boolean extraFormat,
                         Integer paragraphThreshold) {
}
