package ltd.beihu.core.tools.IdCard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IDCardInfo18 extends AbstractIDCardInfo {
    public IDCardInfo18(String idCard) throws InvalidException {
        super(idCard);
        if (idCard.length() != 18) {
            throw new InvalidException("身份证号码位数不符");
        }
        if (idCard.charAt(17) == 'x') {
            this.idCard = idCard.substring(0, 17) + "X";
        }
    }

    public Integer getSex() {
        return Integer.parseInt(idCard.substring(14, 17)) % 2 == 0 ? MAN_SEX : WOMAN_SEX;
    }

    /**
     * 验证身份证号码数字字母组成是否符合规则
     */
    @Override
    protected void checkNumber() throws InvalidException {
        char[] chars = idCard.toCharArray();
        for (int i = 0; i < 18; i++) {
            if (i < 18 - 1) {
                if (chars[i] > '9')
                    throw new InvalidException("18位身份证号码中前17位不能出现字母");
            } else {
                if (chars[i] > '9' && chars[i] != 'X')
                    throw new InvalidException("18位身份证号码中最后一位只能是数字0~9或字母X");
            }
        }
    }

    @Override
    public Integer getBirthYear() {
        return Integer.parseInt(idCard.substring(6, 10));
    }

    @Override
    public Integer getBirthMonth() {
        return Integer.parseInt(idCard.substring(10, 12));
    }

    @Override
    public Integer getBirthDay() {
        return Integer.parseInt(idCard.substring(12, 14));
    }

    /**
     * 验证身份证号码出生日期年份是否符合规则
     */
    @Override
    protected void checkBirthYear() throws InvalidException {
        int year = Integer.parseInt(idCard.substring(6, 10));
        int yearNow = getCurrentYear();
        if (year < 1900 || year > yearNow)
            throw new InvalidException("18位的身份证号码年份须在1900~" + yearNow + "内");
    }

    /**
     * 返回当前年份
     */
    private int getCurrentYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return Integer.parseInt(format.format(new Date()));
    }

    /**
     * 验证18位身份证号码校验码是否符合规则
     */
    protected void checkCheckCode() throws InvalidException {
        int sum = 0;
        char[] chars = idCard.toCharArray();
        for (int i = 0; i < 17; i++) {
            sum = sum + ((chars[i] - '0') * WEIGHT[i]);
        }

        int checkCode = sum % 11;
        String sortCode = SORTCODES[checkCode];

        if (!sortCode.equals(String.valueOf(chars[17])))
            throw new InvalidException("身份中的校验码不正确");
    }
}