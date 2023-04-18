package com.nabob.conch.tools.properties;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Properties;

/**
 * [配置读取工具类]
 */
public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 获取资源中的属性文件
     *
     * @param clazz        -类名
     * @param resourceName －资源名
     * @return －Properties
     */
    public static Properties getResourceProperties(Class clazz, String resourceName) {
        Properties prop = new Properties();
        try (InputStream in = clazz.getResourceAsStream(resourceName)) {
            prop.load(in);
        } catch (IOException e) {
            prop = null;
            e.printStackTrace();
        }

        return prop;
    }

    /**
     * 根据绝对路径获得属性文件
     *
     * @param path －路径
     * @return －Properties
     */
    public static Properties getProperties(String path) {
        Properties prop = new Properties();
        try (InputStream in = Files.newInputStream(NativePath.get(path), StandardOpenOption.READ)) {
            prop.load(in);
        } catch (IOException e) {
            prop = null;
            e.printStackTrace();
            logger.error("error to getProperties from{}", path);
        }
        return prop;
    }

    /**
     * 根据绝对路径获得文件流
     *
     * @param path －路径
     * @return －Properties
     */
    public static InputStream getInput(String path) {
        InputStream stream;
        try {
            stream = Files.newInputStream(NativePath.get(path), StandardOpenOption.READ);
        } catch (IOException e) {
            stream = null;
            e.printStackTrace();
        }

        return stream;
    }


    /**
     * 根据绝对路径获得文件流
     *
     * @param path －路径
     * @return －Properties
     */
    public static DirectoryStream<Path> getDir(String path) {
        DirectoryStream<Path> stream;
        try {
            stream = Files.newDirectoryStream(NativePath.get(path));
        } catch (IOException e) {
            stream = null;
            e.printStackTrace();
        }

        return stream;
    }

    public static Properties map2Properties(Map<?, ?> map) {
        Properties properties = new Properties();
        if (!MapUtils.isEmpty(map)) {
            for (Object key : map.keySet()) {
                properties.put(key, map.get(key));
            }
        }
        return properties;
    }

}
