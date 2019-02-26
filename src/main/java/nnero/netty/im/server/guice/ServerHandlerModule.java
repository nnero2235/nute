package nnero.netty.im.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import nnero.netty.im.Single;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Author: NNERO
 * Time : 下午3:28 19-2-22
 */
public class ServerHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        Reflections reflections = new Reflections("nnero.netty.im.server");
        Set<Class<?>> singles = reflections.getTypesAnnotatedWith(Single.class);
        if(singles != null && singles.size() > 0){
            for(Class<?> clazz : singles){
                bind(clazz).in(Scopes.SINGLETON);
            }
        }
    }
}
