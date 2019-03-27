package org.aztec.autumn.common.algorithm.genetic.advance;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.algorithm.genetic.Individual;

/**
 * 种群分类型，可以将种群进行分类。例如将一个男性成人的身高从120-160分类成偏矮种群，160-180分类成中等身材种群，180-210分类成偏高身材种群。
 * 直如我一篇已经刊载的论文所言，完全随机遗传算法有一个很致命的毛病，就是当目标函数有多个极值点时，完全随机遗传算法，很容易会过快收敛于某一个极值点(特别是多目标遗传算法)。
 * 导致出现过分收敛的原因是因为种群分布不够广泛，绝大部分个体处于相同的收敛域，因此算法很快就收敛到该极值。（这是所有收敛算法的通病，不能避免）
 * 此分类器的作用就是通过一些已知的先验知识（例如一个人的身高体重分类），人为地将初代种群合理地分布在多个不同的收敛域中，从而达到最佳的收敛效果。
 * 
 * 
 * @author 黎明
 *
 */
public interface Classifier {

  /**
   * 将种群分类成不同种群的列表
   * 
   * @param individuals 待分类种群
   * @return 种群列表
   */
  public List<Individual[]> classifyAsList(Individual[] individuals);
  /**
   * 将种群分类成以某一个对象为键值的映射
   * 
   * @param individuals 待分类种群
   * @return 种群映射
   */
  public Map<Object,Individual[]> classifyAsMap(Individual[] individuals);
  /**
   * 获取种群分布值。种群分布值用于裁定当前种群的分布是否合理均匀
   * 
   * @param individuals 待分类种群
   * @return 种群分布值
   */
  public long getGeneDistribution(Individual[] individuals);
  /**
   * 获取最小基本分布值
   * 
   * @return 最小基本分布值
   */
  public long getMinimumDistribution();
  /**
   * 判断两个个体是否属于同一类群
   * 
   * @param individual 个体1
   * @param otherIndividual 个体2
   * @return true，是,false,不是
   */
  public boolean belongsToSameClass(Individual individual,Individual otherIndividual);
}
