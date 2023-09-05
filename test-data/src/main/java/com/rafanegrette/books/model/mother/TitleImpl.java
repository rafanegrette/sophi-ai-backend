package com.rafanegrette.books.model.mother;

import com.rafanegrette.books.model.Title;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TitleImpl implements Title {

    private String id;
    private String title; 
    private String label;
}
