package com.rafanegrette.books.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TitleImpl implements Title {
	private String id;
	private String title;
}
