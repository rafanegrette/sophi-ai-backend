package com.rafanegrette.books.model;

import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;

public record FormParameter(
		String labelName,
        ParagraphSeparator paragraphSeparator,
        ChapterTitleType bookMarkType,
        FirstPageOffset firstPageOffset,
        boolean fixTitleHP1) {


}
