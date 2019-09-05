package ltd.beihu.core.beihubootwebsample.minio;

import io.minio.ObjectStat;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import ltd.beihu.core.minio.boot.MinioTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileApi {

    @javax.annotation.Resource
    private MinioTemplate minioTemplate;

    @GetMapping("{bucketName}/**")
    public ResponseEntity<Resource> downloadFile(@PathVariable("bucketName") String bucketName,
                                                 HttpServletRequest request)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException,
            InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException,
            XmlPullParserException, ErrorResponseException {

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
                           @RequestParam("file") MultipartFile file) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException,
            NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidExpiresRangeException {

        minioTemplate.createBucket(bucketName);

        minioTemplate.saveObject(bucketName, objectName, file.getInputStream(), file.getContentType());

        List<Bucket> allBuckets = minioTemplate.getAllBuckets();
        for (Bucket allBucket : allBuckets) {
            System.out.println(allBucket.name());
            System.out.println(allBucket.creationDate());
        }

        String objectURL = minioTemplate.getObjectURL(bucketName, objectName, 60 * 5);
        System.out.println(objectURL);

        ObjectStat objectInfo = minioTemplate.getObjectInfo(bucketName, objectName);
        System.out.println(objectInfo.toString());

//        String objectUrl = minioTemplate.getObjectUrl(bucketName, objectName);
//        System.out.println(objectUrl);

    }

    @GetMapping("/{bucketName}")
    public void uploadFile(@PathVariable("bucketName") String bucketName,
                           @RequestParam("objectName") String objectName) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException,
            NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidExpiresRangeException {

        minioTemplate.removeObject(bucketName, objectName);

        InputStream objectStream = minioTemplate.getObject(bucketName, objectName);

        System.out.println("remove");


    }
}