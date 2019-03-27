package org.aztec.autumn.common.algorithm.genetic.demo;

import java.util.Arrays;

import org.aztec.autumn.common.algorithm.genetic.Chaos;
import org.aztec.autumn.common.algorithm.genetic.GeneticAlgorithmException;
import org.aztec.autumn.common.algorithm.genetic.GeneticAlgorithmRunner;
import org.aztec.autumn.common.algorithm.genetic.impl.NumericChaos;

/**
 * 这个例子通过多目标遗传算法求二项式的最优值。选用这个例子的原因是求二项式的最大值在中学已经学过，验证容易。
 * <br/><br/>
 * demo例子是:
 * f(x)=-X2+3X-2
 * <br/><br/>
 * 答案是 :当x=1.5(即2/3)时，最大值为0.25(即1/4)
 * <br/><br/>
 * 参数:<br/>
 * 种群数--参与自然选择竞争的个体数，一般数字越大，种群分布越广，越容易逼近最优解<br/>
 * 迭代数--种群迭代数，每迭代一次，种群就会进行优胜劣汰，优秀个体很大机会会被保留下来，同时，个体会进行交配，变异生成下一代。<br/>
 * 基因数--每个个体有决定其性状的基因，基因是决定其对外表现优劣的主要因素。<br/>
 * 基因变化范围--每个基因都有其一定的变化范围，变化范围越广，个体的特征就更多样。<br/>
 * 
 * 
 * @author 黎明
 *
 */
public class BinomialOptimazier {

  public BinomialOptimazier() {
    // TODO Auto-generated constructor stub
  }

  
  public static void main(String[] args) {
    try {
      //种群数300,迭代数100,基因数1(只有1个变量),100是变量变化范围(即-100到100之间)
      Chaos chaos = new NumericChaos(500, 100, 1, 100, new BinomialEvaluator(),null);
      GeneticAlgorithmRunner runner = new GeneticAlgorithmRunner(chaos);
      Long curTime = System.currentTimeMillis();
      runner.execute();
      System.out.println("The final result is X=" + Arrays.toString(chaos.getElite().getGenes()));
      System.out.println("fianl score S=" + chaos.getEvaluator().evaluateAsDouble(chaos.getElite()));
      Long elaspeTime = System.currentTimeMillis() - curTime;
      System.out.println("use time:" + elaspeTime);
    } catch (GeneticAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
