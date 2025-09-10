package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record PhoneticPage(Integer number, List<PhoneticParagraph> paragraphs) implements Serializable {

}
