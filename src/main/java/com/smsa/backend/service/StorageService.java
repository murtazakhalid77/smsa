package com.smsa.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private AmazonS3 s3Client;

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    public String uploadFile(byte[] fileData, String fileName) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileData.length);
        logger.info(BUCKET_NAME);
        s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, inputStream, metadata));
        return "File uploaded: " + fileName;
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
            e.printStackTrace();
        }

        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(BUCKET_NAME, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
