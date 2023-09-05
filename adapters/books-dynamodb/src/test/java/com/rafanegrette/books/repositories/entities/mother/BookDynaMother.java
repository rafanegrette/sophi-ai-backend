package com.rafanegrette.books.repositories.entities.mother;

import java.util.List;

//import com.rafanegrette.books.model.mother.BookMother.BookBuilder;
import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.ChapterDyna;
import com.rafanegrette.books.repositories.entities.ContentIndexDyna;

public class BookDynaMother {

	private static BookDynaBuilder harryPotter1 = new BookDynaBuilder();
	
	public static BookDynaBuilder harryPotter1() {
		return harryPotter1
				.id("Harry-1")
				.label("Harry-1")
				.title("Harry Potter and the Sorcerer's Stone")
				.chapters(List.of(ChapterDynaMother.potterChapter1().build(),
						ChapterDynaMother.potterChapter2().build()));
	}
	
	public static class BookDynaBuilder {
        private String title, id, label;
        private List<ContentIndexDyna> contentTable;
        private List<ChapterDyna> chapters;
        
        public BookDynaBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public BookDynaBuilder title(String title) {
            this.title = title;
            return this;
        }
        
        public BookDynaBuilder label(String label) {
        	this.label = label;
        	return this;
        }
        
        public BookDynaBuilder contentTable(List<ContentIndexDyna> contentTable) {
            this.contentTable = contentTable;
            return this;
        }
        
        public BookDynaBuilder chapters(List<ChapterDyna> chapters) {
            this.chapters = chapters;
            return this;
        }
        
        public BookDyna build() {
            return new BookDyna(id, title, label, contentTable, chapters);
        }
	}
}
