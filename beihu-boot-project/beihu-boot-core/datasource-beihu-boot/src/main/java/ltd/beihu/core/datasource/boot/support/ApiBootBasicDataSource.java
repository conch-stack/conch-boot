package ltd.beihu.core.datasource.boot.support;

import com.zaxxer.hikari.HikariDataSource;
import ltd.beihu.core.datasource.boot.ApiBootDataSource;
import ltd.beihu.core.datasource.boot.config.DataSourceConfig;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;

/**
 * basic data source config
 *
 * @author：恒宇少年 - 于起宇
 * <p>
 * DateTime：2019-04-01 15:08
 * Blog：http://blog.yuqiyu.com
 * WebSite：http://www.jianshu.com/u/092df3f77bca
 * Gitee：https://gitee.com/hengboy
 * GitHub：https://github.com/hengboy
 */
public class ApiBootBasicDataSource extends DelegatingDataSource implements ApiBootDataSource {
    private DataSourceConfig config;

    public ApiBootBasicDataSource(DataSourceConfig config) {
        this.config = config;
        this.setTargetDataSource(build());
    }

    /**
     * create default basic data source
     *
     * @return DataSource
     * @throws RuntimeException 异常信息
     */
    @Override
    public DataSource build() throws RuntimeException {
        try {
            DataSource dataSource = DataSourceBuilder.create().url(config.getUrl()).username(config.getUsername()).password(config.getPassword()).driverClassName(config.getDriverClassName())
                    // springboot 2.x default is HikariDataSource
                    .type(HikariDataSource.class)
                    .build();
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException("Create a default data source exception", e);
        }

    }
}
