package dev.jozefowicz.workshop.companydisc.configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3Client.builder().build();
    }

    @Bean
    public DynamoDB dynamodb() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        return new DynamoDB(client);
    }

}
