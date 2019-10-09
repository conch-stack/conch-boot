package ltd.beihu.core.minio.boot;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.policy.PolicyType;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
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
     * Set policy on bucket and object prefix.
     *
     * @param bucketName   Bucket name.
     * @param objectPrefix Name of the object prefix.
     * @param policyType   Enum of {@link PolicyType}.
     *
     * </p><b>Example:</b><br>
     * <pre>{@code setBucketPolicy("my-bucketname", "my-objectname", PolicyType.READ_ONLY); }</pre>
     */
    public void setBucketPolicy(String bucketName, String objectPrefix, PolicyType policyType)
            throws InvalidBucketNameException, InvalidObjectPrefixException, NoSuchAlgorithmException,
            InsufficientDataException, IOException, InvalidKeyException, NoResponseException,
            XmlPullParserException, ErrorResponseException, InternalException {
        getMinioClient().setBucketPolicy(bucketName, objectPrefix, policyType);
    }

    /**
     * Get bucket policy at given objectPrefix
     *
     * @param bucketName   Bucket name.
     * @param objectPrefix name of the object prefix
     *
     * </p><b>Example:</b><br>
     * <pre>{@code String policy = minioClient.getBucketPolicy("my-bucketname", "my-objectname");
     * System.out.println(policy); }</pre>
     */
    public PolicyType getBucketPolicy(String bucketName, String objectPrefix)
            throws InvalidBucketNameException, InvalidObjectPrefixException, NoSuchAlgorithmException,
            InsufficientDataException, IOException, InvalidKeyException, NoResponseException,
            XmlPullParserException, ErrorResponseException, InternalException {
        return getMinioClient().getBucketPolicy(bucketName, objectPrefix);
    }

    /**
     * Get all policies in the given bucket.
     *
     * @param bucketName the name of the bucket for which policies are to be listed.
     *
     * </p><b>Example:</b><br>
     * <pre>{@code
     *
     * final Map<String, PolicyType> policies = minioClient.getBucketPolicy("my-bucketname");
     * for (final Map.Entry<String, PolicyType> policyEntry : policies.entrySet()) {
     *    final String objectPrefix = policyEntry.getKey();
     *    final PolicyType policyType = policyEntry.getValue();
     *    System.out.println(
     *        String.format("Access permission %s found for object prefix %s", policyType.getValue(), objectPrefix)
     *    );
     * }}
     * </pre>
     *
     * @return a map of object prefixes (keys) to their policy types (values) for a given bucket.
     * @throws InvalidBucketNameException   upon an invalid bucket name
     * @throws IOException                  upon connection error
     * @throws InvalidKeyException          upon an invalid access key or secret key
     * @throws NoSuchAlgorithmException     upon requested algorithm was not found during signature calculation
     * @throws InsufficientDataException    upon insufficient data
     * @throws NoResponseException          upon no response from server
     * @throws XmlPullParserException       upon parsing response xml
     * @throws InternalException            upon internal library error
     * @throws ErrorResponseException       upon unsuccessful execution
     */
    public Map<String, PolicyType> getBucketPolicy(String bucketName)
            throws InvalidBucketNameException, IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, NoResponseException, XmlPullParserException,
            InternalException, ErrorResponseException, InvalidObjectPrefixException {
        // Input validation.
        return getMinioClient().getBucketPolicy(bucketName);
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
