package com.rafanegrette.books.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.io.Serializable;
import java.util.List;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserBookStateDyna implements Serializable {

    String userEmail;

    List<BookStateDyna> bookStateDynas;

    @DynamoDbPartitionKey
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<BookStateDyna> getBookStateDynas() {
        return bookStateDynas;
    }

    public void setBookStateDynas(List<BookStateDyna> bookStateDynas) {
        this.bookStateDynas = bookStateDynas;
    }

}
