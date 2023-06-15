package com.nabob.conch.tools.cast;

import org.apache.commons.lang3.StringUtils;

/**
 * [转换为double]
 */
public class DoubleCaster implements Caster {
    @Override
    public Object cast(Object object) {
        if (object == null)
            return null;
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        } else {
            String str = String.valueOf(object);
            if (StringUtils.isBlank(str)) {
                return null;
            } else {
                return Double.valueOf(str.trim());
            }
        }
    }
}
