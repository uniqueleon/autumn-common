package org.aztec.autumn.common.algorithm.genetic;

public interface Selector {

  public Individual[] select(Individual[] individuals,Evaluator evaluators) throws GeneticAlgorithmException;
}
