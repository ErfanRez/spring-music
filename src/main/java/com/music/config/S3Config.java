package com.music.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}") // Access key from application.properties
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}") // Secret key from application.properties
    private String secretKey;

    @Value("${cloud.aws.endpoint}") // Endpoint from application.properties
    private String endpoint;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint)) // Use the endpoint from configuration
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // Enable path-style access
                        .build())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey) // Use your access and secret keys
                ))
                .region(Region.US_EAST_1) // Dummy region (required by the SDK, but not used)
                .build();
    }
}