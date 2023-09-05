package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public record Book(String id, 
					String title,
					String label,
                    List<ContentIndex> contentTable,
                    List<Chapter> chapters)
                implements Serializable {	

    public static final Book EMPTY_BOOK = new Book("", "", "", new ArrayList<>(), new ArrayList<>());
}
