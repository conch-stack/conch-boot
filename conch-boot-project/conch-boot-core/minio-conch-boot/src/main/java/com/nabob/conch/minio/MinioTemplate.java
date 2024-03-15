package com.nabob.conch.minio;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * <a herf="https://min.io/docs/minio/linux/developers/java/API.html">API</a>
 * <a herf="https://github.com/minio/minio-java/tree/release/examples">Example</a>
 * <a herf="https://blog.csdn.net/qq_43437874/category_10562215.html">CSDN</a>
 * <a herf="https://yunyanchengyu.blog.csdn.net/article/details/120855875">Policy</a>
 * <a herf="https://support.huaweicloud.com/perms-cfg-obs/obs_40_0040.html">OBS</a>
 */
public class MinioTemplate {

    private MinioClient minioClient;

    public MinioTemplate(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Bucket Operations
     */
    public void createBucket(String bucketName) throws NoSuchAlgorithmException, InvalidKeyException, IOException,
            ServerException, InvalidResponseException, XmlParserException, InternalException, ErrorResponseException, InsufficientDataException {
        MinioClient client = getMinioClient();
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public List<Bucket> getAllBuckets() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException,
            InternalException, ErrorResponseException, ServerException, InvalidResponseException, XmlParserException {
        return getMinioClient().listBuckets();
    }

    public Optional<Bucket> getBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InternalException, ErrorResponseException, ServerException, InvalidResponseException, XmlParserException {
        return getMinioClient().listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    public void removeBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InternalException, ErrorResponseException, ServerException, InvalidResponseException, XmlParserException {
        getMinioClient().removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * Object operations
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return getMinioClient().getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires).build());
    }

    public void saveObject(String bucketName, String objectName, InputStream stream, String contentType) throws IOException, ServerException,
            InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        getMinioClient().putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).contentType(contentType).build());
    }

    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return getMinioClient().statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public GetObjectResponse getObject(String bucketName, String objectName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return getMinioClient().getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public void removeObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        getMinioClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public MinioClient getMinioClient() {
        return this.minioClient;
    }
}
