package com.github.rivulet.store;

import com.github.rivulet.types.Message;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
public class S3Service {
    @Autowired
    private Deserializer deserializer;

    private MinioClient minioClient;

    public S3Service () {
        this.minioClient =
                MinioClient.builder()
                        .endpoint("http://172.17.0.2:9000/")
                        .credentials("luGouhOsQopdXEsSJmyQ", "HDP2aIeEjMnG3bbFRVpSlgCjfqGT7gfS1OFlUBVZ")
                        .build();
    }

    public boolean bucketExists(String bucketName) {
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            return found;
        }
        catch(Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean uploadFile(String bucketName, String objectName, String filePath) {
        File file=new File(filePath);
        try (InputStream is = new FileInputStream(file)) {
            // Upload the file to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, file.length(), -1) // -1 means no known size (stream-based)
                            .build()
            );
            System.out.println("File uploaded successfully.");
            return true;
        } catch (Exception e) {
            System.err.println("Error uploading file: " + e.getMessage());
            return false;
        }

    }

    public List<Message> downloadFile(String bucketName, String objectName) {
        List<Message> messages=null;
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build())) {

            messages=deserializer.deserializeMessages(stream);
            System.out.println("Printing messages after download and deserializing");
            for (Message msg:messages) {
                System.out.println(msg.toString());
            }
            return messages;

        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            System.err.println("Error occurred: " + e.getMessage());
            return null;
        }
    }


    private String getContentType(String filePath) {
        String contentType = "application/octet-stream";
        int indexOfLastDot = filePath.lastIndexOf('.');
        if (indexOfLastDot > 0) {
            String extension = filePath.substring(indexOfLastDot + 1);
            switch (extension) {
                case "png":
                    contentType = "image/png";
                    break;
                case "jpg":
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                // Add more cases for other extensions if needed
            }
        }
        return contentType;
    }
}
