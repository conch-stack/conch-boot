package ltd.beihu.core.tools.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.*;

/**
 * @Project: [PermissionServiceSys]
 * @Package: [com.pengshu.common.utils]
 * @Description: [压缩工具类]
 * @Author: [toming]
 * @CreateDate: [5/23/16 11:20 AM]
 * @Version: [v1.0]
 */
public class ZipUtils {

    /**
     * 使用gzip进行压缩
     */
    public static String gzip(String primStr) {
        if (StringUtils.isEmpty(primStr)) {
            return primStr;
        }

        String compressedStr;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(primStr.getBytes());
            gzip.finish();
            Base64 base64 = new Base64();
            compressedStr = base64.encodeToString(out.toByteArray());
        } catch (IOException e) {
            compressedStr = null;
        }
        return compressedStr;
    }

    /**
     * 使用gzip进行压缩
     */
    public static byte[] gzipToByte(String primStr) {
        if (StringUtils.isEmpty(primStr)) {
            return null;
        }

        byte[] compressedStr;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(primStr.getBytes());
            gzip.finish();
            Base64 base64 = new Base64();
            compressedStr = base64.encode(out.toByteArray());
        } catch (IOException e) {
            compressedStr = null;
        }
        return compressedStr;
    }

    /**
     * <p>Description:使用gzip进行解压缩</p>
     *
     * @param compressedStr
     * @return
     */
    public static String gunzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        String decompressed;
        Base64 base64 = new Base64();
        try (ByteArrayInputStream in = new ByteArrayInputStream(base64.decode(compressedStr));
             GZIPInputStream ginzip = new GZIPInputStream(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = new String(out.toByteArray());
        } catch (IOException e) {
            decompressed = null;
        }

        return decompressed;
    }

    /**
     * <p>Description:使用gzip进行解压缩</p>
     *
     * @param compressedStr
     * @return
     */
    public static String gunzipFromByte(byte[] compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        String decompressed;
        Base64 base64 = new Base64();
        try (ByteArrayInputStream in = new ByteArrayInputStream(base64.decode(compressedStr));
             GZIPInputStream ginzip = new GZIPInputStream(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = new String(out.toByteArray());
        } catch (IOException e) {
            decompressed = null;
        }

        return decompressed;
    }

    /**
     * 使用gzip进行压缩
     */
    public static byte[] gzipB(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return null;
        }

        byte[] compressedStr = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(primStr.getBytes());
            gzip.finish();
            compressedStr = out.toByteArray();
        } catch (IOException e) {
        }
        return compressedStr;
    }

    /**
     * <p>Description:使用gzip进行解压缩</p>
     *
     * @param compressed
     * @return
     */
    public static String gunzipB(byte[] compressed) {
        if (compressed == null) {
            return null;
        }

        String decompressed = null;
        try (ByteArrayInputStream in = new ByteArrayInputStream(compressed);
             GZIPInputStream ginzip = new GZIPInputStream(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decompressed;
    }

    /**
     * 使用zip进行压缩
     *
     * @param str 压缩前的文本
     * @return 返回压缩后的文本
     */
    public static String zip(String str) {
        if (str == null)
            return null;
        byte[] compressed;
        String compressedStr = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zout = new ZipOutputStream(out)) {
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes());
            zout.closeEntry();
            Base64 base64 = new Base64();
            compressedStr = base64.encodeToString(out.toByteArray());
        } catch (IOException e) {
            compressed = null;
        }
        return compressedStr;
    }

    /**
     * 使用zip进行解压缩
     *
     * @param compressedStr 压缩后的文本
     * @return 解压后的字符串
     */
    public static String unzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        byte[] compressed = null;
        String decompressed = null;
        Base64 base64 = new Base64();
        compressed = base64.decode(compressedStr);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(compressed);
             ZipInputStream zin = new ZipInputStream(in)) {
            zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = new String(out.toByteArray());
        } catch (IOException e) {
            decompressed = null;
        }
        return decompressed;
    }

    public static String base64Encode(byte[] input)
    {

        Base64 base64 = new Base64();
        return base64.encodeToString(input);
    }
}