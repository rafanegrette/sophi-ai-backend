package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public record PhoneticBook(String id,
                           String title,
                           String label,
                           List<ContentIndex> contentTable,
                           List<PhoneticChapter> chapters)
                implements Serializable {	

    public static final PhoneticBook EMPTY_BOOK = new PhoneticBook("", "", "", new ArrayList<>(), new ArrayList<>());
}
