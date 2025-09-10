package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.List;

public record PhoneticChapter(Integer id, String title, List<PhoneticPage> pages) implements Serializable {
}
