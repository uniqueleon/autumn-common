package org.aztec.autumn.common.algorithm.genetic.impl;

import org.aztec.autumn.common.algorithm.genetic.Chaos;
import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.IndividualGenerator;
import org.aztec.autumn.common.algorithm.genetic.Selector;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;
import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;

public abstract class BaseChaos<T> implements Chaos {

	protected Selector selector;
	protected Classifier classifier;
	protected Evaluator evaluator;
	protected int populations;
	protected int maxGeneration;
	protected int currentGeneration;
	protected int genesNum = 1;
	boolean isFinished = false;
	protected Individual[] elites;
	protected IndividualGenerator generator;
	protected TranningConfig config;

	public BaseChaos(TranningConfig config, IndividualGenerator generator,Evaluator evalutaor) {
		this.populations = config.getPopulation();
		this.maxGeneration = config.getMaxGeneration();
		this.currentGeneration = 0;
		this.genesNum = config.getGenesNum();
		this.generator = generator;
		this.config = config;
		this.evaluator = evalutaor;
		selector = new RouletteDoubleSelector();
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
		// for()
		Individual elite = getElite();
		if(evaluator.isSatisfied(elite) || currentGeneration > maxGeneration) {
			isFinished = true; 
		}
		return isFinished;
	}

	@Override
	public Individual[] getElites() {
		return elites;
		// return selector.;
	}

	@Override
	public Individual getElite() {
		double maxScore = -Double.MAX_VALUE;
		int eliteIndex = -1;
		for (int i = 0; i < elites.length; i++) {
			Individual individual = elites[i];
			double score = evaluator.evaluateAsDouble(individual);
			if (maxScore < score) {
				maxScore = score;
				eliteIndex = i;
			}
		}
		if(eliteIndex == -1)
			return null;
		return elites[eliteIndex];
	}

	@Override
	public void setElites(Individual[] indiviuduals) {
		this.elites = indiviuduals;
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
		for (int i = 0; i < populations; i++) {
			elites[i] = generateIndividual();
			if(evaluator.isSatisfied(elites[i])) {
				isFinished = true;
				break;
			}
		}
		return elites;
	}
	
	private Individual generateIndividual() {
		
		return generator.generate();
	}

	@Override
	public Individual[] generateNext(Individual[] individuals) {

		elites = new Individual[populations];
		for (int i = 0; i < populations; i++) {
			if (i < individuals.length) {
				elites[i] = individuals[i];
			} else {
				elites[i] = generateIndividual();
			}
			if(evaluator.isSatisfied(elites[i])){
				isFinished = true;
				break;
			}
		}
		return elites;
	}

}
