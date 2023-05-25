package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public record Page(@Id Integer number, @Field List<Paragraph> paragraphs) implements Serializable {

	public static final Page EMPTY_PAGE = new Page(-1, new ArrayList<>());

}
