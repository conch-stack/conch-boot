package com.nabob.conch.tools.utils;

import com.nabob.conch.tools.properties.NativePath;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileUtils {
    public static String getName(String filePath) {
        if (filePath.contains(".")) {
            filePath = filePath.substring(0, filePath.lastIndexOf("."));
        }
        if (filePath.contains("\\")) {
            filePath = filePath.substring(filePath.lastIndexOf("\\"));
        }
        if (filePath.contains("/")) {
            filePath = filePath.substring(filePath.lastIndexOf("/"));
        }
        return filePath;
    }

    public static String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = Files.newInputStream(NativePath.get(filePath), StandardOpenOption.READ);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
//                sb.append("\r\n");
            }
        } catch (Exception ignored) {
        }
        return sb.toString();
    }
}
