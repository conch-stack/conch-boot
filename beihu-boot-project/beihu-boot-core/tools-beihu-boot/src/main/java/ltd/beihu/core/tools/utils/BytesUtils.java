package ltd.beihu.core.tools.utils;

import ltd.beihu.core.tools.function.Holder;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BytesUtils {

    public static Long parse(String byteStr) {
        if (org.apache.commons.lang3.StringUtils.isBlank(byteStr)) {
            return 0L;
        }
        Holder<Long> holder = new Holder<>(0L);
        match(byteStr, (duration, unit) -> {
            holder.updateData(span -> span + (long) convert(duration, unit, ByteUnit.BYTE));
        });
        return holder.getData();
    }

    public static Long parse(String timeSpan, ByteUnit timeUnit) {
        return timeUnit.convert(parse(timeSpan), ByteUnit.BYTE);
    }

    public static Double precisionParse(String timeSpan) {
        return precisionParse(timeSpan, ByteUnit.BYTE);
    }

    public static Double precisionParse(String timeSpan, ByteUnit timeUnit) {
        if (org.apache.commons.lang3.StringUtils.isBlank(timeSpan)) {
            return 0d;
        }
        Holder<Double> holder = new Holder<Double>(0D);
        match(timeSpan, (duration, unit) -> {
            holder.updateData(span -> span + convert(duration, unit, timeUnit));
        });
        return holder.getData();
    }

    public static String pretty(long duration, ByteUnit unit) {

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

    private static void match(String timeSpan, BiConsumer<Double, ByteUnit> matcherConsumer) {
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)(\\D+|$)");
        Matcher matcher = pattern.matcher(timeSpan);
        while (matcher.find()) {
            String strDuration = matcher.group(1);
            double duration = Double.parseDouble(strDuration);
            String strUnit = matcher.group(3);
            ByteUnit unit = ByteUnit.BYTE;
            if (strUnit.length() > 0) {
                char charUnit = strUnit.charAt(0);
                if (Character.isUpperCase(charUnit)) {
                    duration *= 8;
                    unit = unitMaps.get(Character.toLowerCase(charUnit));
                } else {
                    unit = unitMaps.get(charUnit);
                }
                if (unit == null) {
                    throw new IllegalArgumentException("unsupported unit:[" + strUnit + "]");
                }
            }
            matcherConsumer.accept(duration, unit);
        }
    }

    private static Map<Character, ByteUnit> unitMaps = new HashMap<>();
    private static ByteUnit[] units = new ByteUnit[]{ByteUnit.BYTE, ByteUnit.KB, ByteUnit.MB,
            ByteUnit.GB, ByteUnit.TB, ByteUnit.PB, ByteUnit.EB};
    private static String[] unitStrs = new String[]{"b", "k", "m", "g", "t", "p", "e"};
    private static long[] carries = new long[]{1024L, 1024L, 1024L, 1024L, 1024L, 1024L};

    static {
        unitMaps.put('b', ByteUnit.BYTE);
        unitMaps.put('k', ByteUnit.KB);
        unitMaps.put('m', ByteUnit.MB);
        unitMaps.put('g', ByteUnit.GB);
        unitMaps.put('t', ByteUnit.TB);
        unitMaps.put('p', ByteUnit.PB);
        unitMaps.put('e', ByteUnit.EB);
    }

    public static double convert(double sourceDuration, ByteUnit sourceUnit, ByteUnit targetUnit) {
        long rate = targetUnit.convert(1, sourceUnit);
        if (rate > 0) {
            return sourceDuration * rate;
        } else {
            rate = sourceUnit.convert(1, targetUnit);
            return sourceDuration / rate;
        }
    }

    public static byte[] concat(byte[] arg1, byte[] arg2) {

        byte[] result = Arrays.copyOf(arg1, arg1.length + arg2.length);
        System.arraycopy(arg2, 0, result, arg1.length, arg2.length);

        return result;
    }

    public static byte[] concatAll(byte[]... args) {

        if (args.length == 0) {
            return new byte[]{};
        }
        if (args.length == 1) {
            return args[0];
        }

        byte[] cur = concat(args[0], args[1]);
        for (int i = 2; i < args.length; i++) {
            cur = concat(cur, args[i]);
        }
        return cur;
    }

    public static byte[][] split(byte[] source, int c) {

        if (source == null || source.length == 0) {
            return new byte[][]{};
        }

        List<byte[]> bytes = new ArrayList<byte[]>();
        int offset = 0;
        for (int i = 0; i <= source.length; i++) {

            if (i == source.length) {

                bytes.add(Arrays.copyOfRange(source, offset, i));
                break;
            }

            if (source[i] == c) {
                bytes.add(Arrays.copyOfRange(source, offset, i));
                offset = i + 1;
            }
        }
        return bytes.toArray(new byte[bytes.size()][]);
    }

    /**
     * Merge multiple {@code byte} arrays into one array
     *
     * @param firstArray       must not be {@literal null}
     * @param additionalArrays must not be {@literal null}
     * @return
     */
    public static byte[][] mergeArrays(byte[] firstArray, byte[]... additionalArrays) {

        Assert.notNull(firstArray, "first array must not be null");
        Assert.notNull(additionalArrays, "additional arrays must not be null");

        byte[][] result = new byte[additionalArrays.length + 1][];
        result[0] = firstArray;
        System.arraycopy(additionalArrays, 0, result, 1, additionalArrays.length);

        return result;
    }
}
