package com.rafanegrette.books.repositories.entities.mother;

import java.util.List;

import com.rafanegrette.books.repositories.entities.PageDyna;
import com.rafanegrette.books.repositories.entities.ParagraphDyna;

public class PageMother {

    private static PageBuilder page1 = new PageBuilder();
    private static PageBuilder page2 = new PageBuilder();
    private static PageBuilder page3 = new PageBuilder();
    
    public static PageBuilder page1() {
        return page1.noPage(1).paragraphs(List.of(ParagraphMother.paragraph1().build(),
                        ParagraphMother.paragraph2().build()));
    }
    
    public static PageBuilder page2() {
        return page2.noPage(2).paragraphs(List.of(ParagraphMother.paragraph1().build(),
                        ParagraphMother.paragraph2().build()));
    }
    
    public static PageBuilder page3() {
        return page3.noPage(3).paragraphs(List.of(ParagraphMother.paragraph1().build(),
                        ParagraphMother.paragraph2().build()));
    }
    
    public static class PageBuilder {
        private Integer noPage;
        private List<ParagraphDyna> paragraphs;
        
        public PageBuilder noPage(Integer noPage) {
            this.noPage = noPage;
            return this;
        }
        
        public PageBuilder paragraphs(List<ParagraphDyna> paragraphs) {
            this.paragraphs = paragraphs;
            return this;
        }
        
        public PageDyna build() {
            return new PageDyna(this.noPage, this.paragraphs);
        }
    }
}
