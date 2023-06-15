package com.nabob.conch.tools.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 打码工具类
 */
public class MosaicUtils {

    private static int mask = 0x00ff;

    private static Cache<Integer, char[]> asteriskCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    private static char[] getAsterisks(int len, char asterisk) {
        int key = ((len) << 16) | asterisk;
        char[] asterisks = asteriskCache.getIfPresent(key);
        if (asterisks == null) {
            asterisks = buildAsterisks(key);
            asteriskCache.put(key, asterisks);
        }
        return asterisks;
    }

    private static char[] buildAsterisks(int lenAndAsterisk) {
        char asterisk = (char) (lenAndAsterisk & mask);
        int len = (lenAndAsterisk >> 16);
        char[] asterisks = new char[len];
        Arrays.fill(asterisks, asterisk);
        return asterisks;
    }

    public static String mosaic(String str, int start, int len, char asterisk) {
        return mosaic(str, start, getAsterisks(len, asterisk));
    }

    public static String mosaic(String str, int start, char[] asterisks) {
        char[] chars = str.toCharArray();
        System.arraycopy(asterisks, 0, chars, start, asterisks.length);
        return new String(chars);
    }

    public static String mosaic(String str, int head, int tail, char[] asterisks) {
        char[] src = str.toCharArray();
        int length = head + tail + asterisks.length;
        char[] chars = new char[length];
        System.arraycopy(src, 0, chars, 0, head);
        System.arraycopy(asterisks, 0, chars, head, asterisks.length);
        System.arraycopy(src, src.length - tail, chars, head + asterisks.length, tail);
        return new String(chars);
    }

    //region builds-------------------------------------------------------------
    public static DefaultMosaicBuild mosaicBuild() {
        return new DefaultMosaicBuild();
    }

    public interface Mosaic extends Function<String, String> {
        String mosaic(String str);

        @Override
        default String apply(String str) {
            return mosaic(str);
        }
    }

    public static interface MosaicBuild {
        MosaicBuild keep(int len);

        MosaicBuild keepTo(int index);

        MosaicBuild skip(int len);

        MosaicBuild skipTo(int index);

        MosaicBuild appendTail();

        MosaicBuild mosaic(String asterisks);

        MosaicBuild mosaic(char[] asterisks);

        MosaicBuild mosaic(int len, char asterisk);

        MosaicBuild mosaicAndSkip(String asterisks);

        MosaicBuild mosaicAndSkip(char[] asterisks);

        MosaicBuild mosaicAndSkip(int len, char asterisk);

        MosaicBuild onException(MosaicExceptionHandle exceptionHandle);

        MosaicBuild returnSrcOnError();

        MosaicBuild returnDefectOnError();

        MosaicBuild returnOnError(String defaultTxt);

        Mosaic build();
    }

    /**
     * 通常打码后的长度均不会超出原字符长度，故暂不支持打码后长度超出原字符长度
     */
    public static class DefaultMosaicBuild implements MosaicBuild {

        private List<MosaicAction> actions = new ArrayList<>(4);

        private int tail = -1;

        private int minLen = 0;

        private MosaicExceptionHandle exceptionHandle;

        public void addAction(SimpleMosaicAction simpleMosaicAction) {
            actions.add(simpleMosaicAction);
        }

        public SafetyMosaicBuild safety() {
            return new SafetyMosaicBuild(actions, tail, minLen, exceptionHandle);
        }

        /**
         * 保留位数
         */
        @Override
        public DefaultMosaicBuild keep(int len) {
            addAction((src, result, indexes) -> {
                System.arraycopy(src, indexes[0], result, indexes[1], len);
                indexes[0] = indexes[0] + len;
                indexes[1] = indexes[1] + len;
            });
            tail = -1;
            minLen += len;
            return this;
        }

        /**
         * 保留位数至
         * 负数从后往前算
         */
        @Override
        public DefaultMosaicBuild keepTo(int index) {
            if (index < 0) {
                addAction((src, result, indexes) -> {
                    int nextIndex = src.length + index;
                    int len = nextIndex - indexes[0];
                    System.arraycopy(src, indexes[0], result, indexes[1], len);
                    indexes[0] = nextIndex;
                    indexes[1] = indexes[1] + len;
                });
                tail = 0 - index;
            } else {
                addAction((src, result, indexes) -> {
                    int len = index - indexes[0];
                    System.arraycopy(src, indexes[0], result, indexes[1], len);
                    indexes[0] = index;
                    indexes[1] = indexes[1] + len;
                });
                tail = -1;
            }
            return this;
        }

        /**
         * 跳过多少位
         */
        @Override
        public DefaultMosaicBuild skip(int len) {
            addAction((src, result, indexes) -> indexes[0] = indexes[0] + len);
            tail = -1;
            return this;
        }

        /**
         * 跳到多少位
         * 负数从后往前算
         */
        @Override
        public DefaultMosaicBuild skipTo(int index) {

            if (index < 0) {
                addAction((src, result, indexes) -> {
                    int nextIndex = src.length + index;
                    indexes[0] = nextIndex;
                });
                tail = 0 - index;
            } else {
                addAction((src, result, indexes) -> indexes[0] = index);
                tail = -1;
            }
            return this;
        }

        /**
         * 加入剩余字符
         */
        @Override
        public DefaultMosaicBuild appendTail() {
            if (tail == -1) {
                addAction((src, result, indexes) -> {
                    int tailLen = src.length - indexes[0];
                    System.arraycopy(src, indexes[0], result, indexes[1], tailLen);
                    indexes[0] += tailLen;
                    indexes[1] += tailLen;
                });
            } else {
                final int tailLen = tail;
                addAction((src, result, indexes) -> {
                    System.arraycopy(src, indexes[0], result, indexes[1], tailLen);
                    indexes[0] += tailLen;
                    indexes[1] += tailLen;
                });
            }
            return this;
        }

        /**
         * 打码
         *
         * @param asterisks -打码
         */
        @Override
        public DefaultMosaicBuild mosaic(String asterisks) {
            return mosaic(asterisks.toCharArray());
        }

        /**
         * 打码
         *
         * @param asterisks -打码
         */
        @Override
        public DefaultMosaicBuild mosaic(char[] asterisks) {
            addAction((src, result, indexes) -> {
                System.arraycopy(asterisks, 0, result, indexes[1], asterisks.length);
                indexes[1] += asterisks.length;
            });
            minLen += asterisks.length;
            return this;
        }

        /**
         * 打码
         *
         * @param len      -位数
         * @param asterisk -打码字符
         */
        @Override
        public DefaultMosaicBuild mosaic(int len, char asterisk) {
            return mosaic(getAsterisks(len, asterisk));
        }

        /**
         * 打码并跳过相同位数
         *
         * @param asterisks -打码
         */
        @Override
        public DefaultMosaicBuild mosaicAndSkip(String asterisks) {
            return mosaic(asterisks.toCharArray());
        }

        /**
         * 打码并跳过相同位数
         *
         * @param asterisks -打码
         */
        @Override
        public DefaultMosaicBuild mosaicAndSkip(char[] asterisks) {
            addAction((src, result, indexes) -> {
                System.arraycopy(asterisks, 0, result, indexes[1], asterisks.length);
                indexes[0] += asterisks.length;
                indexes[1] += asterisks.length;
            });
            tail = -1;
            minLen += asterisks.length;
            return this;
        }

        /**
         * 打码并跳过相同位数
         *
         * @param len      -位数
         * @param asterisk -打码字符
         */
        @Override
        public DefaultMosaicBuild mosaicAndSkip(int len, char asterisk) {
            return mosaicAndSkip(getAsterisks(len, asterisk));
        }

        /**
         * 设置异常处理行为
         *
         * @param exceptionHandle -异常处理行为
         */
        @Override
        public DefaultMosaicBuild onException(MosaicExceptionHandle exceptionHandle) {
            this.exceptionHandle = exceptionHandle;
            return this;
        }

        /**
         * 异常时返回源字符串
         */
        @Override
        public DefaultMosaicBuild returnSrcOnError() {
            this.exceptionHandle = (throwable, src, defect) -> src;
            return this;
        }

        /**
         * 异常时返回已完成的打码字符串
         */
        @Override
        public DefaultMosaicBuild returnDefectOnError() {
            this.exceptionHandle = (throwable, src, defect) -> defect;
            return this;
        }

        /**
         * 异常时返回指定文本
         *
         * @param defaultTxt -异常时返回的文本
         */
        @Override
        public DefaultMosaicBuild returnOnError(String defaultTxt) {
            this.exceptionHandle = (throwable, src, defect) -> defaultTxt;
            return this;
        }

        @Override
        public Mosaic build() {
            return new ActionsMosaic(CollectionUtils.toArray(actions, MosaicAction.class), exceptionHandle, minLen);
        }
    }

    /**
     * 每次操作前都会检查长度
     */
    public static class SafetyMosaicBuild implements MosaicBuild {

        private List<MosaicAction> actions;

        private int tail;

        private int minLen;

        private MosaicExceptionHandle exceptionHandle;


        public void addAction(SafetyMosaicAction safetyMosaicAction) {
            actions.add(safetyMosaicAction);
        }

        public SafetyMosaicBuild() {
            this.actions = new ArrayList<>(4);
            this.tail = -1;
            this.minLen = 0;
        }

        public SafetyMosaicBuild(List<MosaicAction> actions, int tail, int minLen, MosaicExceptionHandle exceptionHandle) {
            this.actions = actions;
            this.tail = tail;
            this.minLen = minLen;
            this.exceptionHandle = exceptionHandle;
        }

        int safetyCopy(char[][] srcAndResult, char[] src, int srcPos,
                       char[] result, int resultPos,
                       int len) {
            if (src.length - srcPos - len < 0) {
                len = src.length - srcPos;
                if (len < 0) {
                    return 0;
                }
            }
            if (needGrowCapacity(srcAndResult, resultPos + len)) {
                result = srcAndResult[1];
            }
            System.arraycopy(src, srcPos, result, resultPos, len);
            return len;
        }

        /**
         * 保留位数
         */
        @Override
        public SafetyMosaicBuild keep(int len) {
            addAction((srcAndResult, src, result, indexes) -> {
                int iLen = safetyCopy(srcAndResult, src, indexes[0], result, indexes[1], len);
                indexes[0] = indexes[0] + iLen;
                indexes[1] = indexes[1] + iLen;
            });
            tail = -1;
            minLen += len;
            return this;
        }

        /**
         * 保留位数至
         * 负数从后往前算
         */
        @Override
        public SafetyMosaicBuild keepTo(int index) {
            if (index < 0) {
                addAction((srcAndResult, src, result, indexes) -> {
                    int nextIndex = src.length + index;
                    int len = nextIndex - indexes[0];
                    len = safetyCopy(srcAndResult, src, indexes[0], result, indexes[1], len);
                    indexes[0] = nextIndex;
                    indexes[1] = indexes[1] + len;
                });
                tail = 0 - index;
            } else {
                addAction((srcAndResult, src, result, indexes) -> {
                    int len = index - indexes[0];
                    len = safetyCopy(srcAndResult, src, indexes[0], result, indexes[1], len);
                    indexes[0] = index;
                    indexes[1] = indexes[1] + len;
                });
                tail = -1;
            }
            return this;
        }

        /**
         * 跳过多少位
         */
        @Override
        public SafetyMosaicBuild skip(int len) {
            addAction((srcAndResult, src, result, indexes) -> indexes[0] = indexes[0] + len);
            tail = -1;
            return this;
        }

        /**
         * 跳到多少位
         * 负数从后往前算
         */
        @Override
        public SafetyMosaicBuild skipTo(int index) {

            if (index < 0) {
                addAction((srcAndResult, src, result, indexes) -> {
                    int nextIndex = src.length + index;
                    indexes[0] = nextIndex;
                });
                tail = 0 - index;
            } else {
                addAction((srcAndResult, src, result, indexes) -> indexes[0] = index);
                tail = -1;
            }
            return this;
        }

        /**
         * 加入剩余字符
         */
        @Override
        public SafetyMosaicBuild appendTail() {
            if (tail == -1) {
                addAction((srcAndResult, src, result, indexes) -> {
                    int len = src.length - indexes[0];
                    len = safetyCopy(srcAndResult, src, indexes[0], result, indexes[1], len);
                    indexes[0] += len;
                    indexes[1] += len;
                });
            } else {
                final int tailLen = tail;
                addAction((srcAndResult, src, result, indexes) -> {
                    int len = safetyCopy(srcAndResult, src, indexes[0], result, indexes[1], tailLen);
                    indexes[0] += len;
                    indexes[1] += len;
                });
            }
            return this;
        }

        /**
         * 打码
         *
         * @param asterisks -打码
         */
        @Override
        public SafetyMosaicBuild mosaic(String asterisks) {
            return mosaic(asterisks.toCharArray());
        }

        /**
         * 打码
         *
         * @param asterisks -打码
         */
        @Override
        public SafetyMosaicBuild mosaic(char[] asterisks) {
            addAction((srcAndResult, src, result, indexes) -> {
                int len = safetyCopy(srcAndResult, asterisks, 0, result, indexes[1], asterisks.length);
                indexes[1] += len;
            });
            minLen += asterisks.length;
            return this;
        }

        /**
         * 打码
         *
         * @param len      -位数
         * @param asterisk -打码字符
         */
        @Override
        public SafetyMosaicBuild mosaic(int len, char asterisk) {
            return mosaic(getAsterisks(len, asterisk));
        }

        /**
         * 打码并跳过相同位数
         *
         * @param asterisks -打码
         */
        @Override
        public SafetyMosaicBuild mosaicAndSkip(String asterisks) {
            return mosaic(asterisks.toCharArray());
        }

        /**
         * 打码并跳过相同位数
         *
         * @param asterisks -打码
         */
        @Override
        public SafetyMosaicBuild mosaicAndSkip(char[] asterisks) {
            addAction((srcAndResult, src, result, indexes) -> {
                int len = safetyCopy(srcAndResult, asterisks, 0, result, indexes[1], asterisks.length);
                indexes[0] += len;
                indexes[1] += len;
            });
            tail = -1;
            minLen += asterisks.length;
            return this;
        }

        /**
         * 打码并跳过相同位数
         *
         * @param len      -位数
         * @param asterisk -打码字符
         */
        @Override
        public SafetyMosaicBuild mosaicAndSkip(int len, char asterisk) {
            return mosaicAndSkip(getAsterisks(len, asterisk));
        }

        /**
         * 设置异常处理行为
         *
         * @param exceptionHandle -异常处理行为
         */
        @Override
        public SafetyMosaicBuild onException(MosaicExceptionHandle exceptionHandle) {
            this.exceptionHandle = exceptionHandle;
            return this;
        }

        /**
         * 异常时返回源字符串
         */
        @Override
        public SafetyMosaicBuild returnSrcOnError() {
            this.exceptionHandle = (throwable, src, defect) -> src;
            return this;
        }

        /**
         * 异常时返回已完成的打码字符串
         */
        @Override
        public SafetyMosaicBuild returnDefectOnError() {
            this.exceptionHandle = (throwable, src, defect) -> defect;
            return this;
        }

        /**
         * 异常时返回指定文本
         *
         * @param defaultTxt -异常时返回的文本
         */
        @Override
        public SafetyMosaicBuild returnOnError(String defaultTxt) {
            this.exceptionHandle = (throwable, src, defect) -> defaultTxt;
            return this;
        }

        @Override
        public Mosaic build() {
            return new ActionsMosaic(CollectionUtils.toArray(actions, MosaicAction.class), exceptionHandle, minLen);
        }

        private boolean needGrowCapacity(char[][] srcAndResult, int minimumCapacity) {
            if (minimumCapacity > srcAndResult[1].length) {
                srcAndResult[1] = Arrays.copyOf(srcAndResult[1],
                        newCapacity(srcAndResult[1], minimumCapacity));
                return true;
            } else {
                return false;
            }
        }

        private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

        private int hugeCapacity(int minCapacity) {
            if (Integer.MAX_VALUE - minCapacity < 0) { // overflow
                throw new OutOfMemoryError();
            }
            return (minCapacity > MAX_ARRAY_SIZE)
                    ? minCapacity : MAX_ARRAY_SIZE;
        }

        private int newCapacity(char[] value, int minCapacity) {
            // overflow-conscious code
            int newCapacity = (value.length << 1) + 2;
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
                    ? hugeCapacity(minCapacity)
                    : newCapacity;
        }

    }

    private interface MosaicAction {
        void next(char[][] srcAndResult, int[] indexes);
    }

    private interface SimpleMosaicAction extends MosaicAction {
        @Override
        default void next(char[][] srcAndResult, int[] indexes) {
            next(srcAndResult[0], srcAndResult[1], indexes);
        }

        void next(char[] src, char[] result, int[] indexes);
    }

    private interface SafetyMosaicAction extends MosaicAction {
        @Override
        default void next(char[][] srcAndResult, int[] indexes) {
            next(srcAndResult, srcAndResult[0], srcAndResult[1], indexes);
        }

        void next(char[][] srcAndResult, char[] src, char[] result, int[] indexes);
    }

    private interface MosaicExceptionHandle {
        String onError(Throwable throwable, String src, String defect);
    }

    private static class ActionsMosaic implements Mosaic {
        private MosaicAction[] actions;

        private MosaicExceptionHandle exceptionHandle;

        private int minLen;

        public ActionsMosaic(MosaicAction[] actions, int minLen) {
            this.actions = actions;
            this.minLen = minLen;
        }

        public ActionsMosaic(MosaicAction[] actions, MosaicExceptionHandle exceptionHandle, int minLen) {
            this.actions = actions;
            this.exceptionHandle = exceptionHandle;
            this.minLen = minLen;
        }

        @Override
        public String mosaic(String str) {
            try {
                char[] src = str.toCharArray();
                char[][] srcAndResult = new char[][]{src, new char[Integer.max(src.length, minLen)]};
                int[] indexes = new int[]{0, 0};
                try {
                    for (MosaicAction action : actions) {
                        action.next(srcAndResult, indexes);
                    }
                } catch (Throwable throwable) {
                    if (exceptionHandle == null) {
                        throw throwable;
                    } else {
                        return exceptionHandle.onError(throwable, str, result(srcAndResult[1], indexes[1]));
                    }
                }
                return result(srcAndResult[1], indexes[1]);
            } catch (Throwable throwable) {
                if (exceptionHandle == null) {
                    throw throwable;
                } else {
                    return exceptionHandle.onError(throwable, str, null);
                }
            }
        }

        private String result(char[] result, int len) {
            if (len == result.length) {
                return new String(result);
            } else {
                return new String(result, 0, len);
            }

        }
    }

    //endregion builds-------------------------------------------------------------

    private static Mosaic idCardMosaic = mosaicBuild().keep(5).mosaic(4, '*').skipTo(-5).appendTail().returnDefectOnError().build();

    public static String idCardMosaic(String idCard) {
        return idCardMosaic.mosaic(idCard);
    }

    private static Mosaic phoneMosaic = mosaicBuild().keep(3).mosaicAndSkip(4, '*').appendTail().returnDefectOnError().build();

    public static String phoneMosaic(String phone) {
        return phoneMosaic.mosaic(phone);
    }

    private static Mosaic nameMosaic = mosaicBuild().keep(1).mosaic(2, '*').returnDefectOnError().build();

    public static String nameMosaic(String phone) {
        return nameMosaic.mosaic(phone);
    }

    private static Mosaic bankMosaic = mosaicBuild().skipTo(-4).appendTail().returnDefectOnError().build();

    public static String bankMosaic(String bankNum) {
        return bankMosaic.mosaic(bankNum);
    }

}
