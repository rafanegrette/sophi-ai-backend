package com.rafanegrette.books.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serializable;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class BookWriteStateDyna implements Serializable {

    String bookId;

    Integer chapterId;
    Integer pageNo;
    Integer paragraphId;
    Integer sentenceId;
    Boolean finished;

    @DynamoDbPartitionKey
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getParagraphId() {
        return paragraphId;
    }

    public void setParagraphId(Integer paragraphId) {
        this.paragraphId = paragraphId;
    }

    public Integer getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Integer sentenceId) {
        this.sentenceId = sentenceId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
