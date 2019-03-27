package org.aztec.autumn.common.algorithm.genetic;

import org.aztec.autumn.common.algorithm.Result;

/**
 * 模拟自然界中的每一个个体
 * 
 * @author 黎明
 *
 */
public interface Individual extends Result{

  /**
   * 获取个体身上的基因序列
   * @return 基因序列
   */
  public Gene[] getGenes();
  /**
   * 个体变异
   */
  public void mutate();
  /**
   * 与其它个体交配，生成下一个个体
   * 
   * @param individual 配偶个体
   * @return 子代个体
   */
  public Individual couple(Individual individual);
  
  /**
   * 适配成其它接口,如果适配失败则返回空
   * @param targetCls
   * @return
   */
  public <T> T adapt(Class<T> targetCls);
  
  
}
