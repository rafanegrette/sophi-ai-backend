package com.rafanegrette.books.model.mother;

import java.util.List;

import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Page;

public class ChapterMother {

    private static ChapterBuilder potterChapter1 = new ChapterBuilder();
    private static ChapterBuilder potterChapter2 = new ChapterBuilder();
    
    public static ChapterBuilder potterChapter1() {
        return potterChapter1
        		.id(0)
                .title("The boy how lived")
                .pages(List.of(PageMother.page1().build(),
                        PageMother.page2().build(),
                        PageMother.page3().build()));
    }
    
    public static ChapterBuilder potterChapter2() {
        return potterChapter2
        		.id(1)
                .title("The vanishing glass")
                .pages(List.of(PageMother.page2().build(),
                        PageMother.page3().build()));
    }

    public static class ChapterBuilder {
    	private Integer id;
        private String title;
        private List<Page> pages;
        
        public ChapterBuilder id(Integer id) {
        	this.id = id;
        	return this;
        }
        public ChapterBuilder title(String title) {
            this.title = title;
            return this;
        }
        
        public ChapterBuilder pages(List<Page> pages) {
            this.pages = pages;
            return this;
        }
        
        public Chapter build() {
            return new Chapter(this.id, this.title, this.pages);
        }
    }

}
