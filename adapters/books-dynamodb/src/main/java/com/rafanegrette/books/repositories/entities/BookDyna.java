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
public class BookDyna implements Serializable {
	
					 
	String id; 
	String title;
    List<ContentIndexDyna> contentTable;
    List<ChapterDyna> chapters;

	
    public static final BookDyna EMPTY_BOOK = new BookDyna("", "", new ArrayList<>(), new ArrayList<>());

    @DynamoDbPartitionKey
	public String getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public List<ContentIndexDyna> getContentTable() {
		return contentTable;
	}


	public List<ChapterDyna> getChapters() {
		return chapters;
	}


	public void setId(String id) {
		this.id = id;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setContentTable(List<ContentIndexDyna> contentTable) {
		this.contentTable = contentTable;
	}


	public void setChapters(List<ChapterDyna> chapters) {
		this.chapters = chapters;
	}
    
    
}
