package ltc.beihu.core.redis.boot;

import org.springframework.lang.Nullable;

public interface MessageListener<T> {

    /**
     * Callback for processing received objects through Redis.
     *
     * @param message message must not be {@literal null}.
     * @param channel the channel associated with the message..
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    void onMessage(T message, String channel, @Nullable String pattern);
}
