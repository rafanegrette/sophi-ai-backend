package com.rafanegrette.books.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public record Book(@Id String id, 
					String title,
                    @Field List<ContentIndex> contentTable,
                    @Field List<Chapter> chapters)
                implements Serializable {	

    public static final Book EMPTY_BOOK = new Book("", "", new ArrayList<>(), new ArrayList<>());
}
