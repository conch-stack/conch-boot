package ltd.beihu.core.retrofit.boot.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retrofit Client Annotation
 *
 * @author Adam
 * @date 2020/8/3
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RetrofitClient {

    /**
     * Client Name
     */
    @AliasFor("name")
    String value() default "";

    /**
     * Client Name
     */
    @AliasFor("value")
    String name() default "";

    /**
     * Application Name : to support to modify remote call to local
     */
    String applicationName() default "";

    /**
     * @return an absolute URL or resolvable hostname (the protocol is optional).
     */
    String url() default "";

}
