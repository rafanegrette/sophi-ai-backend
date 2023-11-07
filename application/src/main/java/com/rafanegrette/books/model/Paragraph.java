package com.rafanegrette.books.model;

import java.io.Serializable;
import java.util.List;

public record Paragraph(Integer id,
                        List<Sentence> sentences) 
						implements Serializable {

}
