package org.aztec.autumn.common.algorithm.genetic.impl;

import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;

public class NumericChaosConfiguration {

  private int population;
  private long range;
  private int geneNum;
  private int generation;
  private Class geneType;
  private Evaluator evaluator;
  private Classifier classifier;
  
  public static final int POPULATION_INDEX = 0;
  public static final int GENERATION_INDEX = 1;
  public static final int GENE_NUMBER_INDEX = 2;
  public static final int GENE_VARIA_RANGE_INDEX = 3;
  public static final int GENE_TYPE_INDEX = 4;
  public static final int EVALUATOR_INDEX = 5;
  public static final int CLASSIFIER_INDEX = 6;
  

  public NumericChaosConfiguration(Object[] configParams) {
    this.population = (int) configParams[POPULATION_INDEX];
    this.generation = (int) configParams[GENERATION_INDEX];
    this.geneNum = (int) configParams[GENE_NUMBER_INDEX];
    this.range = Long.parseLong(configParams[GENE_VARIA_RANGE_INDEX] + "");
    this.geneType = (Class) configParams[GENE_TYPE_INDEX];
    this.evaluator = adapt(Evaluator.class,configParams[EVALUATOR_INDEX]);
    if(configParams.length > 5)
      this.classifier = adapt(Classifier.class,configParams[CLASSIFIER_INDEX]);
  }
  
  public <T> T adapt(Class<T> infCls,Object targetObj){
    if(targetObj == null)return null;
    if (infCls.isAssignableFrom(targetObj.getClass()))
      return (T) targetObj;
    else
      return null;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public long getRange() {
    return range;
  }

  public void setRange(long range) {
    this.range = range;
  }

  public int getGeneNum() {
    return geneNum;
  }

  public void setGeneNum(int geneNum) {
    this.geneNum = geneNum;
  }

  public int getGeneration() {
    return generation;
  }

  public void setGeneration(int generation) {
    this.generation = generation;
  }

  public Class getGeneType() {
    return geneType;
  }

  public void setGeneType(Class geneType) {
    this.geneType = geneType;
  }

  public Evaluator getEvaluator() {
    return evaluator;
  }

  public void setEvaluator(Evaluator evaluator) {
    this.evaluator = evaluator;
  }

  public Classifier getClassifier() {
    return classifier;
  }

  public void setClassifier(Classifier classifier) {
    this.classifier = classifier;
  }

}
