package ltd.beihu.core.tools.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static final String REGEX_MOBILE = "^1[0-9]\\d{9}$";

    public static final String DIGIT = "^\\d+$";

    public static final String DECIMAL = "^\\d+(\\.\\d+)?$";

    public static boolean isMobile(String mobile) {
        return check(REGEX_MOBILE, mobile);
    }

    public static boolean isDigit(String digit) {
        return check(DIGIT, digit);
    }

    public static boolean isDecimal(String decimal) {
        return check(DECIMAL, decimal);
    }

    private static boolean check(String pattern, String str) {
        return !StringUtils.isEmpty(str) && Pattern.matches(pattern, str);
    }
}
