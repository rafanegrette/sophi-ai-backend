package com.rafanegrette.books.repositories.entities.mother;

import java.util.List;

import com.rafanegrette.books.repositories.entities.ChapterDyna;
import com.rafanegrette.books.repositories.entities.PageDyna;

public class ChapterDynaMother {
	  private static ChapterDynaBuilder potterChapter1 = new ChapterDynaBuilder();
	    private static ChapterDynaBuilder potterChapter2 = new ChapterDynaBuilder();
	    
	    public static ChapterDynaBuilder potterChapter1() {
	        return potterChapter1
	        		.id(0)
	                .title("The boy how lived")
	                .pages(List.of(PageDynaMother.page1().build(),
	                		PageDynaMother.page2().build()));
	    }
	    
	    public static ChapterDynaBuilder potterChapter2() {
	        return potterChapter2
	        		.id(1)
	                .title("The vanishing glass")
	                .pages(List.of(PageDynaMother.page2().build(),
	                		PageDynaMother.page3().build()));
	    }

	    public static class ChapterDynaBuilder {
	    	private Integer id;
	        private String title;
	        private List<PageDyna> pages;
	        
	        public ChapterDynaBuilder id(Integer id) {
	        	this.id = id;
	        	return this;
	        }
	        public ChapterDynaBuilder title(String title) {
	            this.title = title;
	            return this;
	        }
	        
	        public ChapterDynaBuilder pages(List<PageDyna> pages) {
	            this.pages = pages;
	            return this;
	        }
	        
	        public ChapterDyna build() {
	            return new ChapterDyna(this.id, this.title, this.pages);
	        }
	    }
}
