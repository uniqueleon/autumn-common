package org.aztec.autumn.common.algorithm.genetic;

/**
 * 种群筛选器，主要用来模拟自然界适者生存，优胜劣汰的自然法则。
 * 
 * @author 黎明
 *
 */
public interface Selector {

  /**
   * 根据竞争力评价器用筛选精英群体
   * 
   * @param individuals 筛选样品
   * @param evaluators 评价器
   * @return 精英种群
   * @throws GeneticAlgorithmException
   */
  public Individual[] select(Individual[] individuals,Evaluator evaluators) throws GeneticAlgorithmException;
}
