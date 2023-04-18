package com.nabob.conch.tools.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * [流工具类]
 */
public class StreamUtils {
    public static String readFromInputStream(InputStream is) throws IOException {
        String line; // 用来保存每行读取的内容
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            line = reader.readLine(); // 读取第一行
            while (line != null) { // 如果 line 为空说明读完了
                sb.append(line); // 将读到的内容添加到 buffer 中
                sb.append("\r\n"); // 添加换行符
                line = reader.readLine(); // 读取下一行
            }
        }
        return sb.toString();
    }
}
