package org.aztec.autumn.common.algorithm.genetic.impl;

import org.aztec.autumn.common.algorithm.genetic.Chaos;
import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.IndividualGenerator;
import org.aztec.autumn.common.algorithm.genetic.Selector;
import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;

public abstract class AbstractChaos implements Chaos{

	  private Selector selector;
	  private Classifier classifier;
	  private Evaluator evaluator;
	  private IndividualGenerator individualGenerator;
	  private int populations;
	  private int maxGeneration;
	  private int currentGeneration;
	  private Individual[] elites;
	  
	  public AbstractChaos(IndividualGenerator generator,Evaluator evaluator,Selector selector) {
		  this.selector = selector;
		  this.individualGenerator = generator;
		  this.evaluator = evaluator;
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
	        elites[i] = individualGenerator.generate();
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
	    	  elites[i] = individualGenerator.generate();
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
