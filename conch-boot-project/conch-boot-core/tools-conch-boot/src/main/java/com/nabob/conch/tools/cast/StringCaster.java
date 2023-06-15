package com.nabob.conch.tools.cast;

import java.math.BigDecimal;

/**
 * [转换为字符串]
 */
public class StringCaster implements Caster {
    @Override
    public Object cast(Object object) {
        if (object == null)
            return null;
        if (object instanceof Number) {
            //防止java用科学计数法
            return new BigDecimal(String.valueOf(object)).toPlainString();
        } else {
            return String.valueOf(object).trim();
        }
    }
}
