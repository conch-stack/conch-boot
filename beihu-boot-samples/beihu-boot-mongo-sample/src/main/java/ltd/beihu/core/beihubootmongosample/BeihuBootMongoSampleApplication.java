package ltd.beihu.core.beihubootmongosample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class BeihuBootMongoSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeihuBootMongoSampleApplication.class, args);
    }

}
