package dev.fneira.interfaceprocessor.imp;

import org.springframework.cglib.proxy.Callback;

public interface ProxyMethodHandler {

  Callback getInterceptor();
}
