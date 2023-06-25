package com.rafanegrette.books.models;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Sentence(@Id Integer id, String text) implements Serializable {

}
