package ltd.beihu.core.tools.image.impl;

import ltd.beihu.core.tools.image.Code;
import ltd.beihu.core.tools.image.ImageGenerator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author layker
 * @date 2018/7/10 下午7:22
 */
public class VerifyCodeGenerator implements ImageGenerator {
    /**
     * 宽度
     */
    private static final Integer W = 150;
    /**
     * 高度
     */
    private static final Integer H = 40;

    /**
     * 长度
     */
    private static final Integer LEN = 4;

    private BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

    private static final String CONTENT_TYPE = "data:image/jpg;base64,";

    /**
     * 生成图片验证码，默认参数 w=45,h=20,len=4,
     *
     * @return 图片验证码base64字符串
     */
    @Override
    public Code generate() {
        return generate(W, H, LEN);
    }

    @Override
    public Code generate(int w, int h, int len) {
        String verifyCode = VerifyCodeUtils.generateVerifyCode(len);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            VerifyCodeUtils.outputImage(w, h, baos, verifyCode);
        } catch (IOException e) {
            try {
                VerifyCodeUtils.outputImage(w, h, baos, verifyCode);
            } catch (IOException e1) {
                throw new IllegalStateException(e1);
            }
        }
        String imageBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        Code code = new Code();
        code.setCode(verifyCode);
        code.setImgBase64Str(CONTENT_TYPE + imageBase64);
        return code;
    }
}
