package com.rafanegrette.books.repositories.config;

import com.rafanegrette.books.repositories.entities.UserBookReadStateDyna;
import com.rafanegrette.books.repositories.entities.UserBookWriteStateDyna;
import com.rafanegrette.books.repositories.entities.UserDyna;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rafanegrette.books.repositories.entities.BookDyna;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;

@Configuration
@AllArgsConstructor
public class DynamoDBConfig {

	private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
	
	@Bean
	@Qualifier("DynamoBook")
	@Primary
	public DynamoDbTable<BookDyna> configDynamoDBTable() {
		TableSchema<BookDyna> booksSchema = BeanTableSchema.create(BookDyna.class);
		return dynamoDbEnhancedClient.table("Book", booksSchema);
	}

	@Bean
	@Qualifier("DynamoBookPhonetic")
	public DynamoDbTable<BookDyna> configDynamoBookPhoneticDBTable() {
		TableSchema<BookDyna> booksSchema = BeanTableSchema.create(BookDyna.class);
		return dynamoDbEnhancedClient.table("BookPhonetic", booksSchema);
	}

	@Bean
	public DynamoDbTable<UserDyna> configDynamoUserDBTable() {
		TableSchema<UserDyna> userSchema = BeanTableSchema.create(UserDyna.class);
		return dynamoDbEnhancedClient.table("User", userSchema);
	}

	@Bean
	public DynamoDbTable<UserBookWriteStateDyna> configDynamoUserBookWriteStateDBTable() {
		TableSchema<UserBookWriteStateDyna> userSchema = BeanTableSchema.create(UserBookWriteStateDyna.class);
		return dynamoDbEnhancedClient.table("UserBookWriteState", userSchema);
	}

	@Bean
	public DynamoDbTable<UserBookReadStateDyna> configDynamoUserBookReadStateDBTable() {
		TableSchema<UserBookReadStateDyna> userSchema = BeanTableSchema.create(UserBookReadStateDyna.class);
		return dynamoDbEnhancedClient.table("UserBookReadState", userSchema);
	}
}
