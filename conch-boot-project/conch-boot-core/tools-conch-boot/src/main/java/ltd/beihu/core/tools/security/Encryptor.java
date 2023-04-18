package ltd.beihu.core.tools.security;

public interface Encryptor<T, R> {

    R encrypt(T input, Object... params) throws Throwable;
}
