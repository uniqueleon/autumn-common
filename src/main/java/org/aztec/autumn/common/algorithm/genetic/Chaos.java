package org.aztec.autumn.common.algorithm.genetic;

import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;

public interface Chaos {

  public int getMaximumGeneration();
  public int getPropulation();
  public Individual[] generate();
  public Individual[] generateNext(Individual[] individuals);
  public Selector getSelector();
  public Evaluator getEvaluator();
  public Classifier getClassifier();
  public boolean isFinished(Individual[] indiviuduals);
  public Individual[] getElites();
  public Individual getElite();
  public void setElites(Individual[] indiviuduals);
  
}
