package com.nabob.conch.web.utils;

import com.nabob.conch.tools.code.ServiceCode;
import com.nabob.conch.web.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
@SuppressWarnings("rawtypes")
public class Asserts {

    public static void isTrue(boolean expression, ServiceCode serviceError) {
        if (!expression) {
            throw new ServiceException(serviceError);
        }
    }

    public static void isFalse(boolean expression, ServiceCode serviceError) {
        if (expression) {
            throw new ServiceException(serviceError);
        }
    }

    public static void isNull(Object object, ServiceCode serviceError) {
        if (object != null) {
            throw new ServiceException(serviceError);
        }
    }

    public static void notNull(Object object, ServiceCode serviceError) {
        if (object == null) {
            throw new ServiceException(serviceError);
        }
    }

    public static void hasLength(String text, ServiceCode serviceError) {
        if (!StringUtils.isNotEmpty(text)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void hasText(String text, ServiceCode serviceError) {
        if (!StringUtils.isNotBlank(text)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void isEquals(Object one, Object another, ServiceCode serviceError) {
        if (!Objects.equals(one, another)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void notEquals(Object one, Object another, ServiceCode serviceError) {
        if (Objects.equals(one, another)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void doesNotContain(String textToSearch, String substring, ServiceCode serviceError) {
        if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
                textToSearch.contains(substring)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void notEmpty(Object[] array, ServiceCode serviceError) {
        if (array == null || array.length == 0) {
            throw new ServiceException(serviceError);
        }
    }

    public static void noNullElements(Object[] array, ServiceCode serviceError) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(serviceError);
                }
            }
        }
    }

    public static void notEmpty(Collection collection, ServiceCode serviceError) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void notEmpty(Map map, ServiceCode serviceError) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, ServiceCode serviceError) {
        notNull(type, serviceError);
        if (!type.isInstance(obj)) {
            throw new ServiceException(serviceError);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, ServiceCode serviceError) {
        notNull(superType, serviceError);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new ServiceException(serviceError);
        }
    }
}
