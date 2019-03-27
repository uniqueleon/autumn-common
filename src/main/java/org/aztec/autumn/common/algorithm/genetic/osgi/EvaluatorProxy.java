package org.aztec.autumn.common.algorithm.genetic.osgi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EvaluatorProxy implements InvocationHandler {

  Object target;
  
  public EvaluatorProxy(Object target) {
    // TODO Auto-generated constructor stub
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    
    return method.invoke(target, args);
  }

}
