package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record Page(Integer number, List<Paragraph> paragraphs) implements Serializable {

	public static final Page EMPTY_PAGE = new Page(-1, new ArrayList<>());

}
