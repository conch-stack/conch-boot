package ltd.beihu.core.tools.utils;

import ltd.beihu.core.tools.time.FastDateFormat;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class TimeUtil {

    private static final ConcurrentMap<Pattern, String> regPatternMap = new ConcurrentHashMap<Pattern, String>();

    static {
        regPatternMap.put(Pattern.compile("^\\d{1,2}:\\d{1,2}:\\d{1,2}$"), "HH:mm:ss");
        regPatternMap.put(Pattern.compile("^\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$"), "HH:mm:ss.S");
        regPatternMap.put(Pattern.compile("^\\d{1,2}:\\d{1,2}:\\d{1,2} \\d{1,3}$"), "HH:mm:ss SSS");
    }

    public static Time convert(Long timestamp) {
        return new Time(timestamp);
    }

    public static Time parse(String date) {
        try {
            for (Map.Entry<Pattern, String> entry : regPatternMap.entrySet()) {
                boolean isMatch = entry.getKey().matcher(date).matches();
                if (isMatch) {
                    return new Time(FastDateFormat.getInstance(entry.getValue()).parse(date));
                }
            }
            return new Time(DateUtils.parse(date));
        } catch (ParseException e) {
            throw new IllegalArgumentException("can't support this pattern , date is " + date);
        }
    }

    public static void main(String[] args) {
        Time time = parse("2:12:8");
        System.out.printf(time.toString());
        time = parse("11:12:13 111");
        System.out.printf(time.toString());
        time = parse("11:12:13.2");
        System.out.printf(time.toString());
        time = parse("11:12:3.22");
        System.out.printf(time.toString());
    }

}
