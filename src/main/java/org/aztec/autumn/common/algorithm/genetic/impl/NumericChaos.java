package org.aztec.autumn.common.algorithm.genetic.impl;

import org.aztec.autumn.common.algorithm.genetic.Chaos;
import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.Selector;
import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;

public class NumericChaos implements Chaos {
  
  private Selector selector;
  private Classifier classifier;
  private Evaluator evaluator;
  private int populations;
  private int maxGeneration;
  private int currentGeneration;
  private int genesNum = -1;
  private Class geneType;
  private long geneSeed;
  private long[] ranges;
  private Individual[] elites;

  public NumericChaos(int pops,int maxGeneration,int genesNum,long seed,Evaluator evaluator,Classifier classifier) {
    // TODO Auto-generated constructor stub
    this.populations = pops;
    this.maxGeneration = maxGeneration;
    this.evaluator = evaluator;
    this.classifier = classifier;
    this.genesNum = genesNum;
    selector = new RouletteDoubleSelector();
    geneSeed = seed;
  }
  
  public NumericChaos(int pops,int maxGeneration,int genesNum,long seed,Class geneType,Evaluator evaluator,Classifier classifier) {
    // TODO Auto-generated constructor stub
    this.populations = pops;
    this.maxGeneration = maxGeneration;
    this.evaluator = evaluator;
    this.classifier = classifier;
    this.genesNum = genesNum;
    selector = new RouletteDoubleSelector();
    geneSeed = seed;
    this.geneType = geneType;
  }
  
  public NumericChaos(NumericChaosConfiguration config){
    this.populations = config.getPopulation();
    this.maxGeneration = config.getGeneration();
    this.evaluator = config.getEvaluator();
    this.classifier = config.getClassifier();
    this.genesNum = config.getGeneNum();
    selector = new RouletteDoubleSelector();
    geneSeed = config.getRange();
    this.geneType = config.getGeneType();
  }
  

  @Override
  public int getMaximumGeneration() {
    return maxGeneration;
  }

  @Override
  public int getPropulation() {
    return populations;
  }

  @Override
  public Individual[] generate() {
    elites = new Individual[populations];
    for(int i = 0;i < populations;i++){
      if(geneType != null)
        elites[i] = new NumericVectorIndividual(genesNum,geneSeed,geneType);
      else
        elites[i] = new NumericVectorIndividual(genesNum,geneSeed);
    }
    return elites;
  }

  @Override
  public Individual[] generateNext(Individual[] individuals) {

    elites = new Individual[populations];
    for(int i = 0;i < populations;i++){
      if(i < individuals.length){
        elites[i] = individuals[i];
      }
      else{
        if(geneType != null)
          elites[i] = new NumericVectorIndividual(genesNum,geneSeed,geneType);
        else
          elites[i] = new NumericVectorIndividual(genesNum,geneSeed);
      }
    }
    return elites;
  }

  @Override
  public Selector getSelector() {
    return selector;
  }

  @Override
  public Evaluator getEvaluator() {
    return evaluator;
  }

  @Override
  public Classifier getClassifier() {
    return classifier;
  }
  
  @Override
  public boolean isFinished(Individual[] indiviuduals) {
    //for()
    return currentGeneration > maxGeneration;
  }

  @Override
  public Individual[] getElites() {
    return elites;
    //return selector.;
  }

  @Override
  public Individual getElite() {
    double maxScore = - Double.MAX_VALUE;
    int eliteIndex = -1;
    for(int i = 0;i < elites.length;i++){
      Individual individual = elites[i];
      double score = evaluator.evaluateAsDouble(individual);
      if(maxScore < evaluator.evaluateAsDouble(individual)){
        maxScore = score;
        eliteIndex = i;
      }
    }
    return elites[eliteIndex];
  }

  @Override
  public void setElites(Individual[] indiviuduals) {
    this.elites = indiviuduals;
  }

}
