package com.rafanegrette.books.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public record Chapter(@Id Integer id, String title, @Field List<Page> pages) implements Serializable {
}
