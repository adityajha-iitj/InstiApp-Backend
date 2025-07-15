package in.ac.iitj.instiapp.services;

import java.util.List;

public interface BucketService {

    // upload file to S3
    void uploadFile(String bucketName, String objectName, String filePath);

    // delete bucket
    void deleteBucket(String bucketName);

    public String getFileUrl(String bucketName, String objectName);
    void deleteFile(String bucketName, String objectKey);
}
