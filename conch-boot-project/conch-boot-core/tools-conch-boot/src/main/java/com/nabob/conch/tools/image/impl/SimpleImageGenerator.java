package com.nabob.conch.tools.image.impl;

import com.nabob.conch.tools.image.Code;
import com.nabob.conch.tools.image.ImageGenerator;
import org.apache.commons.lang3.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 简单图片验证码生成实现
 *
 * @author layker
 * @date 2018/4/24 下午6:54
 */
public class SimpleImageGenerator implements ImageGenerator {

    static final char[] codes = {
            '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 宽度
     */
    private static final Integer W = 65;
    /**
     * 高度
     */
    private static final Integer H = 20;

    /**
     * 长度
     */
    private static final Integer LEN = 4;
    /**
     * 随机种子
     */
    private static Random random = new Random();


    private BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

    private static final String Content_Type = "data:image/jpg;base64,";

    /**
     * 生成图片验证码，默认参数
     *
     * @return 图片验证码base64字符串
     */
    @Override
    public Code generate() {
        Code code = new Code();
        // 在内存中创建图象
        BufferedImage image = new BufferedImage(W, H,
                BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(CodeUtil.getRandColor(230, 255));
        g.fillRect(0, 0, 100, 25);
        // 设定字体
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 18));
        // 产生0条干扰线，
        g.drawLine(0, 0, 0, 0);
        // 取随机产生的认证码

        String sRand = CodeUtil.getRandomCode(LEN);
        for (int i = 0; i < sRand.length(); i++) {
            // 将认证码显示到图象中
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.setColor(CodeUtil.getRandColor(100, 150));
            g.drawString(String.valueOf(sRand.charAt(i)), 15 * i + 6, 16);
        }
        code.setCode(sRand);
        for (int i = 0; i < (random.nextInt(5) + 5); i++) {
            g.setColor(new Color(random.nextInt(255) + 1, random.nextInt(255) + 1, random.nextInt(255) + 1));
            g.drawLine(random.nextInt(100), random.nextInt(30), random.nextInt(100), random.nextInt(30));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
            String encodeToString = Base64.getEncoder().encodeToString(baos.toByteArray());
            code.setImgBase64Str(Content_Type + encodeToString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 生成图片验证码
     *
     * @param w   宽度
     * @param h   高度
     * @param len 验证码字符长度
     * @return 验证码图片字符串
     */
    @Override
    public Code generate(int w, int h, int len) {
        Code code = new Code();
        BufferedImage image = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        String sRand = CodeUtil.getRandomCode(len);

        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(CodeUtil.getRandColor(230, 255));
        g.fillRect(0, 0, 100, 25);
        // 设定字体
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 18));
        // 产生0条干扰线，
        g.drawLine(0, 0, 0, 0);
        // 取随机产生的认证码

        for (int i = 0; i < sRand.length(); i++) {
            // 将认证码显示到图象中
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.setColor(CodeUtil.getRandColor(100, 150));
            g.drawString(String.valueOf(sRand.charAt(i)), 15 * i + 6, 16);
        }
        code.setCode(sRand);
        for (int i = 0; i < (random.nextInt(5) + 5); i++) {
            g.setColor(new Color(random.nextInt(255) + 1, random.nextInt(255) + 1, random.nextInt(255) + 1));
            g.drawLine(random.nextInt(100), random.nextInt(30), random.nextInt(100), random.nextInt(30));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
            String encodeToString = Base64.getEncoder().encodeToString(baos.toByteArray());
            code.setImgBase64Str(Content_Type + encodeToString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    static class CodeUtil {
        public static String getRandomCode(int length) {
            StringBuilder sb = new StringBuilder();
            while (sb.length() < length) {
                sb.append(codes[RandomUtils.nextInt(0, codes.length)]);
            }
            return sb.toString();
        }

        public static Color getRandColor(int fc, int bc) {
            if (fc > 255)
                fc = 255;
            if (bc > 255)
                bc = 255;
            int r = fc + RandomUtils.nextInt(0, bc - fc);
            int g = fc + RandomUtils.nextInt(0, bc - fc);
            int b = fc + RandomUtils.nextInt(0, bc - fc);
            return new Color(r, g, b);
        }
    }


    public static void main(String[] args) {

        SimpleImageGenerator simpleImageGenerator = new SimpleImageGenerator();
        long start = System.currentTimeMillis();
        System.out.println("start");
        Code generate = simpleImageGenerator.generate(W, H, 4);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(generate);
    }
}
