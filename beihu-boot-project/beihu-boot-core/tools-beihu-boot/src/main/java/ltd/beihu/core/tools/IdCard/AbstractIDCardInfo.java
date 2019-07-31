package ltd.beihu.core.tools.IdCard;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: [CommonUtil]
 * @Package: [com.pengshu.common.IdCard]
 * @Description: [description]
 * @Author: [toming]
 * @CreateDate: [7/23/16 1:38 PM]
 * @Version: [v1.0]
 */
public abstract class AbstractIDCardInfo implements IDCardInfo {

    public static final int MAX_MAINLAND_AREACODE = 659004;    //大陆地区地域编码最大值
    public static final int MIN_MAINLAND_AREACODE = 110000;    //大陆地区地域编码最小值
    public static final int HONGKONG_AREACODE = 810000;    //香港地域编码值
    public static final int TAIWAN_AREACODE = 710000;    //台湾地域编码值
    public static final int MACAO_AREACODE = 820000;    //澳门地域编码值

    public static final int MAN_SEX = 1;    //标识男性
    public static final int WOMAN_SEX = 2;    //标识女性

    final static Map<Integer, String> zoneNum = new HashMap<Integer, String>();

    static {
        zoneNum.put(11, "北京");
        zoneNum.put(12, "天津");
        zoneNum.put(13, "河北");
        zoneNum.put(14, "山西");
        zoneNum.put(15, "内蒙古");
        zoneNum.put(21, "辽宁");
        zoneNum.put(22, "吉林");
        zoneNum.put(23, "黑龙江");
        zoneNum.put(31, "上海");
        zoneNum.put(32, "江苏");
        zoneNum.put(33, "浙江");
        zoneNum.put(34, "安徽");
        zoneNum.put(35, "福建");
        zoneNum.put(36, "江西");
        zoneNum.put(37, "山东");
        zoneNum.put(41, "河南");
        zoneNum.put(42, "湖北");
        zoneNum.put(43, "湖南");
        zoneNum.put(44, "广东");
        zoneNum.put(45, "广西");
        zoneNum.put(46, "海南");
        zoneNum.put(50, "重庆");
        zoneNum.put(51, "四川");
        zoneNum.put(52, "贵州");
        zoneNum.put(53, "云南");
        zoneNum.put(54, "西藏");
        zoneNum.put(61, "陕西");
        zoneNum.put(62, "甘肃");
        zoneNum.put(63, "青海");
        zoneNum.put(64, "新疆");
        zoneNum.put(71, "台湾");
        zoneNum.put(81, "香港");
        zoneNum.put(82, "澳门");
        zoneNum.put(91, "外国");
    }

    @Override
    public String getZone() {
        return zoneNum.get(Integer.parseInt(idCard.substring(0, 2)));
    }

    //权重值
    protected static final Integer[] WEIGHT = new Integer[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    //储存18位身份证校验码
    protected static final String[] SORTCODES = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};


    protected String idCard;

    public AbstractIDCardInfo(String idCard) throws InvalidException {
        if (StringUtils.isEmpty(idCard))
            throw new InvalidException("身份证号不可为空");
        this.idCard = idCard;
    }

    @Override
    public String getIdCard() {
        return idCard;
    }

    @Override
    public void checkSex(Integer sex) throws InvalidException {
        if (!java.util.Objects.equals(getSex(), sex)) {
            throw new InvalidException("性别有误");
        }
    }

    @Override
    public void checkInfo() throws InvalidException {
        checkNumber();
        checkArea();
        checkBirthDate();
        checkCheckCode();
    }

    protected abstract void checkCheckCode() throws InvalidException;

    @Override
    public Date getBirthDate() {
        return new Calendar.Builder().setDate(getBirthYear(), getBirthMonth(), getBirthDay()).build().getTime();
    }

    protected void checkBirthDate() throws InvalidException {
        checkBirthYear();
        checkBirthMonth();
        checkBirthDay();
    }

    protected abstract void checkBirthYear() throws InvalidException;

    protected void checkBirthMonth() throws InvalidException {
        Integer month = getBirthMonth();
        if (month < 1 || month > 12)
            throw new InvalidException("身份证号码月份须在01~12内");
    }

    /**
     * 验证身份证号码出生日期天数是否符合规则
     */
    protected void checkBirthDay() throws InvalidException {
        Integer year = getBirthYear();
        Integer month = getBirthMonth();
        Integer day = getBirthDay();
        boolean bissextile = false;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            bissextile = true;

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (day < 1 || day > 31)
                    throw new InvalidException("身份证号码大月日期须在1~31之间");
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day < 1 || day > 30)
                    throw new InvalidException("身份证号码小月日期须在1~30之间");
                break;
            case 2:
                if (bissextile) {
                    if (day < 1 || day > 29)
                        throw new InvalidException("身份证号码闰年2月日期须在1~29之间");
                } else {
                    if (day < 1 || day > 28)
                        throw new InvalidException("身份证号码非闰年2月日期年份须在1~28之间");
                }
                break;
        }
    }

    /**
     * 验证身份证的地域编码是符合规则
     */
    protected void checkArea() throws InvalidException {
        String subStr = idCard.substring(0, 6);
        int areaCode = Integer.parseInt(subStr);
        if (areaCode != HONGKONG_AREACODE && areaCode != TAIWAN_AREACODE
                && areaCode != MACAO_AREACODE
                && (areaCode > MAX_MAINLAND_AREACODE || areaCode < MIN_MAINLAND_AREACODE))
            throw new InvalidException("输入的身份证号码地域编码不符合大陆和港澳台规则");
    }

    /**
     * 验证身份证号码数字字母组成是否符合规则
     */
    protected abstract void checkNumber() throws InvalidException;
}
