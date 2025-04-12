package com.music.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
public class S3Service {

    private final S3Client s3Client;

    private final String bucketName = "spring";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String key, byte[] fileBytes) {
        // Upload the file to S3
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), RequestBody.fromBytes(fileBytes));

        String endpoint = "storage.c2.liara.space";
        // Return the S3 URL
        return String.format("https://%s/%s/%s", endpoint, bucketName, key);
    }

    public void deleteFile(String key) {
        // Delete the file from S3
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }
}
