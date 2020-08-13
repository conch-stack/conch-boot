package ltd.beihu.core.retrofit.boot.config;

import ltd.beihu.core.retrofit.boot.annotation.RetrofitClient;
import ltd.beihu.core.retrofit.boot.annotation.RetrofitClientScan;
import ltd.beihu.core.retrofit.boot.retrofit.RetrofitClientFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link ImportBeanDefinitionRegistrar} to allow annotation configuration of
 * Retrofit Client scanning. Using an @Enable annotation allows beans to be
 * registered via @Component configuration, whereas implementing
 * {@code BeanDefinitionRegistryPostProcessor} will work for XML configuration.
 *
 * @author Adam
 * @date 2020/8/1
 */
public class RetrofitClientScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes retrofitClientScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(RetrofitClientScan.class.getName()));
        if (retrofitClientScanAttrs != null) {
            registerBeanDefinitions(retrofitClientScanAttrs, registry, importingClassMetadata);
        }
    }

    void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, AnnotationMetadata importingClassMetadata) {

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RetrofitClient.class);

        scanner.addIncludeFilter(annotationTypeFilter);
        Set<String> basePackages = getBasePackages(annoAttrs, importingClassMetadata);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(),
                            "@RetrofitClient can only be specified on an interface");

                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(
                            RetrofitClient.class.getCanonicalName());

                    registerRetrofitClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void registerRetrofitClient(BeanDefinitionRegistry registry,
                                        AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(RetrofitClientFactoryBean.class);

        definition.addPropertyValue("url", getUrl(attributes));
        definition.addPropertyValue("name", getName(attributes));
        definition.addPropertyValue("type", className);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationAttributes annoAttrs, AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();
        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("value"))
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toList()));

        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("basePackages"))
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toList()));

        basePackages.addAll(
                Arrays.stream(annoAttrs.getClassArray("basePackageClasses"))
                        .map(ClassUtils::getPackageName)
                        .collect(Collectors.toList()));

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    // private

    private String getUrl(Map<String, Object> attributes) {
        String url = resolve((String) attributes.get("url"));
        String applicationName = resolve((String) attributes.get("applicationName"));
        String currentApplicationName = environment.getProperty("spring.application.name");
        if (StringUtils.hasText(applicationName) && StringUtils.hasText(currentApplicationName) &&
                applicationName.equalsIgnoreCase(currentApplicationName)) {
            return getUrl(url, true);
        }
        return getUrl(url, false);
    }

    /* for testing */ String getName(Map<String, Object> attributes) {
        String name = (String) attributes.get("serviceId");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("name");
        }
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }
        name = resolve(name);
        return getName(name);
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    private String getName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }

        String host = null;
        try {
            String url;
            if (!name.startsWith("http://") && !name.startsWith("https://")) {
                url = "http://" + name;
            } else {
                url = name;
            }
            host = new URI(url).getHost();
        } catch (URISyntaxException e) {
        }
        Assert.state(host != null, "Service id not legal hostname (" + name + ")");
        return name;
    }

    private String getUrl(String url, boolean localCall) {
        if (StringUtils.hasText(url) && !(url.startsWith("#{") && url.contains("}"))) {
            if (!url.contains("://")) {
                url = "http://" + url;
            }
            try {
                URL targetUrl = new URL(url);

                // modify remote call to local
                if (localCall) {
                    String currentServerPort = environment.getProperty("server.port");
                    String newPattern = "http://127.0.0.1:" + currentServerPort;
                    String oldPattern = targetUrl.getProtocol() + "://" + ((-1 != targetUrl.getPort()) ? targetUrl.getHost() + ":" + targetUrl.getPort() : targetUrl.getHost());
                    url = StringUtils.replace(url, oldPattern, newPattern);
                }
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(url + " is malformed", e);
            }
        }
        return url;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
