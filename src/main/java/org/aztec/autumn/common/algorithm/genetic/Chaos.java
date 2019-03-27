package org.aztec.autumn.common.algorithm.genetic;

import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;

/**
 * 该接口模拟由所有个体组成的一个种群环境。命名为chaos(混沌)的原因在于初始化时，所有个体都是由这个接口生成的，犹如天地初生，万物初成的混沌状态。
 * 
 * @author 黎明
 *
 */
public interface Chaos {

  /**
   * 获取最大迭代代数
   * @return 最大种群迭代代数
   */
  public int getMaximumGeneration();
  /**
   * 获取基本种群人口
   * @return 基本种群人口
   */
  public int getPropulation();
  /**
   * 生成第一代种群
   * 
   * @return 第一代种群数据
   */
  public Individual[] generate();
  /**
   * 根据上一代生成的种群数据，重新生成新一代种群数据。生成的过程中，上代种群依旧保留，只有当种群数少于基本人口时，才重新随机生成。
   * 
   * @param individuals 上一代种群数据
   * @return 新一代种群数据
   */
  public Individual[] generateNext(Individual[] individuals);
  /**
   * 获取自然选择器
   * 
   * @return 自然选择器
   */
  public Selector getSelector();
  /**
   * 获取评价器
   * @return 个体评价器
   */
  public Evaluator getEvaluator();
  /**
   * 获取分类器。分类器只是使种群的分布更广泛，更合理，不是必须的
   * @return 分类器
   */
  public Classifier getClassifier();
  /**
   * 判断种群中是否已经存在最优个体，从而主动停止GA
   * @param indiviuduals 测试种群
   * @return
   */
  public boolean isFinished(Individual[] indiviuduals);
  /**
   * 获取混沌中的精英种群
   * @return 精英种群
   */
  public Individual[] getElites();
  /**
   * 获取种群中的精英
   * @return 种群精英
   */
  public Individual getElite();
  /**
   * 设置精英种群
   * @param indiviuduals 精英种群
   */
  public void setElites(Individual[] indiviuduals);
  
}
