package com.nabob.conch.tools.utils;

import java.io.File;

/**
 * @author Adam
 * @since 2023/6/15
 */
public class RFileUtils {

    public static String getFileData(String fileName) {
        try {
            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }
            String fullPath = FileUtils.class.getResource("/").getPath() + fileName;
            return doGetFileData(fullPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String doGetFileData(String fullPath) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(new File(fullPath), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
