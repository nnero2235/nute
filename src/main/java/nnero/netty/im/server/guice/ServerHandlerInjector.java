package nnero.netty.im.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Author: NNERO
 * Time : 下午3:27 19-2-22
 */
public class ServerHandlerInjector {

    private static final Injector INJECTOR = Guice.createInjector(new ServerHandlerModule());

    public static <T> T getInstance(Class<T> clazz){
        return INJECTOR.getInstance(clazz);
    }
}
