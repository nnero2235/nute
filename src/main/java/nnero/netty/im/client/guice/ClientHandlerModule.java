package nnero.netty.im.client.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import nnero.netty.im.Single;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Author: NNERO
 * Time : 下午3:28 19-2-22
 */
public class ClientHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        Reflections reflections = new Reflections("nnero.netty.im.client");
        Set<Class<?>> singles = reflections.getTypesAnnotatedWith(Single.class);
        if(singles != null && singles.size() > 0){
            for(Class<?> clazz : singles){
                bind(clazz).in(Scopes.SINGLETON);
            }
        }
    }
}
