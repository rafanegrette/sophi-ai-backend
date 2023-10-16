package com.rafanegrette.books.repositories.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.TableStatus;

@Configuration
public class DynamoClient {

	private String awsAccessKey;
	private String awsSecretKey;
	@Value("${aws.region}")
	private String region;
	
	@Bean
	public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
		return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient())
        .build();
		
		
	}
	
	@Bean
	public DynamoDbClient dynamoDbClient() {
		var dynamoDbClient = DynamoDbClient.builder()
        		//.endpointOverride(URI.create("http://172.17.0.2:8000"))
        		.credentialsProvider(DefaultCredentialsProvider.create())
				.region(Region.of(region))
        		.build();
		
        createBookTable(dynamoDbClient);
        return dynamoDbClient;
	}

	private void createBookTable(DynamoDbClient dynamoDbClient) {
		try {
        	dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("Book").build());
        } catch (ResourceNotFoundException e) {
        	dynamoDbClient.createTable(CreateTableRequest.builder()
        			.tableName("Book")
        			.keySchema(KeySchemaElement.builder()
        					.attributeName("id")
        					.keyType(KeyType.HASH)
        					.build())
        			.attributeDefinitions(AttributeDefinition.builder()
        					.attributeName("id")
        					.attributeType(ScalarAttributeType.S)
        					.build())
        			.provisionedThroughput(ProvisionedThroughput.builder()
        					.readCapacityUnits(5L)
        					.writeCapacityUnits(5L)
        					.build())
        			.build());
        	
        	while(true) {
        		try {
        			Thread.sleep(1000);
        		} catch(InterruptedException ie) {
        			Thread.currentThread().interrupt();
        			throw new RuntimeException("Interrup");
        		}
        		
        		String tableStatus = dynamoDbClient
        				.describeTable(DescribeTableRequest.builder()
        				.tableName("Book").build())
        				.table()
        				.tableStatusAsString();
        		if (tableStatus.equals(TableStatus.ACTIVE.toString())) {
        			break;
        		}
        	}
        }
	}
}
