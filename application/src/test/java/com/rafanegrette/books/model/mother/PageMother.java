package com.rafanegrette.books.model.mother;

import java.util.List;

import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Paragraph;

public class PageMother {

    private static final PageBuilder page1 = new PageBuilder();
    private static final PageBuilder pagePhonetic1 = new PageBuilder();
    private static final PageBuilder page2 = new PageBuilder();
    private static final PageBuilder page3 = new PageBuilder();
    
    public static PageBuilder page1() {
        return page1.noPage(1).paragraphs(List.of(ParagraphMother.paragraph1().build(),
                        ParagraphMother.paragraph2().build()));
    }

    public static PageBuilder pagePhonetic1() {
        return pagePhonetic1.noPage(1).paragraphs(List.of(ParagraphMother.paragraphPhonetic1().build(),
                ParagraphMother.paragraph2().build()));
    }

    public static PageBuilder page2() {
        return page2.noPage(2).paragraphs(List.of(ParagraphMother.paragraph1().build(),
                        ParagraphMother.paragraph2().build()));
    }
    
    public static PageBuilder page3() {
        return page3.noPage(3).paragraphs(List.of(ParagraphMother.paragraph1().build(),
                        ParagraphMother.paragraph2().build(),
                        ParagraphMother.paragraph3().build()
                ));
    }
    
    public static class PageBuilder {
        private Integer noPage;
        private List<Paragraph> paragraphs;
        
        public PageBuilder noPage(Integer noPage) {
            this.noPage = noPage;
            return this;
        }
        
        public PageBuilder paragraphs(List<Paragraph> paragraphs) {
            this.paragraphs = paragraphs;
            return this;
        }
        
        public Page build() {
            return new Page(this.noPage, this.paragraphs);
        }
    }
}
