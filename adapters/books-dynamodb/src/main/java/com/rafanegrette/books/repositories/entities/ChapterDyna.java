package com.rafanegrette.books.repositories.entities;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDyna implements Serializable {
		
	private Integer id;
	private String title; 
	private List<PageDyna> pages;
	
	@DynamoDbPartitionKey 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<PageDyna> getPages() {
		return pages;
	}
	public void setPages(List<PageDyna> pages) {
		this.pages = pages;
	}
	
	
}
