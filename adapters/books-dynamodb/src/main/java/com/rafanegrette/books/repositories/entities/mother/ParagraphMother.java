package com.rafanegrette.books.repositories.entities.mother;

import java.util.List;

import com.rafanegrette.books.repositories.entities.ParagraphDyna;
import com.rafanegrette.books.repositories.entities.SentenceDyna;

public class ParagraphMother {
    
    private static ParagraphBuilder paragraph1 = new ParagraphBuilder();
    private static ParagraphBuilder paragraph2 = new ParagraphBuilder();
    private static ParagraphBuilder paragraph3 = new ParagraphBuilder();
    private static ParagraphBuilder paragraph4 = new ParagraphBuilder();
    
    public static ParagraphBuilder paragraph1() {
        return paragraph1.id(1).sentences(List.of(new SentenceDyna(0, "This is boring story")));
    }
    
    public static ParagraphBuilder paragraph2() {
        return paragraph2.id(2).sentences(List.of(new SentenceDyna(0, "This is another boring story")));
    }
    
    public static ParagraphBuilder paragraph3() {
        return paragraph3.id(3).sentences(List.of(new SentenceDyna(0, "This is the third boring story")));
    }
    
    public static ParagraphBuilder paragraph4() {
        return paragraph4.id(4).sentences(List.of(new SentenceDyna(0, "This is the fourth boring")));
    }

    public static class ParagraphBuilder {
        private Integer id;
        private List<SentenceDyna> sentences;

        public ParagraphBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public ParagraphBuilder sentences(List<SentenceDyna> sentences) {
            this.sentences = sentences;
            return this;
        }

        public ParagraphDyna build() {
            return new ParagraphDyna(this.id, this.sentences);
        }
    }
}
