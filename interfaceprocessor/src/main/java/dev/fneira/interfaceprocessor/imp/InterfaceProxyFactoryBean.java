package dev.fneira.interfaceprocessor.imp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class InterfaceProxyFactoryBean
    implements FactoryBean<Object>, InitializingBean, BeanFactoryAware {
  private BeanFactory beanFactory;
  private Class<?> type;
  private Class<? extends MethodInterceptor> handler;

  @Override
  public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  <T> T createProxy() {
    return (T) Enhancer.create(type, beanFactory.getBean(handler));
  }

  @Override
  public Object getObject() {
    return createProxy();
  }

  @Override
  public Class<?> getObjectType() {
    return type;
  }

  @Override
  public void afterPropertiesSet() {
    if (type == null) {
      throw new IllegalArgumentException("Property 'type' is required");
    }
    if (handler == null) {
      throw new IllegalArgumentException("Property 'handler' is required");
    }
  }

  public void setType(final Class<?> type) {
    this.type = type;
  }

  public void setHandler(final Class<? extends MethodInterceptor> handler) {
    this.handler = handler;
  }
}
