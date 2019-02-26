package nnero.netty.redis.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.util.Set;

/**
 * Author: NNERO
 * Time : 下午3:28 19-2-22
 */
public class RedisHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        Reflections reflections = new Reflections("nnero.netty.redis");
        Set<Class<?>> singles = reflections.getTypesAnnotatedWith(Singleton.class);
        if(singles != null && singles.size() > 0){
            for(Class<?> clazz : singles){
                bind(clazz).in(Scopes.SINGLETON);
            }
        }
    }
}
