package com.rafanegrette.books.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public record Paragraph(@Id Integer id,
                        @Field List<Sentence> sentences) 
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
