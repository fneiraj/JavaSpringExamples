package dev.fneira.interfaceprocessor.resourceprovider;

import dev.fneira.interfaceprocessor.FakeDataProviderStub;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceProviderHandler implements MethodInterceptor {

  private final FakeDataProviderStub fakeDataProviderStub;

  @Autowired
  public ResourceProviderHandler(final FakeDataProviderStub fakeDataProviderStub) {
    this.fakeDataProviderStub = fakeDataProviderStub;
  }

  @Override
  public Object intercept(
      final Object obj, final Method method, final Object[] args, MethodProxy proxy)
      throws Throwable {
    if (Object.class.equals(method.getDeclaringClass())) {
      return proxy.invokeSuper(obj, args);
    }

    if (method.isAnnotationPresent(ResourceValue.class)) {
      final ResourceValue resourceValue = method.getAnnotation(ResourceValue.class);

      if (method.getReturnType() == String.class) {
        return this.fakeDataProviderStub.getResource(resourceValue.key());
      } else {
        return 1;
      }
    }

    throw new IllegalArgumentException(
        "Method " + method.getName() + " does not have the @ResourceValue annotation");
  }
}
