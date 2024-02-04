package dev.fneira.examples.interfaceprocessor.imp;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class InterfaceProxyScannerRegistrar
    implements ImportBeanDefinitionRegistrar, EnvironmentAware {

  private Environment environment;

  @Override
  public void setEnvironment(final Environment environment) {
    this.environment = environment;
  }

  @Override
  public void registerBeanDefinitions(
      final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry) {
    final String basePackage = getBasePackage(importingClassMetadata);
    final List<AnnotationWithHandlerClass> annotations = getAnnotations(importingClassMetadata);

    final Set<BeanDefinition> beanDefinitions = getBeanDefinition(basePackage, annotations);

    beanDefinitions.forEach(
        beanDefinition -> {
          try {
            Class<?> targetClass = Class.forName(beanDefinition.getBeanClassName());
            registerBeanForInterface(registry, targetClass);
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private String getBasePackage(final AnnotationMetadata importingClassMetadata) {
    return Optional.ofNullable(
            AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(
                    EnableInterfaceProxy.class.getName())))
        .map(annotationAttributes -> annotationAttributes.getStringArray("basePackages"))
        .map(strings -> strings.length > 0 ? strings[0] : null)
        .map(basePackage -> environment.getRequiredProperty(basePackage).trim())
        .orElseThrow(() -> new IllegalArgumentException("basePackages is required"));
  }

  private List<AnnotationWithHandlerClass> getAnnotations(
      final AnnotationMetadata importingClassMetadata) {
    final List<String> annotations =
        Optional.ofNullable(
                AnnotationAttributes.fromMap(
                    importingClassMetadata.getAnnotationAttributes(
                        EnableInterfaceProxy.class.getName())))
            .map(annotationAttributes -> annotationAttributes.getString("annotations"))
            .map(basePackage -> environment.getRequiredProperty(basePackage, List.class))
            .orElseThrow(() -> new IllegalArgumentException("annotations is required"));

    return annotations.stream()
        .map(
            annotation -> {
              try {
                return (Class<? extends Annotation>) Class.forName(annotation);
              } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
              }
            })
        .map(annotation -> new AnnotationWithHandlerClass(annotation, getHandlerClass(annotation)))
        .collect(Collectors.toList());
  }

  private Class<? extends ProxyMethodHandler> getHandlerClass(
      final Class<? extends Annotation> clazz) {
    return (Class<? extends ProxyMethodHandler>) clazz.getAnnotation(ProxyHandler.class).value();
  }

  private Set<BeanDefinition> getBeanDefinition(
      final String basePackage, final List<AnnotationWithHandlerClass> annotations) {
    final ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false) {
          @Override
          protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
            return super.isCandidateComponent(beanDefinition)
                || beanDefinition.getMetadata().isAbstract();
          }
        };

    annotations.forEach(
        annotation ->
            provider.addIncludeFilter(
                new AnnotationTypeFilter(annotation.getAnnotation(), true, true)));

    return provider.findCandidateComponents(basePackage);
  }

  private <T> void registerBeanForInterface(
      final BeanDefinitionRegistry registry, final Class<T> targetClass) {
    final BeanDefinitionBuilder builder =
        BeanDefinitionBuilder.genericBeanDefinition(targetClass, () -> createProxy(targetClass));
    final BeanDefinition beanDefinition = builder.getRawBeanDefinition();
    registry.registerBeanDefinition(targetClass.getSimpleName() + "Proxy", beanDefinition);
  }

  private <T> T createProxy(final Class<T> targetClass) {
    return (T) Enhancer.create(targetClass, (MethodInterceptor) (obj, method, args, proxy) -> null);
  }

  private static class AnnotationWithHandlerClass {
    private final Class<? extends Annotation> annotation;
    private final Class<? extends ProxyMethodHandler> handlerClass;

    public AnnotationWithHandlerClass(
        final Class<? extends Annotation> annotation,
        final Class<? extends ProxyMethodHandler> handlerClass) {
      this.annotation = annotation;
      this.handlerClass = handlerClass;
    }

    public Class<? extends Annotation> getAnnotation() {
      return annotation;
    }

    public Class<? extends ProxyMethodHandler> getHandlerClass() {
      return handlerClass;
    }
  }
}
