package com.nabob.conch.tools.cast;


import org.apache.commons.lang3.StringUtils;

/**
 * [转换为int]
 */
public class IntegerCaster implements Caster {
    @Override
    public Object cast(Object object) {
        if (object == null)
            return null;
        if (object instanceof Number) {
            return ((Number) object).intValue();
        } else {
            String str = String.valueOf(object);
            if (StringUtils.isBlank(str)) {
                return null;
            } else {
                return Integer.valueOf(str.trim());
            }
        }
    }
}
