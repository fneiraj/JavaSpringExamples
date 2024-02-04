package dev.fneira.examples.interfaceprocessor.imp;

import static dev.fneira.examples.interfaceprocessor.imp.InterfaceProxyConstants.CONFIG_ANNOTATIONS;

import dev.fneira.examples.interfaceprocessor.imp.annotations.ProxyHandler;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InterfaceProxyBeanPostProcessor implements BeanPostProcessor {

  private final ApplicationContext applicationContext;
  private final Environment environment;

  @Autowired
  public InterfaceProxyBeanPostProcessor(
      final ApplicationContext applicationContext, final Environment environment) {
    this.applicationContext = applicationContext;
    this.environment = environment;
  }

  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName)
      throws BeansException {
    final Optional<Class<?>> interfaceWithAnnotation = getInterfaceWithAnnotation(bean);
    if (interfaceWithAnnotation.isPresent()) {
      return createProxy(interfaceWithAnnotation.get());
    }

    return bean;
  }

  private <T> T createProxy(final Class<T> targetClass) {
    return (T)
        Enhancer.create(targetClass, getBeanByType(getHandler(targetClass)).getInterceptor());
  }

  private Class<?> getHandler(final Class<?> targetClass) {
    return targetClass
        .getAnnotations()[0]
        .annotationType()
        .getAnnotation(ProxyHandler.class)
        .value();
  }

  private ProxyMethodHandler getBeanByType(final Class<?> type) {
    try {
      return (ProxyMethodHandler) applicationContext.getBean(type);
    } catch (BeansException e) {
      throw new RuntimeException("Bean not found for type " + type.getName(), e);
    }
  }

  private Optional<Class<?>> getInterfaceWithAnnotation(final Object bean) {
    final List<Class<?>> interfaces = Arrays.asList(bean.getClass().getInterfaces());
    return interfaces.stream().filter(this::isAnnotationPresent).findFirst();
  }

  private boolean isAnnotationPresent(final Class<?> interf) {
    return getAnnotationsToProcess().stream().anyMatch(interf::isAnnotationPresent);
  }

  private List<Class<? extends Annotation>> getAnnotationsToProcess() {
    return (List<Class<? extends Annotation>>)
        environment.getRequiredProperty(CONFIG_ANNOTATIONS, List.class).stream()
            .map(
                annotation -> {
                  try {
                    return Class.forName((String) annotation);
                  } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                  }
                })
            .collect(Collectors.toList());
  }
}
