package com.cqvip.innocence.framework.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author eternal
 * @Date 2021/9/10
 * @Version 1.0
 */
@Component
public class Application implements ApplicationContextAware {

 private ApplicationContext applicationContext;

 @Override
 public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
  this.applicationContext = applicationContext;
 }

public Object getBean(Class clazz){
  return applicationContext.getBean(clazz);
}
}