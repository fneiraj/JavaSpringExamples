package dev.fneira.examples.interfaceprocessor.restclient;

import dev.fneira.examples.interfaceprocessor.imp.ProxyMethodHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;

@Configuration
public class RestClientHandler implements ProxyMethodHandler {

  @Override
  public Interceptor getInterceptor() {
    return new Interceptor();
  }

  public static class Interceptor implements MethodInterceptor {

    @Override
    public Object intercept(
        final Object obj, final Method method, final Object[] args, final MethodProxy proxy)
        throws Throwable {
      if (Object.class.equals(method.getDeclaringClass())) {
        return proxy.invokeSuper(obj, args);
      }

      RestClient restClient = obj.getClass().getInterfaces()[0].getAnnotation(RestClient.class);

      if (method.isAnnotationPresent(RestClientCfg.class)) {
        final RestClientCfg restClientCfg = method.getAnnotation(RestClientCfg.class);
        final List<Param> params = getParams(method, args);

        // System.out.println("Calling rest client: " + restClient.name() + " at " +
        // restClient.url());

        if (method.getReturnType() == String.class) {
          return String.format(
              "[%s] %s %s%s %s",
              restClient.name(),
              restClientCfg.method(),
              restClient.url(),
              restClientCfg.path(),
              params.stream()
                  .map(
                      param ->
                          "[("
                              + param.getType()
                              + ") "
                              + param.getName()
                              + "="
                              + param.getValue()
                              + "]")
                  .collect(Collectors.joining(", ")));
        } else {
          return 1;
        }
      }

      throw new IllegalArgumentException(
          "Method " + method.getName() + " does not have the @RestClientCfg annotation");
    }

    private List<Param> getParams(final Method method, Object[] args) {
      final List<Param> params = new ArrayList<>();
      for (int i = 0; i < method.getParameters().length; i++) {
        final Parameter parameter = method.getParameters()[i];
        if (parameter.isAnnotationPresent(RestClientParam.class)) {
          final RestClientParam restClientParam = parameter.getAnnotation(RestClientParam.class);

          params.add(new Param(restClientParam.name(), (String) args[i], restClientParam.type()));
        } else {
          params.add(new Param(getParameterName(method, i), (String) args[i], "query"));
        }
      }
      return params;
    }

    private String getParameterName(final Method method, final int index) {
      final StandardReflectionParameterNameDiscoverer discoverer =
          new StandardReflectionParameterNameDiscoverer();
      final String[] names = discoverer.getParameterNames(method);

      if (names == null) {
        throw new IllegalArgumentException(
            "Method " + method.getName() + " does not have parameter names");
      }

      return names[index];
    }

    private static class Param {
      private final String name;
      private final String value;
      private final String type;

      public Param(final String name, final String value, final String type) {
        this.name = name;
        this.value = value;
        this.type = type;
      }

      public String getName() {
        return name;
      }

      public String getValue() {
        return value;
      }

      public String getType() {
        return type;
      }
    }
  }
}
