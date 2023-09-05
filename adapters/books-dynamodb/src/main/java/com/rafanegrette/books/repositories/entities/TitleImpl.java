package com.rafanegrette.books.repositories.entities;

import com.rafanegrette.books.model.Title;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TitleImpl implements Title {
	private String id;
	private String title;
	private String label;
}
