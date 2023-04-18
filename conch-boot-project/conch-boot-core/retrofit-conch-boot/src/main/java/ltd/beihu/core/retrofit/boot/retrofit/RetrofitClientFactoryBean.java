package ltd.beihu.core.retrofit.boot.retrofit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Retrofit Client FactoryBean
 *
 * @author Adam
 * @date 2020/8/1
 */
public class RetrofitClientFactoryBean implements FactoryBean<Object>, ApplicationContextAware {

    private Class<?> type;

    private ApplicationContext applicationContext;

    private String name;

    private String url;

    private RetrofitClientBuildDelegate retrofitClientBuildDelegate;

    @Override
    public Object getObject() throws Exception {
        return retrofitClientBuildDelegate.getClient(url, type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    public Class<?> getType() {
        return type;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RetrofitClientBuildDelegate getRetrofitClientBuildDelegate() {
        return retrofitClientBuildDelegate;
    }

    public void setRetrofitClientBuildDelegate(RetrofitClientBuildDelegate retrofitClientBuildDelegate) {
        this.retrofitClientBuildDelegate = retrofitClientBuildDelegate;
    }
}
