package com.nabob.conch.tools.image;

/**
 * 验证码图片生产器接口
 *
 * @author layker
 * @date 2018/4/24 下午6:52
 */
public interface ImageGenerator {

    /**
     * 生成图片验证码，默认参数 w=45,h=20,len=4,
     *
     * @return 图片验证码base64字符串
     */
    Code generate();

    Code generate(int w, int h, int len);
}
