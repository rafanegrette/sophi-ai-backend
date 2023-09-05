package com.rafanegrette.books.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TitleImpl implements Title {

    private String id;
    private String title; 
    private String label;
}
