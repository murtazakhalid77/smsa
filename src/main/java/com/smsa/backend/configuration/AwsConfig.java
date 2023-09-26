package com.smsa.backend.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.smsa.backend.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.credentials.accessKey}")
    String awsBucketAccessKey;

    @Value("${spring.cloud.aws.credentials.secretKey}")
    String awsBucketSecretKey;

    @Value("${spring.cloud.aws.region.static}")
    String awsRegion;
    private static final Logger logger = LoggerFactory.getLogger(AwsConfig.class);
    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(awsBucketAccessKey, awsBucketSecretKey);
        logger.info(awsBucketAccessKey);
        logger.info(awsBucketSecretKey);
        logger.info(awsRegion);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.ME_CENTRAL_1)
                .build();

    }
}
