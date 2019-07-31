package ltd.beihu.core.tools.cast;


import org.apache.commons.lang3.StringUtils;

/**
 *[转换为int]
 */
public class LongCaster implements Caster {
    @Override
    public Object cast(Object object) {
        if (object == null)
            return null;
        if (object instanceof Number) {
            return ((Number) object).longValue();
        } else {
            String str = String.valueOf(object);
            if (StringUtils.isBlank(str)) {
                return null;
            } else {
                return Long.valueOf(str.trim());
            }
        }
    }
}
