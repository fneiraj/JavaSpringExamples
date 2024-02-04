package dev.fneira.examples.interfaceprocessor.imp;

import org.springframework.cglib.proxy.Callback;

public interface ProxyMethodHandler {

  Callback getInterceptor();
}
