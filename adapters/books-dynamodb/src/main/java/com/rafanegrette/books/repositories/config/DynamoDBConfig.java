package com.rafanegrette.books.repositories.config;

import com.rafanegrette.books.repositories.entities.UserDyna;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rafanegrette.books.repositories.entities.BookDyna;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;

@Configuration
@AllArgsConstructor
public class DynamoDBConfig {

	private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
	
	@Bean
	public DynamoDbTable<BookDyna> configDynamoDBTable() {
		TableSchema<BookDyna> booksSchema = BeanTableSchema.create(BookDyna.class);
		return dynamoDbEnhancedClient.table("Book", booksSchema);
	}

	@Bean
	public DynamoDbTable<UserDyna> configDynamoUserDBTable() {
		TableSchema<UserDyna> userSchema = BeanTableSchema.create(UserDyna.class);
		return dynamoDbEnhancedClient.table("User", userSchema);
	}
}
