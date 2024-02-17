package dev.fneira.interfaceprocessor.imp.annotations;

import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyHandler{
    Class<? extends MethodInterceptor> value();
}