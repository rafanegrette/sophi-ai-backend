package com.rafanegrette.books.repositories.entities.mother;

import java.util.List;

import com.rafanegrette.books.repositories.entities.ParagraphDyna;
import com.rafanegrette.books.repositories.entities.SentenceDyna;

public class ParagraphDynaMother {
    private static ParagraphDynaBuilder paragraph1 = new ParagraphDynaBuilder();
    private static ParagraphDynaBuilder paragraph2 = new ParagraphDynaBuilder();
    private static ParagraphDynaBuilder paragraph3 = new ParagraphDynaBuilder();
    private static ParagraphDynaBuilder paragraph4 = new ParagraphDynaBuilder();
    
    public static ParagraphDynaBuilder paragraph1() {
        return paragraph1.id(1).sentences(List.of(new SentenceDyna(0, "This is boring story")));
    }
    
    public static ParagraphDynaBuilder paragraph2() {
        return paragraph2.id(2).sentences(List.of(new SentenceDyna(0, "This is another boring story")));
    }
    
    public static ParagraphDynaBuilder paragraph3() {
        return paragraph3.id(3).sentences(List.of(new SentenceDyna(0, "This is the third boring story")));
    }
    
    public static ParagraphDynaBuilder paragraph4() {
        return paragraph4.id(4).sentences(List.of(new SentenceDyna(0, "This is the fourth boring")));
    }

    public static class ParagraphDynaBuilder {
        private Integer id;
        private List<SentenceDyna> sentences;

        public ParagraphDynaBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public ParagraphDynaBuilder sentences(List<SentenceDyna> sentences) {
            this.sentences = sentences;
            return this;
        }

        public ParagraphDyna build() {
            return new ParagraphDyna(this.id, this.sentences);
        }
    }
}
