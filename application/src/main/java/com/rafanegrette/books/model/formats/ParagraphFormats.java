package com.rafanegrette.books.model.formats;

import com.rafanegrette.books.model.Paragraph;

public record ParagraphFormats(ParagraphThreshold paragraphThreshold,
                               boolean applyExtraFormat,
                               ParagraphSeparator paragraphSeparator) {
}
