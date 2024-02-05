package dev.fneira.interfaceprocessor.imp;

import dev.fneira.interfaceprocessor.imp.annotations.EnableInterfaceProxy;
import dev.fneira.interfaceprocessor.imp.annotations.ProxyHandler;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class InterfaceProxyScannerRegistrar
    implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

  private Environment environment;
  private ResourceLoader resourceLoader;

  @Override
  public void setEnvironment(final Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setResourceLoader(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void registerBeanDefinitions(
      final AnnotationMetadata metadata, final BeanDefinitionRegistry registry) {
    LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();

    final Set<String> basePackages = getBasePackages(metadata);

    for (String basePackage : basePackages) {
      candidateComponents.addAll(getScanner().findCandidateComponents(basePackage));
    }

    for (BeanDefinition candidateComponent : candidateComponents) {
      if (candidateComponent instanceof AnnotatedBeanDefinition beanDefinition) {
        final AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
        Assert.isTrue(
            annotationMetadata.isInterface(),
            "@ProxyHandler can only be specified on an interface");

        final Class<?> targetClass = getClass(beanDefinition.getBeanClassName());

        registerBean(registry, targetClass);
      }
    }
  }

  protected ClassPathScanningCandidateComponentProvider getScanner() {
    final ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false, this.environment) {
          @Override
          protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
            if (beanDefinition.getMetadata().isIndependent()) {
              return !beanDefinition.getMetadata().isAnnotation();
            }
            return false;
          }
        };

    scanner.setResourceLoader(this.resourceLoader);
    scanner.addIncludeFilter(new AnnotationTypeFilter(ProxyHandler.class));

    return scanner;
  }

  protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
    final Map<String, Object> attributes =
        importingClassMetadata.getAnnotationAttributes(
            EnableInterfaceProxy.class.getCanonicalName());

    final Set<String> basePackages = new HashSet<>();

    for (String pkg : (String[]) attributes.get("basePackages")) {
      if (StringUtils.hasText(pkg)) {
        basePackages.add(pkg);
      }
    }
    if (basePackages.isEmpty()) {
      basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
    }
    return basePackages;
  }

  private <T> void registerBean(final BeanDefinitionRegistry registry, final Class<T> targetClass) {
    final BeanDefinitionBuilder definition =
        BeanDefinitionBuilder.genericBeanDefinition(InterfaceProxyFactoryBean.class)
            .addPropertyValue("type", targetClass)
            .addPropertyValue("handler", getHandler(targetClass));

    final BeanDefinition beanDefinition = definition.getRawBeanDefinition();
    beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, targetClass);
    beanDefinition.setPrimary(true);

    BeanDefinitionHolder beanDefinitionHolder =
        new BeanDefinitionHolder(beanDefinition, targetClass.getSimpleName());

    BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
  }

  private Class<?> getHandler(final Class<?> targetClass) {
    return targetClass
        .getAnnotations()[0]
        .annotationType()
        .getAnnotation(ProxyHandler.class)
        .value();
  }

  private Class<?> getClass(final String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
