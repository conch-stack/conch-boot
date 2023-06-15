package com.nabob.conch.tools.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.reflect.TypeToken;
import com.nabob.conch.tools.utils.AnnotationUtils;
import com.nabob.conch.tools.utils.ReflectionHelper;
import com.nabob.conch.tools.utils.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SecurityManager {
    private static final Logger logger = LoggerFactory.getLogger(SecurityManager.class);

    private SecurityManager() {
    }

    private static SecurityManager securityManager = new SecurityManager();

    public static SecurityManager instance() {
        return securityManager;
    }

    private LoadingCache<String, Encryptor> encryptorCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(50)
            .build(CacheLoader.from(this::findEncrypt));

    private Cache<String, Boolean> missingEncryptorCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(50)
            .build();

    private LoadingCache<String, Decryptor> decryptorCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(50)
            .build(CacheLoader.from(this::findDecrypt));

    private Cache<String, Boolean> missingDecryptorCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(50)
            .build();

    private LoadingCache<Class, Object> beanCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(50)
            .build(CacheLoader.from(this::instanceBean));

    private Object instanceBean(Class cls) {
        return ReflectionHelper.newInstance(cls);
    }

    public Encryptor encryptor(String algorithm) {
        if (ObjectUtils.defaultIfNull(missingEncryptorCache.getIfPresent(algorithm), false)) {
            return null;
        }
        return encryptorCache.getUnchecked(algorithm);
    }

    public Decryptor decryptor(String algorithm) {
        if (ObjectUtils.defaultIfNull(missingDecryptorCache.getIfPresent(algorithm), false)) {
            return null;
        }
        return decryptorCache.getUnchecked(algorithm);
    }

    private static final String encryptMethod = "encrypt";
    private static final String decryptMethod = "decrypt";

    private Encryptor findEncrypt(String algorithm) {

        Reflections reflections = new Reflections("com.nabob.conch");

        Set<Class<?>> encryptSupportClses = reflections.getTypesAnnotatedWith(EncryptSupport.class);
        for (Class<?> encryptSupportCls : encryptSupportClses) {
            EncryptSupport encryptSupport = AnnotationUtils.findAnnotation(encryptSupportCls, EncryptSupport.class);
            String[] algorithms = encryptSupport.value();
            if (!findAlgorithm(algorithms, algorithm)) {
                continue;
            }

            Set<? extends Class<?>> supertypes = TypeToken.of(encryptSupportCls).getTypes().rawTypes();
            for (Class<?> supertype : supertypes) {
                for (Method method : supertype.getDeclaredMethods()) {
                    if (Objects.equals(encryptMethod, method.getName()) && !method.isSynthetic()) {
                        if (method.getParameterCount() < 1) {
                            logger.warn("Method {} named encrypt but has no parameters.Encrypt methods must have at least 1 parameter",
                                    ReflectionHelper.buildKey(supertype, method));
                            continue;
                        }
                        Object bean = Modifier.isStatic(method.getModifiers()) ? null : beanCache.getUnchecked(encryptSupportCls);
                        Encryptor encryptor = buildEncryptor(bean, method);
                        for (String algo : algorithms) {
                            encryptorCache.put(algo, encryptor);
                        }
                        return encryptor;
                    }
                }
            }
        }
        missingEncryptorCache.put(algorithm, true);
        throw new IllegalArgumentException("unsupported algorithm:" + algorithm);
    }

    private Encryptor buildEncryptor(Object bean, Method method) {
        ReflectionUtils.makeAccessible(method);
        if (method.getParameterCount() == 1) {
            return new SingleMethodEncryptor(bean, method);
        } else {
            return new MultMethodEncryptor(bean, method);
        }
    }

    private Decryptor findDecrypt(String algorithm) {

        Reflections reflections = new Reflections("com.nabob.conch");

        Set<Class<?>> decryptSupportClses = reflections.getTypesAnnotatedWith(DecryptSupport.class);
        for (Class<?> decryptSupportCls : decryptSupportClses) {
            DecryptSupport decryptSupport = AnnotationUtils.findAnnotation(decryptSupportCls, DecryptSupport.class);
            String[] algorithms = decryptSupport.value();
            if (!findAlgorithm(algorithms, algorithm)) {
                continue;
            }


            Set<? extends Class<?>> supertypes = TypeToken.of(decryptSupportCls).getTypes().rawTypes();
            for (Class<?> supertype : supertypes) {
                for (Method method : supertype.getDeclaredMethods()) {
                    if (Objects.equals(decryptMethod, method.getName()) && !method.isSynthetic()) {
                        if (method.getParameterCount() < 1) {
                            logger.warn("Method {} named encrypt but has no parameters.Encrypt methods must have at least 1 parameter",
                                    ReflectionHelper.buildKey(supertype, method));
                            continue;
                        }
                        Object bean = Modifier.isStatic(method.getModifiers()) ? null : beanCache.getUnchecked(decryptSupportCls);
                        Decryptor decryptor = buildDecryptor(bean, method);
                        for (String algo : algorithms) {
                            decryptorCache.put(algo, decryptor);
                        }
                        return decryptor;
                    }
                }
            }
        }
        missingDecryptorCache.put(algorithm, true);
        throw new IllegalArgumentException("unsupported algorithm:" + algorithm);
    }

    private Decryptor buildDecryptor(Object bean, Method method) {
        ReflectionUtils.makeAccessible(method);
        if (method.getParameterCount() == 1) {
            return new SingleMethodDecryptor(bean, method);
        } else {
            return new MultMethodDecryptor(bean, method);
        }
    }

    private boolean findAlgorithm(String[] algorithms, String algorithm) {
        if (ArrayUtils.contains(algorithms, algorithm)) {
            return true;
        }
        for (String algo : algorithms) {
            if (algo.equalsIgnoreCase(algorithm)) {
                return true;
            }
        }
        return false;

    }

    private static abstract class MethodInvoker {

        protected Object delegate;

        protected Method method;

        public MethodInvoker(Object delegate, Method method) {
            this.delegate = delegate;
            this.method = method;
        }

        protected Object invoke(Object[] args) throws Throwable {
            try {
                return method.invoke(delegate, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    private static class SingleMethodEncryptor extends MethodInvoker implements Encryptor {

        public SingleMethodEncryptor(Object delegate, Method method) {
            super(delegate, method);
        }

        @Override
        public Object encrypt(Object input, Object... params) throws Throwable {
            return invoke(new Object[]{input});
        }
    }

    private static class MultMethodEncryptor extends MethodInvoker implements Encryptor {

        public MultMethodEncryptor(Object delegate, Method method) {
            super(delegate, method);
        }

        @Override
        public Object encrypt(Object input, Object... params) throws Throwable {
            Object[] args = new Object[method.getParameterCount()];
            args[0] = input;
            System.arraycopy(params, 0, args, 1, params.length);
            return invoke(args);
        }
    }

    private static class SingleMethodDecryptor extends MethodInvoker implements Decryptor {

        public SingleMethodDecryptor(Object delegate, Method method) {
            super(delegate, method);
        }

        @Override
        public Object decrypt(Object input, Object... params) throws Throwable {
            return invoke(new Object[]{input});
        }
    }

    private static class MultMethodDecryptor extends MethodInvoker implements Decryptor {

        public MultMethodDecryptor(Object delegate, Method method) {
            super(delegate, method);
        }

        @Override
        public Object decrypt(Object input, Object... params) throws Throwable {
            Object[] args = new Object[method.getParameterCount()];
            args[0] = input;
            System.arraycopy(params, 0, args, 1, params.length);
            return invoke(args);
        }
    }
}
