package com.rafanegrette.books.repositories.entities;

import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
@NoArgsConstructor
public class UserBookReadStateDyna extends UserBookStateDyna{

    public UserBookReadStateDyna(String userEmail, List<BookStateDyna> bookStateDynas) {
        super(userEmail, bookStateDynas);
    }
}
