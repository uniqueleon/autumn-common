package org.aztec.autumn.common.algorithm.genetic;

public interface Evaluator {

  public double evaluateAsDouble(Individual individual);

  public String evaluateAsString(Individual individual);

  public Object evaluateAsObject(Individual individual);
  

  public <T> T evaluate(Individual individual,Class<T> resultBeanCls);
  
  
  public boolean isSatisfied(Individual individual);
}
