package com.nabob.conch.tools.utils;

import java.lang.reflect.*;
import java.util.*;

public class ClassHelper {

    public static boolean isArrayOrCollection(Object bean) {
        return bean != null && (bean instanceof Collection || bean.getClass().isArray());
    }

    public static boolean isArray(Object bean) {
        return bean != null && bean.getClass().isArray();
    }

    public static boolean isCollection(Object bean) {
        return bean != null && bean instanceof Collection;
    }

    public static Class<?> forNameWithThreadContextClassLoader(String name)
            throws ClassNotFoundException {
        return forName(name, Thread.currentThread().getContextClassLoader());
    }

    public static Class<?> forNameWithCallerClassLoader(String name, Class<?> caller)
            throws ClassNotFoundException {
        return forName(name, caller.getClassLoader());
    }

    public static ClassLoader getCallerClassLoader(Class<?> caller) {
        return caller.getClassLoader();
    }

    /**
     * get class loader
     *
     * @param cls
     * @return class loader
     */
    public static ClassLoader getClassLoader(Class<?> cls) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = cls.getClassLoader();
        }
        return cl;
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p/>
     * Call this method if you intend to use the thread context ClassLoader in a
     * scenario where you absolutely need a non-null ClassLoader reference: for
     * example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
     * reference as well).
     *
     * @return the default ClassLoader (never <code>null</code>)
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassHelper.class);
    }

    /**
     * Same as <code>Class.forName()</code>, except that it works for primitive
     * types.
     */
    public static Class<?> forName(String name) throws ClassNotFoundException {
        return forName(name, getClassLoader());
    }

    /**
     * Replacement for <code>Class.forName()</code> that also returns Class
     * instances for primitives (like "int") and array class names (like
     * "String[]").
     *
     * @param name        the name of the Class
     * @param classLoader the class loader to use (may be <code>null</code>,
     *                    which indicates the default class loader)
     * @return Class instance for the supplied name
     * @throws ClassNotFoundException if the class was not found
     * @throws LinkageError           if the class file could not be loaded
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static Class<?> forName(String name, ClassLoader classLoader)
            throws ClassNotFoundException, LinkageError {

        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz != null) {
            return clazz;
        }

        // "java.lang.String[]" style arrays
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[Ljava.lang.String;" style arrays
        int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
        if (internalArrayMarker != -1 && name.endsWith(";")) {
            String elementClassName = null;
            if (internalArrayMarker == 0) {
                elementClassName = name
                        .substring(INTERNAL_ARRAY_PREFIX.length(), name.length() - 1);
            } else if (name.startsWith("[")) {
                elementClassName = name.substring(1);
            }
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = getClassLoader();
        }
        String tempName = name;
        int lastDot;
        do {
            try {
                return classLoaderToUse.loadClass(tempName);
            } catch (ClassNotFoundException e2) {
                lastDot = tempName.lastIndexOf('.');
                if (lastDot > 0) {
                    tempName = tempName.substring(0, lastDot) + "$" + tempName.substring(lastDot + 1);
                }
            }
        } while (lastDot > 0);
        throw new ClassNotFoundException("not found:[" + name + "]");
    }

    public static Class<?> forName(String[] packages, String className) {
        try {
            return _forName(className);
        } catch (ClassNotFoundException e) {
            if (packages != null && packages.length > 0) {
                for (String pkg : packages) {
                    try {
                        return _forName(pkg + "." + className);
                    } catch (ClassNotFoundException e2) {
                    }
                }
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static Class<?> _forName(String className) throws ClassNotFoundException {
        if ("boolean".equals(className))
            return boolean.class;
        if ("byte".equals(className))
            return byte.class;
        if ("char".equals(className))
            return char.class;
        if ("short".equals(className))
            return short.class;
        if ("int".equals(className))
            return int.class;
        if ("long".equals(className))
            return long.class;
        if ("float".equals(className))
            return float.class;
        if ("double".equals(className))
            return double.class;
        if ("boolean[]".equals(className))
            return boolean[].class;
        if ("byte[]".equals(className))
            return byte[].class;
        if ("char[]".equals(className))
            return char[].class;
        if ("short[]".equals(className))
            return short[].class;
        if ("int[]".equals(className))
            return int[].class;
        if ("long[]".equals(className))
            return long[].class;
        if ("float[]".equals(className))
            return float[].class;
        if ("double[]".equals(className))
            return double[].class;
        try {
            return arrayForName(className);
        } catch (ClassNotFoundException e) {
            if (className.indexOf('.') == -1) { // 尝试java.lang包
                try {
                    return arrayForName("java.lang." + className);
                } catch (ClassNotFoundException e2) {
                    // 忽略尝试异常, 抛出原始异常
                }
            }
            throw e;
        }
    }

    private static Class<?> arrayForName(String className) throws ClassNotFoundException {
        return Class.forName(className.endsWith("[]")
                ? "[L" + className.substring(0, className.length() - 2) + ";"
                : className, true, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Resolve the given class name as primitive class, if appropriate,
     * according to the JVM's naming rules for primitive classes.
     * <p/>
     * Also supports the JVM's internal class names for primitive arrays. Does
     * <i>not</i> support the "[]" suffix notation for primitive arrays; this is
     * only supported by {@link #forName}.
     *
     * @param name the name of the potentially primitive class
     * @return the primitive class, or <code>null</code> if the name does not
     * denote a primitive class or primitive array class
     */
    public static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;
        // Most class names will be quite long, considering that they
        // SHOULD sit in a package, so a length check is worthwhile.
        if (name != null && name.length() <= 8) {
            // Could be a primitive - likely.
            result = primitiveTypeNameMap.get(name);
        }
        return result;
    }

    /**
     * Suffix for array class names: "[]"
     */
    public static final String ARRAY_SUFFIX = "[]";
    /**
     * Prefix for internal array class names: "[L"
     */
    private static final String INTERNAL_ARRAY_PREFIX = "[L";

    /**
     * Map with primitive type name as key and corresponding primitive type as
     * value, for example: "int" -> "int.class".
     */
    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(16);

    /**
     * Map with primitive wrapper type as key and corresponding primitive type
     * as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveTypeMap = new HashMap<Class<?>, Class<?>>(8);
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);
    private static final Map<Class<?>, Object> primitiveTypeDftValueMap = new HashMap<Class<?>, Object>(8);


    static {
        wrapperPrimitiveTypeMap.put(Boolean.class, boolean.class);
        wrapperPrimitiveTypeMap.put(Byte.class, byte.class);
        wrapperPrimitiveTypeMap.put(Character.class, char.class);
        wrapperPrimitiveTypeMap.put(Double.class, double.class);
        wrapperPrimitiveTypeMap.put(Float.class, float.class);
        wrapperPrimitiveTypeMap.put(Integer.class, int.class);
        wrapperPrimitiveTypeMap.put(Long.class, long.class);
        wrapperPrimitiveTypeMap.put(Short.class, short.class);

        primitiveTypeDftValueMap.put(boolean.class, false);
        primitiveTypeDftValueMap.put(byte.class, 0);
        primitiveTypeDftValueMap.put(char.class, 0);
        primitiveTypeDftValueMap.put(double.class, 0);
        primitiveTypeDftValueMap.put(float.class, 0);
        primitiveTypeDftValueMap.put(int.class, 0);
        primitiveTypeDftValueMap.put(long.class, 0);
        primitiveTypeDftValueMap.put(short.class, 0);

        for (Map.Entry<Class<?>, Class<?>> entry : wrapperPrimitiveTypeMap.entrySet()) {
            primitiveWrapperTypeMap.put(entry.getValue(), entry.getKey());
        }

        Set<Class<?>> primitiveTypeNames = new HashSet<Class<?>>(16);
        primitiveTypeNames.addAll(wrapperPrimitiveTypeMap.values());
        primitiveTypeNames.addAll(Arrays
                .asList(boolean[].class, byte[].class, char[].class, double[].class,
                        float[].class, int[].class, long[].class, short[].class));
        for (Iterator<Class<?>> it = primitiveTypeNames.iterator(); it.hasNext(); ) {
            Class<?> primitiveClass = it.next();
            primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
        }
    }

    public static boolean isPrimitiveType(Class<?> clazz) {
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

    public static Class<?> getPrimitiveTypeByWrapper(Class<?> clazz) {
        return wrapperPrimitiveTypeMap.get(clazz);
    }

    public static Class<?> getWrapperTypeByPrimitive(Class<?> clazz) {
        return primitiveWrapperTypeMap.get(clazz);
    }

    public static boolean isPrimitiveWrapperType(Class<?> clazz) {
        return wrapperPrimitiveTypeMap.containsKey(clazz);
    }

    public static Object getPrimitiveDftValue(Class<?> clazz) {
        return primitiveTypeDftValueMap.get(clazz);
    }

    public static boolean isSimpleType(Class<?> clazz) {
        return isPrimitiveType(clazz) || isPrimitiveWrapperType(clazz);
    }

    /**
     * 获取类型的默认值
     */
    public static Object getDefaultValue(Class cl) {
        if (cl.isArray()) {
            return Array.newInstance(cl.getComponentType(), 0);
        } else if (cl.isPrimitive() || primitiveWrapperTypeMap.containsKey(cl)) {
            return primitiveTypeDftValueMap.get(cl);
        } else if (wrapperPrimitiveTypeMap.containsKey(cl)) {
            return primitiveTypeDftValueMap.get(wrapperPrimitiveTypeMap.get(cl));
        } else {
            return null;
        }
    }

    public static String toShortString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getSimpleName() + "@" + System.identityHashCode(obj);
    }

    private static Class[] EMPTY_CLASS_ARRAY = new Class[0];

    public static Object newInstance(Class type, Object... params) {
        Constructor constructor = null;
        try {
            if (CollectionUtils.isEmpty(params)) {
                constructor = type.getConstructor(EMPTY_CLASS_ARRAY);
            } else {
                constructor = type.getConstructor(CollectionUtils.map(params, p -> p == null ? Object.class : p.getClass(), Class[]::new));
            }
        } catch (NoSuchMethodException e) {
            // ignore
        }

        if (constructor == null) {
            Constructor[] constructors = type.getConstructors();
            if (constructors == null || constructors.length == 0) {
                throw new IllegalArgumentException("Illegal params to instance Class[" + type.getName() + "]");
            }
            for (Constructor cst : constructors) {
                Class[] paramCls = cst.getParameterTypes();
                if (isAssignable(params, paramCls)) {
                    constructor = cst;
                    break;
                }
            }
        }
        if (constructor == null) {
            throw new IllegalArgumentException("Illegal params to instance Class[" + type.getName() + "]");
        }
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Illegal params to instance Class[" + type.getName() + "]");
        }
    }

    public static boolean isAssignable(Class cls, Class toClass) {
        if (toClass == null)
            return false;
        if (cls == null)
            return !toClass.isPrimitive();
        if (cls.equals(toClass))
            return true;
        if (isPrimitiveWrapperType(cls)) {
            cls = getPrimitiveTypeByWrapper(cls);
            if (cls.equals(toClass)) {
                return true;
            }
        }
        if (cls.isPrimitive()) {
            if (isPrimitiveWrapperType(toClass)) {
                toClass = getPrimitiveTypeByWrapper(toClass);
                if (cls.equals(toClass)) {
                    return true;
                }
            } else if (!isPrimitiveWrapperType(toClass))
                return false;
            if (Integer.TYPE.equals(cls))
                return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            if (Long.TYPE.equals(cls))
                return Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            if (Boolean.TYPE.equals(cls))
                return false;
            if (Double.TYPE.equals(cls))
                return Float.TYPE.equals(toClass);
            if (Float.TYPE.equals(cls))
                return Double.TYPE.equals(toClass);
            if (Character.TYPE.equals(cls))
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            if (Short.TYPE.equals(cls))
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            if (Byte.TYPE.equals(cls))
                return Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            return false;
        } else {
            return toClass.isAssignableFrom(cls);
        }
    }

    public static boolean isAssignable(Object bean, Class cls) {
        return isAssignable(bean == null ? null : bean.getClass(), cls);
    }

    public static boolean isAssignable(Class classArray[], Class toClassArray[]) {
        if (classArray == null)
            classArray = EMPTY_CLASS_ARRAY;
        if (toClassArray == null)
            toClassArray = EMPTY_CLASS_ARRAY;
        if (classArray.length != toClassArray.length)
            return false;
        for (int i = 0; i < classArray.length; i++)
            if (!isAssignable(classArray[i], toClassArray[i]))
                return false;

        return true;
    }

    public static boolean isAssignable(Object[] objectArray, Class[] classArray) {
        return isAssignable(
                Arrays.stream(objectArray)
                        .map(bean -> bean == null ? null : bean.getClass()).toArray(Class[]::new)
                , classArray);
    }

    public static boolean isAssignableFrom(Class original, Class... checkedClasses) {
        if (checkedClasses == null)
            return false;
        for (Class checkedClass : checkedClasses)
            if (original.isAssignableFrom(checkedClass))
                return true;

        return false;
    }

    public static Object invokeMethod(Object bean, String methodName, Object... args) throws NullPointerException {
        Assert.notNull(bean, "can not invoke method:["
                + methodName + "] in null!");
        return invokeMethod(bean.getClass(), bean, methodName, args);
    }

    public static Object invokeMethod(Class beanClass, Object bean, String methodName, Object... args) throws NullPointerException {
        Assert.notNull(bean, "can not invoke method:["
                + methodName + "] in null!");
        Assert.notNull(beanClass, "can not invoke method:["
                + methodName + "] in null!");
        Object result;
        Exception ex = null;
        Class clz = beanClass;
        while (clz != null) {
            for (Method m : clz.getDeclaredMethods()) {
                if (m.getName().equals(methodName)) {
                    if (isAssignable(args, m.getParameterTypes())) {
                        try {
                            boolean accessible = m.isAccessible();
                            if (!accessible)
                                m.setAccessible(true);
                            result = m.invoke(bean, args);
                            return result;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            ex = e;
                        }
                    }
                }
            }
            clz = clz.getSuperclass();
        }
        if (ex == null)
            throw new IllegalArgumentException("can not invoke method:" + beanClass.getName()
                    + "." + methodName);
        else
            throw new RuntimeException(ex);
    }

    public static Field getField(Class cls, String fieldName) throws NoSuchFieldException {
        try {
            return cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {
        }
        try {
            return cls.getField(fieldName);
        } catch (NoSuchFieldException ignored) {
        }
        cls = cls.getSuperclass();
        if (cls != null && cls != Object.class) {
            return getField(cls, fieldName);
        } else {
            throw new NoSuchFieldException(fieldName);
        }
    }

    /**
     * Return all interfaces that the given instance implements as an array,
     * including ones implemented by superclasses.
     *
     * @param instance the instance to analyze for interfaces
     * @return all interfaces that the given instance implements as an array
     */
    public static Class<?>[] getAllInterfaces(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        return getAllInterfacesForClass(instance.getClass());
    }

    /**
     * Return all interfaces that the given class implements as an array,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     *
     * @param clazz the class to analyze for interfaces
     * @return all interfaces that the given object implements as an array
     */
    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
        return getAllInterfacesForClass(clazz, null);
    }

    /**
     * Return all interfaces that the given class implements as an array,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     *
     * @param clazz       the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     *                    (may be {@code null} when accepting all declared interfaces)
     * @return all interfaces that the given object implements as an array
     */
    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader) {
        Set<Class<?>> ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);
        return ifcs.toArray(new Class<?>[ifcs.size()]);
    }

    /**
     * Return all interfaces that the given instance implements as a Set,
     * including ones implemented by superclasses.
     *
     * @param instance the instance to analyze for interfaces
     * @return all interfaces that the given instance implements as a Set
     */
    public static Set<Class<?>> getAllInterfacesAsSet(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        return getAllInterfacesForClassAsSet(instance.getClass());
    }

    /**
     * Return all interfaces that the given class implements as a Set,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     *
     * @param clazz the class to analyze for interfaces
     * @return all interfaces that the given object implements as a Set
     */
    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
        return getAllInterfacesForClassAsSet(clazz, null);
    }

    /**
     * Return all interfaces that the given class implements as a Set,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     *
     * @param clazz       the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     *                    (may be {@code null} when accepting all declared interfaces)
     * @return all interfaces that the given object implements as a Set
     */
    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, ClassLoader classLoader) {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface() && isVisible(clazz, classLoader)) {
            return Collections.<Class<?>>singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
        while (clazz != null) {
            Class<?>[] ifcs = clazz.getInterfaces();
            for (Class<?> ifc : ifcs) {
                interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces;
    }

    /**
     * Check whether the given class is visible in the given ClassLoader.
     *
     * @param clazz       the class to check (typically an interface)
     * @param classLoader the ClassLoader to check against (may be {@code null},
     *                    in which case this method will always return {@code true})
     */
    public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
        if (classLoader == null) {
            return true;
        }
        try {
            Class<?> actualClass = classLoader.loadClass(clazz.getName());
            return (clazz == actualClass);
            // Else: different interface class found...
        } catch (ClassNotFoundException ex) {
            // No interface class found...
            return false;
        }
    }
}