package ltd.beihu.core.tools.utils;

import java.util.function.Function;

/**
 * hash码工具类
 * TODO：使用getter setter方法
 */
public class HashHelper {

    public static String hashOf(Object bean) {
        return HASH_CODE_HASH_CALCULATOR.hashOf(bean);
    }

    private static final HashCodeHashCalculator HASH_CODE_HASH_CALCULATOR = new HashCodeHashCalculator();

    private static class HashCodeHashCalculator implements HashCalculator {

        @Override
        public String hashOf(Object bean) {
            return Md5Utils.md5(String.valueOf(bean.hashCode()));
        }
    }

    private static class SimpleHashCalculator implements HashCalculator {

        Function<Object, Object> fieldGetter;

        public SimpleHashCalculator(Function<Object, Object> fieldGetter) {
            this.fieldGetter = fieldGetter;
        }

        @Override
        public String hashOf(Object bean) {
            return Md5Utils.md5(String.valueOf(fieldGetter.apply(bean)));
        }
    }

    private static class MultiHashCalculator implements HashCalculator {

        Function<Object, Object>[] fieldGetter;

        public MultiHashCalculator(Function<Object, Object>[] fieldGetter) {
            this.fieldGetter = fieldGetter;
        }

        @Override
        public String hashOf(Object bean) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Function<Object, Object> objectObjectFunction : fieldGetter) {
                stringBuilder.append(String.valueOf(objectObjectFunction.apply(bean)));
            }
            return Md5Utils.md5(stringBuilder.toString());
        }
    }

    public interface HashCalculator {
        String hashOf(Object bean);
    }
}
