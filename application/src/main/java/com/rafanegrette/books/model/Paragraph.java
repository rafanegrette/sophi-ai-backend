package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.List;

public record Paragraph(Integer id,
                        List<Sentence> sentences) 
						implements Serializable {

    public enum ParagraphSeparator {
        ONE_JUMP_WITH_SPACE(". \n"),
        TWO_JUMP("\n\n"),
        ONE_JUMP("[.|”]\n");
        private String separator;
        private ParagraphSeparator(String separator) {
            this.separator = separator;
        }
        
        public String getSeparator() {
            return separator;
        }
    }
}
