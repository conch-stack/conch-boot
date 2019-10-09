package ltd.beihu.core.tools.cast;


import java.util.HashMap;
import java.util.Map;

public class CasterUtils {

    private final static Map<Class, Caster> casterMap = new HashMap<>();

    static {
        casterMap.put(Integer.class, new IntegerCaster());
        casterMap.put(Double.class, new DoubleCaster());
        casterMap.put(String.class, new StringCaster());
        casterMap.put(Long.class, new LongCaster());
    }

    public static Caster get(Class clazz) {
        return casterMap.get(clazz);
    }

    public static <T> T cast(Object o, Class<T> clazz) {
        if (o == null)
            return null;
        Caster caster = get(clazz);
        if (caster == null) {
            throw new IllegalArgumentException(String.format("can not case%s to %s", o.toString(), clazz.getSimpleName()));
        } else {
            return (T) caster.cast(o);
        }
    }

    public static <T> T cast(Object o, Class<T> clazz, T defaultVal) {
        if (o == null)
            return null;
        Caster caster = get(clazz);
        if (caster == null) {
            return defaultVal;
        } else {
            return (T) caster.cast(o);
        }
    }
}
