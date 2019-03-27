package org.aztec.autumn.common.algorithm.genetic.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.Selector;
import org.aztec.autumn.common.algorithm.genetic.SelectorException;

/**
 * 轮盘赌筛选器
 * 
 * @author 黎明
 *
 */
public class RouletteDoubleSelector implements Selector {

  public RouletteDoubleSelector() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public Individual[] select(Individual[] individuals, Evaluator evaluators)
      throws SelectorException {
    //初始化
    List<Score> scores = init(individuals,evaluators);
    //将负分变成正分，为了方便后面求分布率
    changeToPositive(scores);
    //求分布率
    caculateRatio(scores);
    //轮盘赌
    List<Individual> elites = rouletteGamble(scores);
    return elites.toArray(new Individual[elites.size()]);
  }
  
  public List<Score> init(Individual[] individuals, Evaluator evaluators) throws SelectorException{
    List<Score> scores = new ArrayList<Score>();
    for (Individual individual : individuals) {
      double score = evaluators.evaluateAsDouble(individual);
      scores.add(new Score(score,individual));
    }
    Collections.sort(scores);
    return scores;
  }
  
  public void changeToPositive(List<Score> scores){
    double minumValue = scores.get(0).get();
    if(minumValue < 0){
      for(int i = 0;i < scores.size();i++){
        scores.get(i).set(scores.get(i).get() - minumValue);
      }
    }
  }
  
  public void caculateRatio(List<Score> scores){
    double totalScore = 0;
    for(Score score : scores){
      totalScore += score.get();
    }
    for(Score score : scores){
      score.set(score.get() / totalScore);
    }
  }
  
  public List<Individual> rouletteGamble(List<Score> scores){
    List<Individual> elites = new ArrayList<Individual>();
    Random random = new Random();
    for (int i = 0; i < scores.size(); i++) {
      double randomNum = random.nextDouble() ;
      for(int j = 0;j < scores.size();j++){
        Score currentScore = scores.get(j);
        if(currentScore.get() < randomNum){
          if(j == scores.size() - 1){
            if(!elites.contains(currentScore.getIndividual()))
              elites.add(currentScore.getIndividual());
          }
          else{
            if(randomNum < scores.get(j + 1).get()){
              if(!elites.contains(currentScore.getIndividual()))
                elites.add(currentScore.getIndividual());
            }
          }
        }
      }
    }
    return elites;
  }
  

  private static class Score implements Comparable<Score>{
    double score;
    Individual individual;

    public Score(double index, Individual individual) throws SelectorException {
      this.score = index;
      this.individual = individual;
    }
    
    public void set(double score){
      this.score = score;
    }

    public double get() {
      return score;
    }

    public Individual getIndividual() {
      return individual;
    }

    @Override
    public int compareTo(Score o) {
      if(score - o.get() == 0)
        return 0;
      return score - o.get() > 0 ? 1 : -1;
    }
    
  }

}
