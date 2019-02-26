package nnero.netty.im.client.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nnero.netty.im.server.guice.ServerHandlerModule;

/**
 * Author: NNERO
 * Time : 下午3:27 19-2-22
 */
public class ClientHandlerInjector {

    private static final Injector INJECTOR = Guice.createInjector(new ClientHandlerModule());

    public static <T> T getInstance(Class<T> clazz){
        return INJECTOR.getInstance(clazz);
    }
}
