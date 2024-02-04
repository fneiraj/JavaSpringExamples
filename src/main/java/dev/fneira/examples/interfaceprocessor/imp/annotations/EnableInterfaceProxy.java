package dev.fneira.examples.interfaceprocessor.imp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.fneira.examples.interfaceprocessor.imp.InterfaceProxyScannerRegistrar;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({InterfaceProxyScannerRegistrar.class})
public @interface EnableInterfaceProxy {
}
