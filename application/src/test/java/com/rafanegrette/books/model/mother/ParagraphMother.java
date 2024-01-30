package com.rafanegrette.books.model.mother;

import java.util.List;

import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.Sentence;

public class ParagraphMother {
    
    private static ParagraphBuilder paragraph1 = new ParagraphBuilder();
    private static ParagraphBuilder paragraph2 = new ParagraphBuilder();
    private static ParagraphBuilder paragraph3 = new ParagraphBuilder();
    private static ParagraphBuilder paragraph4 = new ParagraphBuilder();
    
    public static ParagraphBuilder paragraph1() {
        return paragraph1.id(1).sentences(List.of(new Sentence(0, "This is boring story")));
    }
    
    public static ParagraphBuilder paragraph2() {
        return paragraph2.id(2).sentences(List.of(new Sentence(0, "This is another boring story")));
    }

    public static ParagraphBuilder paragraph3() {
        return paragraph3.id(3).sentences(List.of(new Sentence(0, "This is the third boring story"),
                new Sentence(1, "This is the next sentence")));
    }
    
    public static ParagraphBuilder paragraph4() {
        return paragraph4.id(4).sentences(List.of(new Sentence(0, "This is the fourth boring")));
    }

    public static class ParagraphBuilder {
        private Integer id;
        private List<Sentence> sentences;

        public ParagraphBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public ParagraphBuilder sentences(List<Sentence> sentences) {
            this.sentences = sentences;
            return this;
        }

        public Paragraph build() {
            return new Paragraph(this.id, this.sentences);
        }
    }
}
