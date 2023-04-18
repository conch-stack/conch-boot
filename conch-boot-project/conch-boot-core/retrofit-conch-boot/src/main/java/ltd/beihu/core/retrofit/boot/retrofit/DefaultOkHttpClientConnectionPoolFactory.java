package ltd.beihu.core.retrofit.boot.retrofit;

import okhttp3.ConnectionPool;

import java.util.concurrent.TimeUnit;

public class DefaultOkHttpClientConnectionPoolFactory implements OkHttpClientConnectionPoolFactory {

	@Override
	public ConnectionPool create(int maxIdleConnections, long keepAliveDuration,
								 TimeUnit timeUnit) {
		return new ConnectionPool(maxIdleConnections, keepAliveDuration, timeUnit);
	}

}
