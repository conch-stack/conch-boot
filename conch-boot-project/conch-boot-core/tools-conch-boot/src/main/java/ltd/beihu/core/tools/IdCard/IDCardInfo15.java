package ltd.beihu.core.tools.IdCard;


public class IDCardInfo15 extends AbstractIDCardInfo {
    public IDCardInfo15(String idCard) throws InvalidException {
        super(idCard);
        if (idCard.length() != 15) {
            throw new InvalidException("身份证号码位数不符");
        }
    }

    @Override
    public Integer getSex() {
        return Integer.parseInt(idCard.substring(12, 15)) % 2 == 0 ? MAN_SEX : WOMAN_SEX;
    }

    /**
     * 验证身份证号码数字字母组成是否符合规则
     */
    @Override
    protected void checkNumber() throws InvalidException {
        char[] chars = idCard.toCharArray();
        for (char aChar : chars) {
            if (aChar > '9')
                throw new InvalidException("15位身份证号码中不能出现字母");
        }
    }


    /**
     * 验证身份证号码出生日期年份是否符合规则
     */
    @Override
    protected void checkBirthYear() throws InvalidException {
        int year = getBirthYear();
        if (year < 0 || year > 99)
            throw new InvalidException("15\"位的身份证号码年份须在00~99内");
    }

    @Override
    public Integer getBirthYear() {
        return Integer.parseInt("19" + idCard.substring(6, 8));
    }

    @Override
    public Integer getBirthMonth() {
        return Integer.parseInt(idCard.substring(8, 10));
    }

    @Override
    public Integer getBirthDay() {
        return Integer.parseInt(idCard.substring(10, 12));
    }

    /**
     * 15位身份证号码无校验码
     */
    protected void checkCheckCode() {
    }
}
