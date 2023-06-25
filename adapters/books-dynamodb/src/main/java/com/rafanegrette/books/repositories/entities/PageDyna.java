package com.rafanegrette.books.repositories.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class PageDyna implements Serializable {

	private Integer number; 
	private List<ParagraphDyna> paragraphs;
	
	public static final PageDyna EMPTY_PAGE = new PageDyna(-1, new ArrayList<>());

	@DynamoDbPartitionKey 
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<ParagraphDyna> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<ParagraphDyna> paragraphs) {
		this.paragraphs = paragraphs;
	}
	
	

}
