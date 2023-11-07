package com.rafanegrette.books.model;

import com.rafanegrette.books.model.formats.ParagraphFormats;

public record FormParameter(
		String labelName,
        ParagraphFormats paragraphFormats,
        ChapterTitleType bookMarkType,
        FirstPageOffset firstPageOffset,
        boolean fixTitleHP2) {


}
