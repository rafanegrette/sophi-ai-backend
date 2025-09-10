package com.rafanegrette.books.model.mother;

import java.util.List;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.ContentIndex;

public class BookMother {
    
    private static final BookBuilder harryPotter1 = new BookBuilder();
    
    public static BookBuilder harryPotter1() {
        return harryPotter1
                .id("Harry-1")
                .label("Harry-1")
                .title("Harry Potter and the Sorcerer's Stone")
                .chapters(List.of(ChapterMother.potterChapter1().build(),
                        ChapterMother.potterChapter2().build()));
    }

    public static BookBuilder harryPotter1Phonetic() {
        return harryPotter1
                .id("Harry-1")
                .label("We can assume that is the original book with the content changed, " +
                        "we will not testing if the content changed, but the concept of it as it was.")
                .title("Harry Potter and the Sorcerer's Stone")
                .chapters(List.of(ChapterMother.potterChapterPhonetic1().build(),
                        ChapterMother.potterChapter2().build()));
    }

    public static BookBuilder harryPotter2() {
        return harryPotter1
                .id("Harry-2")
                .id("Harry-2")
                .title("Harry Potter and the forty thieves")
                .chapters(List.of(ChapterMother.potterChapter1().build(),
                        ChapterMother.potterChapter2().build()));
    }
    
    public static class BookBuilder {
        private String title, id, label;
        private List<ContentIndex> contentTable;
        private List<Chapter> chapters;
        
        public BookBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }
        
        public BookBuilder label(String label) {
        	this.label = label;
        	return this;
        }
        
        public BookBuilder contentTable(List<ContentIndex> contentTable) {
            this.contentTable = contentTable;
            return this;
        }
        
        public BookBuilder chapters(List<Chapter> chapters) {
            this.chapters = chapters;
            return this;
        }
        
        public Book build() {
            return new Book(id, title, label, contentTable, chapters);
        }
    }

}
