package com.nabob.conch.boot.websample.minio;

import com.nabob.conch.minio.MinioTemplate;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/files")
public class FileApi {

    @javax.annotation.Resource
    private MinioTemplate minioTemplate;

    @GetMapping("{bucketName}/**")
    public ResponseEntity<Resource> downloadFile(@PathVariable("bucketName") String bucketName,
                                                 HttpServletRequest request) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String path = String.valueOf(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        String objectName = path.substring(path.indexOf(bucketName) + bucketName.length() + 1);

        InputStream objectStream = minioTemplate.getObject(bucketName, objectName);
        String fileName = objectName.substring(objectName.lastIndexOf('/') + 1);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(new InputStreamResource(objectStream));
    }

    @PostMapping("/{bucketName}")
    public void uploadFile(@PathVariable("bucketName") String bucketName,
                           @RequestParam("objectName") String objectName,
                           @RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioTemplate.createBucket(bucketName);
        minioTemplate.saveObject(bucketName, objectName, file.getInputStream(), file.getContentType());

//        List<Bucket> allBuckets = minioTemplate.getAllBuckets();
//        for (Bucket allBucket : allBuckets) {
//            System.out.println(allBucket.name());
//            System.out.println(allBucket.creationDate());
//        }

//        String objectURL = minioTemplate.getObjectURL(bucketName, objectName, 60 * 5);
//        System.out.println(objectURL);

//        ObjectStat objectInfo = minioTemplate.getObjectInfo(bucketName, objectName);
//        System.out.println(objectInfo.toString());

//        String objectUrl = minioTemplate.getObjectUrl(bucketName, objectName);
//        System.out.println(objectUrl);

    }
}