package com.rafanegrette.books.model;

import java.io.Serializable;

public record PhoneticSentence(Integer id, String text, String phonetic) implements Serializable {

}
