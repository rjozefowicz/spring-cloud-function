package dev.jozefowicz.companydisc.function.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfiguration {

    @Bean
    public RekognitionClient rekognitionClient() {
        return RekognitionClient
                .builder()
                .httpClient(ApacheHttpClient.create())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .httpClient(ApacheHttpClient.create())
                .build();
    }

}
