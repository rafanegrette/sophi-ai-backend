package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.List;

public record Chapter(Integer id, String title, List<Page> pages) implements Serializable {
}
