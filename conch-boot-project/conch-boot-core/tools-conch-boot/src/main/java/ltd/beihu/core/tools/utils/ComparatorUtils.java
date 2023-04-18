package ltd.beihu.core.tools.utils;


public final class ComparatorUtils {
    /**
     * 获取较小值
     *
     * @param t1  -第一个元素
     * @param t2  - 第二个元素
     * @param <T> -元素类型
     * @return -最小的元素
     */
    public static <T extends Comparable<T>> T getMin(T t1, T t2) {
        if (t1 == null || t2 == null) {
            return null;
        } else {
            return t1.compareTo(t2) == 1 ? t2 : t1;
        }
    }

    /**
     * 获取较大值
     *
     * @param t1  -第一个元素
     * @param t2  - 第二个元素
     * @param <T> -元素类型
     * @return -最大的元素
     */
    public static <T extends Comparable<T>> T getMax(T t1, T t2) {
        if (t1 == null) {
            return t2;
        } else if (t2 == null) {
            return t1;
        } else {
            return t1.compareTo(t2) == 1 ? t1 : t2;
        }
    }
}
