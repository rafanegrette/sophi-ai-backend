package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.List;

public record PhoneticParagraph(Integer id,
                                List<PhoneticSentence> sentences)
						implements Serializable {

}
