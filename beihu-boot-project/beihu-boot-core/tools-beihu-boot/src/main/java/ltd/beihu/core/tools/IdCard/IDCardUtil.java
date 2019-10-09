package ltd.beihu.core.tools.IdCard;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Project: [CommonUtil]
 * @Package: [com.pengshu.common]
 * @Description: [description]
 * @Author: [toming]
 * @CreateDate: [7/23/16 11:38 AM]
 * @Version: [v1.0]
 */
public class IDCardUtil {

    /**
     * 验证身份证信息
     */
    public static void chekIdCard(String idCard) throws InvalidException {
        buildIdCardInfo(idCard).checkInfo();
    }

    public static Integer getSex(String idCard) throws InvalidException {
        return buildIdCardInfo(idCard).getSex();
    }

    public static Date getBirthDate(String idCard) throws InvalidException {
        return buildIdCardInfo(idCard).getBirthDate();
    }

    public static IDCardInfo buildIdCardInfo(String idCard) throws InvalidException {
        if (StringUtils.isEmpty(idCard))
            throw new InvalidException("身份证号不可为空");
        if (idCard.length() == 18) {
            return new IDCardInfo18(idCard);
        } else if (idCard.length() == 15) {
            return new IDCardInfo15(idCard);
        } else {
            throw new InvalidException("身份证号码位数不符");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String idCard = "533527198909210238";
        try {
            chekIdCard(idCard);
            System.out.println("身份证合法");
        } catch (InvalidException e) {
            e.printStackTrace();
        }
    }

}
