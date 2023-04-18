package com.nabob.conch.tools.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public final class StringUtil {

    private StringUtil() {
    }

    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");


    public static String format(String format, Object... args) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
        return ft.getMessage();
    }

    public static String toString(Throwable e) {
        StringWriter w = new StringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName());
        if (e.getMessage() != null) {
            p.print(": " + e.getMessage());
        }
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }

    public static String toString(String msg, Throwable e) {
        StringWriter w = new StringWriter();
        w.write(msg + "\n");
        try (PrintWriter p = new PrintWriter(w)) {
            e.printStackTrace(p);
            return w.toString();
        }
    }

    public static String concat(String... strings) {
        if (strings == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (str != null) {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static boolean isInteger(String str) {
        return !(str == null || str.length() == 0) && INT_PATTERN.matcher(str).matches();
    }

    public static String toString(Object value) {
        return toString(value, null);
    }

    public static String toString(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    public static String[] splitWithTrim(String spilt, String sequence) {
        if (StringUtils.isEmpty(sequence)) {
            return null;
        }
        String[] values = sequence.split(spilt);
        if (values.length == 0) {
            return values;
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }
        return values;
    }

    /**
     * 需要转义的字符集
     */
    private static final Set<Character> fbsArr = new HashSet<>(Arrays.asList('\\', '$', '(', ')', '*', '+', '.', '[', ']', '?', '^', '{', '}', '|'));

    public static String escape(String str) {
        return escape(str, fbsArr);
    }

    /**
     * 处理转义字符
     */
    public static String escape(String str, Set<Character> fbsArr) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (fbsArr.contains(c)) {
                stringBuilder.append("\\");
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    /**
     * 为没有前缀的字符串加上前缀
     *
     * @param str    -字符串
     * @param prefix -前缀
     * @return -带上前缀的字符串
     */
    public static String addPrefix(String str, String prefix) {
        return str.startsWith(prefix) ? str : (prefix + str);
    }

    /**
     * 为没有后缀的字符串加上后缀
     *
     * @param str    -字符串
     * @param suffix -后缀
     * @return -带上前缀的字符串
     */
    public static String addSuffix(String str, String suffix) {
        return str.endsWith(suffix) ? str : (str + suffix);
    }

    /**
     * 为没有前缀的字符串加上前缀
     *
     * @param str    -字符串
     * @param prefix -前缀
     * @return -带上前缀的字符串
     */
    public static String addPrefix(String str, char prefix) {
        return str.indexOf(prefix) == 0 ? str : (prefix + str);
    }

    /**
     * 为没有前缀的字符串加上前缀
     *
     * @param bean   -目标,当且仅当为字符串时，才会加上前缀
     * @param prefix -前缀
     * @return -带上前缀的字符串
     */
    public static Object addPrefixIfStr(Object bean, String prefix) {
        if (bean == null)
            return bean;
        if (bean instanceof String) {
            String str = (String) bean;
            return str.startsWith(prefix) ? str : (prefix + str);
        } else {
            return bean;
        }
    }

    /**
     * 为没有前缀的字符串加上前缀
     *
     * @param bean   -目标,当且仅当为字符串时，才会加上前缀
     * @param prefix -前缀
     * @return -带上前缀的字符串
     */
    public static Object addPrefixIfStr(Object bean, char prefix) {
        if (bean == null)
            return bean;
        if (bean instanceof String) {
            String str = (String) bean;
            return str.indexOf(prefix) == 0 ? str : (prefix + str);
        } else {
            return bean;
        }
    }

    //region Deprecated-----------------------------
    @Deprecated
    public static boolean isNotEmpty(String... strings) {
        if (strings != null) {
            for (String s : strings) {
                if (StringUtils.isEmpty(s)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Deprecated
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    @Deprecated
    public static String replace(String text, String repl, String with, int max) {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(repl) || with == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(repl, start);
        if (end == -1) {
            return text;
        }
        int replLength = repl.length();
        int increase = with.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(repl, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    @Deprecated
    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    @Deprecated
    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static boolean isEmptyOrBlank(final Object bean) {
        return bean == null || StringUtils.isEmpty(bean.toString());
    }
    //endregion

    public static String emptyAs(String str, String as) {
        return ObjectUtils.predictAs(str, as, StringUtils::isEmpty);
    }

    public static String blankAs(String str, String as) {
        return ObjectUtils.predictAs(str, as, StringUtils::isBlank);
    }

}
