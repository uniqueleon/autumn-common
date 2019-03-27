package org.aztec.autumn.common.algorithm.genetic;


/**
 * 个体值评价器，主要通过此评价器来评价个体的优劣。
 * 
 * @author 黎明
 *
 */
public interface Evaluator {

  /**
   * 以一个双精度浮点型数值评价一个个体
   * @param individual 被评价个体
   * @return 评价值
   */
  public double evaluateAsDouble(Individual individual);

  /**
   * 以一个字符串形式评价一个个体
   * @param individual 被评价个体
   * @return 评价值
   */
  public String evaluateAsString(Individual individual);

  /**
   * 以一个对象评价一个个体
   * @param individual 被评价个体
   * @return 评价值
   */
  public Object evaluateAsObject(Individual individual);
  

  /**
   * 以一个泛型对象评价一个个体
   * @param individual 被评价个体
   * @param resultBeanCls 评价值泛型实际参数
   * @return 评价值
   */
  public <T> T evaluate(Individual individual,Class<T> resultBeanCls);
  
  
  public boolean isSatisfied(Individual individual);
}
