package com.rafanegrette.books.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
public class UserBookWriteStateDyna extends UserBookStateDyna {
    public UserBookWriteStateDyna(String userEmail, List<BookStateDyna> bookStateDynas) {
        super(userEmail, bookStateDynas);
    }

    public UserBookWriteStateDyna() {
    }
}
