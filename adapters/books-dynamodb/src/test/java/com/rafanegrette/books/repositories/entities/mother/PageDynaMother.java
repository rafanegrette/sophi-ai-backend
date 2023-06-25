package com.rafanegrette.books.repositories.entities.mother;

import java.util.List;

import com.rafanegrette.books.repositories.entities.PageDyna;
import com.rafanegrette.books.repositories.entities.ParagraphDyna;

public class PageDynaMother {
    private static PageDynaBuilder page1 = new PageDynaBuilder();
    private static PageDynaBuilder page2 = new PageDynaBuilder();
    private static PageDynaBuilder page3 = new PageDynaBuilder();
    
    public static PageDynaBuilder page1() {
        return page1.noPage(1).paragraphs(List.of(ParagraphDynaMother.paragraph1().build(),
        				ParagraphDynaMother.paragraph2().build()));
    }
    
    public static PageDynaBuilder page2() {
        return page2.noPage(2).paragraphs(List.of(ParagraphDynaMother.paragraph1().build(),
        		ParagraphDynaMother.paragraph2().build()));
    }
    
    public static PageDynaBuilder page3() {
        return page3.noPage(3).paragraphs(List.of(ParagraphDynaMother.paragraph1().build(),
        		ParagraphDynaMother.paragraph2().build()));
    }
    
    public static class PageDynaBuilder {
        private Integer noPage;
        private List<ParagraphDyna> paragraphs;
        
        public PageDynaBuilder noPage(Integer noPage) {
            this.noPage = noPage;
            return this;
        }
        
        public PageDynaBuilder paragraphs(List<ParagraphDyna> paragraphs) {
            this.paragraphs = paragraphs;
            return this;
        }
        
        public PageDyna build() {
            return new PageDyna (this.noPage, this.paragraphs);
        }
    }
}
