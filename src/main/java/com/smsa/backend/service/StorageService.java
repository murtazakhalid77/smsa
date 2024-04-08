package com.smsa.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
@Slf4j
public class StorageService {


    public static final String BUCKET_NAME="cdvinv";
    public static final String MANIFEST_FOLDER = "manifestFolder/";
    @Autowired
    private AmazonS3 s3Client;

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);
    public List<String> getFileNamesInManifestFolder() {
        List<String> fileNames = new ArrayList<>();
        try {
            if (s3Client == null) {
                throw new IllegalStateException("s3Client is not initialized");
            }

            ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(BUCKET_NAME)
                    .withPrefix(MANIFEST_FOLDER);
            ListObjectsV2Result result;
            boolean isFirstIteration = true;
            do {
                result = s3Client.listObjectsV2(request);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    String fileName = objectSummary.getKey();

                    if (isFirstIteration) {
                        isFirstIteration=false;
                        continue;
                    }
                    // Remove the "manifestFolder/" prefix
                    if (fileName.startsWith(MANIFEST_FOLDER)) {
                        fileName = fileName.substring(MANIFEST_FOLDER.length());
                    }
                    fileNames.add(fileName);
                }
                request.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());
        } catch (Exception e) {
            logger.error("Failed to retrieve file names from manifest folder", e);
            throw new RuntimeException("Failed to retrieve file names from manifest folder", e);
        }
        return fileNames;
    }

    public String uploadFile(byte[] fileData, String fileName) {
        try{
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileData.length);
            logger.info(BUCKET_NAME);
            s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, inputStream, metadata));
            return "File uploaded: " + fileName;
        }catch (Exception e){
            logger.error("file not uplaoded to s3 bucket",fileName);
            throw new RuntimeException(e.getMessage());
        }

    }



    public byte[] downloadFile(String fileName) {
        try {
            S3Object s3Object = s3Client.getObject(BUCKET_NAME, fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            try {
                return IOUtils.toByteArray(inputStream);
            } finally {
                // Ensure the input stream is closed
                inputStream.close();
            }
        } catch (IOException e) {
            logger.error("cannot download file from s3 bucket");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
