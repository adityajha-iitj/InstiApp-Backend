package in.ac.iitj.instiapp.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import in.ac.iitj.instiapp.services.BucketService;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

@Service
public class BucketServiceImpl implements BucketService {

    private static final Logger LOG = LogManager.getLogger(BucketServiceImpl.class);

    private AmazonS3 s3Client;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    @PostConstruct
    private void init() {
        BasicAWSCredentials creds =
                new BasicAWSCredentials(accessKeyId, secretAccessKey);

        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();
    }

    // To upload the file into s3
    public void uploadFile(String bucketName, String objectName, String filePath) {
        try {
            LOG.info("Uploading file {} to bucket {}...", filePath, bucketName);
            s3Client.putObject(bucketName, objectName, new File(filePath));
            LOG.info("Upload successful.");
        } catch (AmazonServiceException e) {
            LOG.error("Upload failed: {}", e.getErrorMessage());
            throw e;
        }
    }

    // To delete the s3 bucket
    public void deleteBucket(String bucketName) {
        try {
            LOG.info("Deleting bucket {}...", bucketName);
            s3Client.deleteBucket(bucketName);
            LOG.info("Bucket deleted successfully.");
        } catch (AmazonServiceException e) {
            LOG.error("Delete bucket failed: {}", e.getErrorMessage());
            throw e;
        }
    }

    public void deleteFile(String bucketName, String objectKey) {
        try {
            LOG.info("Deleting object {}/{} …", bucketName, objectKey);
            s3Client.deleteObject(bucketName, objectKey);
            LOG.info("Delete successful.");
        } catch (AmazonServiceException e) {
            LOG.error("Delete failed: {}", e.getErrorMessage());
            throw e;
        }
    }


    public String getFileUrl(String bucketName, String objectName) {
        try {
            LOG.info("Generating URL for {}/{} …", bucketName, objectName);
            return s3Client.getUrl(bucketName, objectName).toString();
        } catch (AmazonServiceException e) {
            LOG.error("Failed to get URL for {}/{}: {}", bucketName, objectName, e.getErrorMessage());
            throw e;
        }
    }



}

