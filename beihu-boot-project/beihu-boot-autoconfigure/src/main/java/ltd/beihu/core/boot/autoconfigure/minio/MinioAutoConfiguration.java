package ltd.beihu.core.boot.autoconfigure.minio;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import ltd.beihu.core.minio.boot.MinioTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({MinioClient.class, MinioTemplate.class})
@EnableConfigurationProperties({ MinioProperties.class })
public class MinioAutoConfiguration {

    private MinioProperties properties;

    @Bean
    @ConditionalOnMissingBean(MinioTemplate.class)
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(properties.getUrl(), properties.getAccessKey(), properties.getSecretKey());
    }

    @Bean
    @ConditionalOnMissingBean(MinioTemplate.class)
    MinioTemplate minioTemplate(MinioClient minioClient){
        return new MinioTemplate(minioClient);
    }
}
