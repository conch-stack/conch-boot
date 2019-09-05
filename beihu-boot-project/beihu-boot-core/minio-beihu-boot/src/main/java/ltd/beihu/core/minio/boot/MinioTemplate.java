package ltd.beihu.core.minio.boot;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


public class MinioTemplate {

    private MinioClient minioClient;

    public MinioTemplate(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Bucket Operations
     */
    public void createBucket(String bucketName) throws XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, IOException, RegionConflictException, NoResponseException, InternalException, ErrorResponseException, InsufficientDataException, InvalidBucketNameException {
        MinioClient client = getMinioClient();
        if(!client.bucketExists(bucketName)){
            client.makeBucket(bucketName);
        }
    }

    public List<Bucket> getAllBuckets() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().listBuckets();
    }

    public Optional<Bucket> getBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().listBuckets().stream().filter( b -> b.name().equals(bucketName)).findFirst();
    }

    public void removeBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        getMinioClient().removeBucket(bucketName);
    }

    /**
     * Object operations
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().presignedGetObject(bucketName, objectName, expires);
    }

    public void saveObject(String bucketName, String objectName, InputStream stream, String contentType) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        getMinioClient().putObject(bucketName, objectName, stream, contentType);
    }

    public void saveObject(String bucketName, String objectName, InputStream stream, long size, String contentType) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        getMinioClient().putObject(bucketName, objectName, stream, size, contentType);
    }

    public ObjectStat getObjectInfo(String bucketName, String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().statObject(bucketName, objectName);
    }

    public InputStream getObject(String bucketName, String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().getObject(bucketName, objectName);
    }

//    public String getObjectUrl(String bucketName, String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
//        return getMinioClient().getObjectUrl(bucketName, objectName);
//    }

    public void removeObject(String bucketName, String objectName ) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidArgumentException {
        getMinioClient().removeObject(bucketName, objectName);
    }

    public MinioClient getMinioClient() {
        return this.minioClient;
    }
}
