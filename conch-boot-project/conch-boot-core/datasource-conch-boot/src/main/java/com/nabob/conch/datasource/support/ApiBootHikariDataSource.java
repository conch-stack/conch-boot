package com.nabob.conch.datasource.support;

import com.nabob.conch.datasource.ApiBootDataSource;
import com.zaxxer.hikari.HikariDataSource;
import com.nabob.conch.datasource.config.DataSourceHikariConfig;

import javax.sql.DataSource;

/**
 * Api Boot Hikari Data Source
 *
 * @author：恒宇少年 - 于起宇
 * <p>
 * DateTime：2019-04-01 15:01
 * Blog：http://blog.yuqiyu.com
 * WebSite：http://www.jianshu.com/u/092df3f77bca
 * Gitee：https://gitee.com/hengboy
 * GitHub：https://github.com/hengboy
 */
public class ApiBootHikariDataSource extends HikariDataSource implements ApiBootDataSource {

    public ApiBootHikariDataSource(DataSourceHikariConfig config) {
        try {
            this.setJdbcUrl(config.getUrl());
            this.setUsername(config.getUsername());
            this.setPassword(config.getPassword());
            config.getProperty().keySet().stream().forEach(param -> this.addDataSourceProperty(param, config.getProperty().get(param)));
        } catch (Exception e) {
            throw new RuntimeException("Create new Hikari dataSource fail.", e);
        }
    }

    /**
     * create new Hikari dataSource instance
     *
     * @return
     * @throws RuntimeException
     */
    @Override
    public DataSource build() throws RuntimeException {
        return this;
    }
}
