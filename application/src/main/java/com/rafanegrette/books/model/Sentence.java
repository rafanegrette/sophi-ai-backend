package com.rafanegrette.books.model;

import java.io.Serializable;

public record Sentence(Integer id, String text) implements Serializable {

}
