package ltd.beihu.core.beihubootwebsample.config;

import ltd.beihu.core.beihubootwebsample.http.TestHttpClient;
import ltd.beihu.core.web.boot.http.RetrofitUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
@Configuration
@EnableConfigurationProperties({HttpProperties.class})
public class BlackListAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TestHttpClient testHttpClient(HttpProperties httpProperties) {
        return RetrofitUtil.getClient(httpProperties.getUrl(), TestHttpClient.class);
    }

}
