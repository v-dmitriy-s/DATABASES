package com.dtit.inventory.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.dtit.inventory.model.InventoryItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.dynamodb.region}")
    private String amazonAWSRegion;

    @Value("${amazon.aws.access-key:}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secret-key:}")
    private String amazonAWSSecretKey;

//    @Bean
//    public DynamoDBMapper dynamoDBMapper() {
//        return new DynamoDBMapper(buildAmazonDynamoDB());
//    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonAWSRegion))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("fakeMyKeyId", "fakeSecretAccessKey")))
                .build();
    }

//    private AmazonDynamoDB buildAmazonDynamoDB() {
//        return AmazonDynamoDBClientBuilder
//                .standard()
//                .withEndpointConfiguration(
//                        new AwsClientBuilder.EndpointConfiguration(
//                                amazonDynamoDBEndpoint,
//                                amazonAWSRegion
//                        )
//                )
//                .withCredentials(
//                        new AWSStaticCredentialsProvider(
//                                new BasicAWSCredentials(
//                                        amazonAWSAccessKey,
//                                        amazonAWSSecretKey
//                                )
//                        )
//                )
//                .build();
//    }
}
