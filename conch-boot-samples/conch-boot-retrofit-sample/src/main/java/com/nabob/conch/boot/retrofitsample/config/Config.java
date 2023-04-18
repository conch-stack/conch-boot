package com.nabob.conch.boot.retrofitsample.config;

import com.nabob.conch.retrofit.annotation.RetrofitClientScan;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描
 *
 * @author Adam
 * @date 2020/8/4
 */
@Configuration
@RetrofitClientScan(basePackages = "com.nabob.conch.boot.retrofitsample")
public class Config {
}
