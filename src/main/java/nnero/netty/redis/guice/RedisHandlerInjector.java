package nnero.netty.redis.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Author: NNERO
 * Time : 下午3:27 19-2-22
 */
public class RedisHandlerInjector {

    private static final Injector INJECTOR = Guice.createInjector(new RedisHandlerModule());

    public static <T> T getInstance(Class<T> clazz){
        return INJECTOR.getInstance(clazz);
    }
}
