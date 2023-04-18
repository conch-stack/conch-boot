package com.nabob.conch.tools.utils;

import com.nabob.conch.tools.function.Holder;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tangming on 4/1/17.
 */
public class TimeSpanUtils {

    public static Long parse(String timeSpan) {
        if (org.apache.commons.lang3.StringUtils.isBlank(timeSpan)) {
            return 0L;
        }
        Holder<Long> holder = new Holder<>(0L);
        match(timeSpan, (duration, unit) -> {
            holder.updateData(span -> span + (long) convert(duration, unit, TimeUnit.MILLISECONDS));
        });
        return holder.getData();
    }

    public static Long parse(String timeSpan, TimeUnit timeUnit) {
        return timeUnit.convert(parse(timeSpan), TimeUnit.MILLISECONDS);
    }

    public static Double precisionParse(String timeSpan) {
        return precisionParse(timeSpan, TimeUnit.MILLISECONDS);
    }

    public static Double precisionParse(String timeSpan, TimeUnit timeUnit) {
        if (org.apache.commons.lang3.StringUtils.isBlank(timeSpan)) {
            return 0d;
        }
        Holder<Double> holder = new Holder<Double>(0D);
        match(timeSpan, (duration, unit) -> {
            holder.updateData(span -> span + convert(duration, unit, timeUnit));
        });
        return holder.getData();
    }

    public static String pretty(long duration, TimeUnit unit) {

        int index = ArrayUtils.indexOf(units, unit);
        StringBuilder stringBuilder = new StringBuilder();
        long carry = 0;
        long rest = 0;
        for (; index < units.length - 1; index++) {
            carry = duration / carries[index];
            rest = duration - (carry * carries[index]);
            if (rest > 0) {
                stringBuilder.insert(0, unitStrs[index])
                        .insert(0, rest);
            }
            if (carry == 0) {
                break;
            }
            duration = carry;
        }
        if (index == units.length - 1) {
            stringBuilder.insert(0, unitStrs[index])
                    .insert(0, duration);
        }
        if (stringBuilder.length() == 0) {
            return "0" + unitStrs[ArrayUtils.indexOf(units, unit)];
        }
        return stringBuilder.toString();

    }

    private static void match(String timeSpan, BiConsumer<Double, TimeUnit> matcherConsumer) {
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)(\\D+)");
        Matcher matcher = pattern.matcher(timeSpan);
        while (matcher.find()) {
            String strDuration = matcher.group(1);
            double duration = Double.parseDouble(strDuration);
            String strUnit = matcher.group(3);
            TimeUnit unit = unitMaps.get(strUnit);
            if (unit == null) {
                throw new IllegalArgumentException("unsupported unit:[" + strUnit + "]");
            }
            matcherConsumer.accept(duration, unit);
        }
    }

    private static Map<String, TimeUnit> unitMaps = new HashMap<>();
    private static TimeUnit[] units = new TimeUnit[]{TimeUnit.NANOSECONDS, TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS,
            TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS};
    private static String[] unitStrs = new String[]{"ns", "us", "ms", "s", "m", "h", "d"};
    private static long[] carries = new long[]{1000L, 1000L, 1000L, 60L, 60L, 24L};

    static {
        unitMaps.put("ns", TimeUnit.NANOSECONDS);
        unitMaps.put("us", TimeUnit.MICROSECONDS);
        unitMaps.put("ms", TimeUnit.MILLISECONDS);
        unitMaps.put("s", TimeUnit.SECONDS);
        unitMaps.put("m", TimeUnit.MINUTES);
        unitMaps.put("h", TimeUnit.HOURS);
        unitMaps.put("d", TimeUnit.DAYS);
    }

    public static double convert(double sourceDuration, TimeUnit sourceUnit, TimeUnit targetUnit) {
        long rate = targetUnit.convert(1, sourceUnit);
        if (rate > 0) {
            return sourceDuration * rate;
        } else {
            rate = sourceUnit.convert(1, targetUnit);
            return sourceDuration / rate;
        }
    }

    public static void main(String[] args) {
        String[] strings = new String[]{
                "10s", "1h10s", "0.2h", "200h", "20d20h", "700000s", "0.05d"
        };


        for (String s : strings) {
            for (TimeUnit unit : TimeUnit.values()) {
                long spans = TimeSpanUtils.parse(s, unit);
                System.out.printf("[%s,%s]-> parse:%d\tprecision:%s\n",
                        s, unit.name(), spans,
                        TimeSpanUtils.precisionParse(s, unit));
                System.out.printf("[%d %s] -> %s\n", spans, unit.name(), pretty(spans, unit));
            }
        }
    }
}
