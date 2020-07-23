/*
 * Copyright (c) $year, TIDESQUARE and/or its affiliates.. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is for the services of STELLA, the back office solution of
 * TOURVIS, TIDESQUARE's online travel service. This code is a framework
 * for developing JAVA based solutions through Spring Framework (Spring
 * Boot). This code may be used for non-STELLA solutions, but may not be
 * suitable and makes no warranty.
 *
 * This code is private and can only be used with TIDESQUARE's solutions.
 * Reproduction, modification and redistribution of this code for the
 * development of a solution other than TIDESQUARE's solution may result
 * in civil and criminal penalties.
 *
 * If you need to duplicate, modify, redistribute or use this code, please
 * contact us via the contact details below.
 *
 * @url http://www.tidesquare.com
 * @author Airlines Platform Dept. Manager, JI YOONSEONG
 * @email jiys@tidesquare.com
 */

package com.example.dynamicmapperscanner.a;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Application Context 통한 처리를 위한 Provider 클래스
 */
@Component
@SuppressWarnings("unused")
public class ApplicationCtxProvider implements ApplicationContextAware {

  private static ApplicationContext context;

  public static ApplicationContext getContext() {
    return context;
  }

  /**
   * 인스턴스를 Application Bean 등록
   *
   * @param instance 등록할 인스턴스
   * @param name     등록할 인스턴스 이름
   * @param <T>      등록할 인스턴스 클래스 타입 (Generic)
   */
  public static <T> void autowireBean(T instance, String name) {
    AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
    factory.autowireBean(instance);
    factory.initializeBean(instance, name);
  }

  public static Object autowireBean(Class<?> instance) {
    AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
    return factory.autowire(instance, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
  }

  /**
   * Application Context 내 BeanFactory 가져오기
   *
   * @return 실행 중인 Application Context BeanFactory
   */
  public static AutowireCapableBeanFactory getAutowireCapableBeanFactory() {
    return context.getAutowireCapableBeanFactory();
  }

  public static ConfigurableListableBeanFactory getBeanFactory() {
    return ((StaticApplicationContext)context).getBeanFactory();
  }

  @Override
  public void setApplicationContext(
      @NonNull ApplicationContext applicationContext
  ) throws BeansException {
    context = applicationContext;
  }
}
