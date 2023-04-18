package ltd.beihu.core.tools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class ReflectionHelper {

    private static Logger logger = LoggerFactory.getLogger(ReflectionHelper.class);

    /**
     * Naming prefix for CGLIB-renamed methods.
     *
     * @see #isCglibRenamedMethod
     */
    private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";

    private static final Method[] NO_METHODS = {};

    private static final Field[] NO_FIELDS = {};

    /**
     * Cache for {@link Class#getDeclaredMethods()} plus equivalent default methods
     * from Java 8 based interfaces, allowing for fast iteration.
     */
    private static final Map<Class<?>, Method[]> declaredMethodsCache =
            new ConcurrentHashMap<Class<?>, Method[]>(256);

    /**
     * Cache for {@link Class#getDeclaredFields()}, allowing for fast iteration.
     */
    private static final Map<Class<?>, Field[]> declaredFieldsCache =
            new ConcurrentHashMap<Class<?>, Field[]>(256);

    public static Object newInstance(Class type) {
        Constructor constructor = null;
        Object[] constructorArgs = new Object[0];
        try {
            constructor = type.getConstructor(new Class[]{});
        } catch (NoSuchMethodException ignored) {
            // ignore
        }

        if (constructor == null) {
            Constructor[] constructors = type.getConstructors();
            if (constructors == null || constructors.length == 0) {
                throw new UnsupportedOperationException("Class[" + type.getName() + "] has no public constructors");
            }
            constructor = constructors[getSimpleParamenterTypeIndex(constructors)];
            Class[] params = constructor.getParameterTypes();
            constructorArgs = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                constructorArgs[i] = ClassHelper.getDefaultValue(params[i]);
            }
        }

        return newInstance(constructor, constructorArgs);
    }

    public static Object newInstance(Constructor cstruct, Object[] args) {
        boolean flag = cstruct.isAccessible();

        Object var4;
        try {
            if (!flag) {
                cstruct.setAccessible(true);
            }

            Object result = cstruct.newInstance(args);
            var4 = result;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (!flag) {
                cstruct.setAccessible(flag);
            }

        }

        return var4;
    }

    public static int getSimpleParamenterTypeIndex(Constructor[] constructors) {
        Constructor constructor = null;
        Class[] params = null;
        boolean isSimpleTypes;
        for (int i = 0; i < constructors.length; i++) {
            constructor = constructors[i];
            params = constructor.getParameterTypes();
            if (params.length > 0) {
                isSimpleTypes = true;
                for (int j = 0; j < params.length; j++) {
                    if (ClassHelper.getDefaultValue(params[j]) == null) {
                        isSimpleTypes = false;
                        break;
                    }
                }
                if (isSimpleTypes) {
                    return i;
                }
            } else {
                return i;
            }
        }
        return 0;
    }

    public static Object invoke(Object finObj, Method method, Object... args) throws Throwable {
        try {
            return method.invoke(finObj, args);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    public static Object invoke(Object finObj, Object[] finArgs, Method method) throws Throwable {
        try {
            return method.invoke(finObj, finArgs);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied name
     * and parameter types. Searches all superclasses up to {@code Object}.
     * <p>Returns {@code null} if no {@link Method} can be found.
     *
     * @param clazz      the class to introspect
     * @param name       the name of the method
     * @param paramTypes the parameter types of the method
     *                   (may be {@code null} to indicate any signature)
     * @return the Method object, or {@code null} if none found
     */
    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
            for (Method method : methods) {
                if (name.equals(method.getName()) &&
                        (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static Class<?> getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) {
            return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
        } else {
            return (Class<?>) genericClass;
        }
    }

    /**
     * 检查类中是否含有此注解:
     * 仅检查类型上与方法上的注解
     */
    public static boolean existAnnatation(Class cls, Class<? extends Annotation> annotationType) {
        //final类无法代理
        if (!Modifier.isFinal(cls.getModifiers())) {
            Annotation classAnnotation = AnnotationUtils.findAnnotation(cls, annotationType);
            if (classAnnotation != null)
                return true;
        }
        for (Method method : cls.getMethods()) {
            //final方法无法代理
            if (Modifier.isFinal(method.getModifiers()))
                continue;
            Annotation methodAnnotation = AnnotationUtils.findAnnotation(method, annotationType);
            if (methodAnnotation != null)
                return true;
        }
        Class[] interfaces = cls.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class anInterface : interfaces) {
                if (existAnnatation(anInterface, annotationType)) {
                    return true;
                }
            }
        }
        if (cls.isInterface()) {
            return false;
        } else {
            Class superCls = cls.getSuperclass();
            if (superCls == null || superCls.equals(Object.class)) {
                return false;
            } else {
                return existAnnatation(superCls, annotationType);
            }
        }
    }

    /**
     * Get all declared methods on the leaf class and all superclasses.
     * Leaf class methods are included first.
     *
     * @param leafClass the class to introspect
     */
    public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
        final List<Method> methods = new ArrayList<Method>(32);
        doWithMethods(leafClass, new MethodCallback() {
            @Override
            public void doWith(Method method) {
                methods.add(method);
            }
        });
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * Perform the given callback operation on all matching methods of the given
     * class and superclasses.
     * <p>The same named method occurring on subclass and superclass will appear
     * twice, unless excluded by a {@link MethodFilter}.
     *
     * @param clazz the class to introspect
     * @param mc    the callback to invoke for each method
     * @see #doWithMethods(Class, MethodCallback, MethodFilter)
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
        doWithMethods(clazz, mc, null);
    }

    /**
     * Perform the given callback operation on all matching methods of the given
     * class and superclasses (or given interface and super-interfaces).
     * <p>The same named method occurring on subclass and superclass will appear
     * twice, unless excluded by the specified {@link MethodFilter}.
     *
     * @param clazz the class to introspect
     * @param mc    the callback to invoke for each method
     * @param mf    the filter that determines the methods to apply the callback to
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf) {
        // Keep backing up the inheritance hierarchy.
        Method[] methods = getDeclaredMethods(clazz);
        for (Method method : methods) {
            if (mf != null && !mf.matches(method)) {
                continue;
            }
            try {
                mc.doWith(method);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
            }
        }
        if (clazz.getSuperclass() != null) {
            doWithMethods(clazz.getSuperclass(), mc, mf);
        } else if (clazz.isInterface()) {
            for (Class<?> superIfc : clazz.getInterfaces()) {
                doWithMethods(superIfc, mc, mf);
            }
        }
    }


    /**
     * This variant retrieves {@link Class#getDeclaredMethods()} from a local cache
     * in order to avoid the JVM's SecurityManager check and defensive array copying.
     * In addition, it also includes Java 8 default methods from locally implemented
     * interfaces, since those are effectively to be treated just like declared methods.
     *
     * @param clazz the class to introspect
     * @return the cached array of methods
     * @see Class#getDeclaredMethods()
     */
    private static Method[] getDeclaredMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Method[] result = declaredMethodsCache.get(clazz);
        if (result == null) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
            if (defaultMethods != null) {
                result = new Method[declaredMethods.length + defaultMethods.size()];
                System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                System.arraycopy(defaultMethods, 0, result, declaredMethods.length, defaultMethods.size());
//                int index = declaredMethods.length;
//                for (Method defaultMethod : defaultMethods) {
//                    result[index] = defaultMethod;
//                    index++;
//                }
            } else {
                result = declaredMethods;
            }
            declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
        }
        return result;
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new LinkedList<Method>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }

    /**
     * 根据调用的目标与方法生成唯一性签名
     *
     * @param object -调用的目标
     * @param method -调用的方法
     * @return -唯一性签名
     */
    public static String buildKey(Object object, Method method) {
        Assert.notNull(object);
        return new StringBuilder(object.getClass().getName()).append(".").append(buildMethod(method)).toString();
    }

    /**
     * 根据调用的目标与方法生成唯一性签名
     *
     * @param object -调用的目标类型
     * @param method -调用的方法
     * @return -唯一性签名
     */
    public static String buildKey(Class cls, Method method) {
        Assert.notNull(cls);
        return new StringBuilder(cls.getName()).append(".").append(buildMethod(method)).toString();
    }

    public static String buildSimpleKey(Object object, Method method) {
        Assert.notNull(object);
        return new StringBuilder(object.getClass().getName()).append(".").append(method.getName()).toString();
    }

    /**
     * 根据方法名称及参数生成签名:
     * 1:此签名在此方法当前类中具有唯一性
     * 2:不同类中的方法如果方法名与参数类型均相同,则生成的方法签名是相同的
     *
     * @param method -目标方法
     * @return -唯一性签名(相对于此方法当前类)
     */
    public static String buildMethod(Method method) {
        StringBuilder strbuilder = new StringBuilder(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 0) {
            for (Class<?> parameterType : parameterTypes) {
                strbuilder.append("#").append(parameterType.getName());
            }
        }
        return strbuilder.toString();
    }

    /**
     * 获取真实调用的方法
     */
    public static Method realInvokeMethod(Method method, Object target) {
        if (target == null)
            return method;
        if (method.getDeclaringClass().equals(target.getClass()))
            return method;
        Class targetCls = target.getClass();
        while (targetCls != null) {
            try {
                return targetCls.getDeclaredMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (targetCls.equals(Object.class)) {
                return method;
            } else {
                targetCls = targetCls.getSuperclass();
            }
        }
        return method;
    }

    /**
     * 获取class上的所有方法,包括其父类的私有方法
     *
     * @param onlyCustom -是否只包含自定义的方法
     *                   即:true表示不包含从Object继承的方法
     */
    public static Method[] getAllMethods(Class cls, boolean onlyCustom) {
        Assert.notNull(cls);
        Set<Method> methods = new HashSet<>();
        while (cls != Object.class && cls != null) {
            for (Method method : cls.getDeclaredMethods()) {
                methods.add(method);
            }
            cls = cls.getSuperclass();
        }
        if (!onlyCustom && cls == Object.class) {
            for (Method method : cls.getDeclaredMethods()) {
                methods.add(method);
            }
        }
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * 获取class上的所有属性,包括其父类的私有属性
     *
     * @param onlyCustom -是否只包含自定义的属性
     *                   即:true表示不包含从Object继承的属性
     */
    public static Field[] getAllFields(Class cls, boolean onlyCustom) {
        Assert.notNull(cls);
        Set<Field> fields = new HashSet<>();
        while (cls != Object.class && cls != null) {
            for (Field method : cls.getDeclaredFields()) {
                fields.add(method);
            }
            cls = cls.getSuperclass();
        }
        if (!onlyCustom && cls == Object.class) {
            for (Field method : cls.getDeclaredFields()) {
                fields.add(method);
            }
        }
        return fields.toArray(new Field[fields.size()]);
    }


    public static boolean isCustomMethod(Method method) {
        return method.getDeclaringClass() != Object.class;
    }

    public static boolean isObjectMethod(Method method) {
        return method.getDeclaringClass() == Object.class;
    }

    public static Object safeCall(Object target, Method method, Object... params) {
        try {
            try {
                return method.invoke(target, params);
            } catch (IllegalAccessException e) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method.invoke(target, params);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            LoggerFactory.getLogger(method.getDeclaringClass()).error("error to call [" + buildMethod(method) + "]");
            return method.getReturnType() == Void.class ? null : ClassHelper.getDefaultValue(method.getReturnType());//返回默认值
        }
    }

    public static Object safeGet(Object target, Field field) {
        try {
            try {
                return field.get(target);
            } catch (IllegalAccessException e) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(target);
            }
        } catch (IllegalAccessException e) {
            logger.error("error to get [" + field.getName() + "] form " + field.getDeclaringClass());
            return ClassHelper.getDefaultValue(field.getType());//返回默认值
        }
    }

    public static void safeSet(Object target, Field field, Object value) {
        try {
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(target, value);
            }
        } catch (IllegalAccessException e) {
            logger.error("error to set [" + field.getName() + "] to " + field.getDeclaringClass());
        }
    }

    /**
     * @see ClassHelper#getField(Class, String)
     */
    public static Object getField(Object target, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = ClassHelper.getField(target.getClass(), fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(target);
    }

    /**
     * Make the given field accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     *
     * @param field the field to make accessible
     * @see Field#setAccessible
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     *
     * @param method the method to make accessible
     * @see Method#setAccessible
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }


    /**
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     *
     * @param ctor the constructor to make accessible
     * @see Constructor#setAccessible
     */
    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    /**
     * Action to take on each method.
     */
    public interface MethodCallback {

        /**
         * Perform an operation using the given method.
         *
         * @param method the method to operate on
         */
        void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
    }

    /**
     * Callback optionally used to filter methods to be operated on by a method callback.
     */
    public interface MethodFilter {

        /**
         * Determine whether the given method matches.
         *
         * @param method the method to check
         */
        boolean matches(Method method);
    }

}
 