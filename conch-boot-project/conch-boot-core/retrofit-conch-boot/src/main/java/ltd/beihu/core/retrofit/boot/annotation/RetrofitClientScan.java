package ltd.beihu.core.retrofit.boot.annotation;

import ltd.beihu.core.retrofit.boot.config.RetrofitClientScannerRegistrar;
import ltd.beihu.core.retrofit.boot.retrofit.RetrofitClientFactoryBean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to register Retrofit Client interfaces when using Java Config.
 *
 * @author Adam
 * @date 2020/8/3
 * @see RetrofitClientScannerRegistrar
 * @see RetrofitClientFactoryBean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RetrofitClientScannerRegistrar.class)
public @interface RetrofitClientScan {

  /**
   * Alias for the {@link #basePackages()} attribute. Allows for more concise
   * annotation declarations e.g.:
   * {@code @RetrofitClientScan("org.my.pkg")} instead of {@code @RetrofitClientScan(basePackages = "org.my.pkg"})}.
   *
   * @return base package names
   */
  String[] value() default {};

  /**
   * Base packages to scan for Retrofit Client interfaces. Note that only interfaces
   * with at least one method will be registered; concrete classes will be
   * ignored.
   *
   * @return base package names for scanning retrofit interface
   */
  String[] basePackages() default {};

  /**
   * Type-safe alternative to {@link #basePackages()} for specifying the packages to
   * scan for annotated components. The package of each class specified will be scanned.
   * <p>
   * Consider creating a special no-op marker class or interface in each package that
   * serves no purpose other than being referenced by this attribute.
   * @return the array of 'basePackageClasses'.
   */
  Class<?>[] basePackageClasses() default {};
}
