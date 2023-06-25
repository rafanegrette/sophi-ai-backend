package com.rafanegrette.books.repositories.entities.mother;

import java.util.List;

import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.ChapterDyna;
import com.rafanegrette.books.repositories.entities.ContentIndexDyna;

public class BookMother {
    
    private static BookBuilder harryPotter1 = new BookBuilder();
    
    public static BookBuilder harryPotter1() {
        return harryPotter1
                .id("Harry-1")
                .title("Harry Potter and the Sorcerer's Stone")
                .chapters(List.of(ChapterMother.potterChapter1().build(),
                        ChapterMother.potterChapter2().build()));
    }
    
    public static BookBuilder harryPotter2() {
        return harryPotter1
                .id("Harry-2")
                .title("Harry Potter and the forty thieves")
                .chapters(List.of(ChapterMother.potterChapter1().build(),
                        ChapterMother.potterChapter2().build()));
    }
    
    public static class BookBuilder {
        private String title, id;
        private List<ContentIndexDyna> contentTable;
        private List<ChapterDyna> chapters;
        
        public BookBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }
        
        public BookBuilder contentTable(List<ContentIndexDyna> contentTable) {
            this.contentTable = contentTable;
            return this;
        }
        
        public BookBuilder chapters(List<ChapterDyna> chapters) {
            this.chapters = chapters;
            return this;
        }
        
        public BookDyna build() {
            return new BookDyna(id, title, contentTable, chapters);
        }
    }

}
