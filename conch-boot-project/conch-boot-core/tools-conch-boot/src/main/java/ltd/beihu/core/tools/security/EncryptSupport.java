package ltd.beihu.core.tools.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * which annotation by {@code EncryptSupport} should implement a method which named "encrypt"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EncryptSupport {
    String[] value();
}
