package com.rafanegrette.books.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serializable;
import java.util.List;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class UserBookWriteStateDyna implements Serializable {

    String userEmail;

    List<BookWriteStateDyna> bookWriteStateDynas;

    @DynamoDbPartitionKey
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<BookWriteStateDyna> getBookWriteStateDynas() {
        return bookWriteStateDynas;
    }

    public void setBookWriteStateDynas(List<BookWriteStateDyna> bookWriteStateDynas) {
        this.bookWriteStateDynas = bookWriteStateDynas;
    }
}
