package dev.fneira.interfaceprocessor.resourceprovider;

import dev.fneira.interfaceprocessor.imp.annotations.ProxyHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ProxyHandler(ResourceProviderHandler.class)
public @interface ResourceProvider {
}
