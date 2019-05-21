package org.aztec.autumn.common.algorithm.genetic.impl;

import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;

public abstract class BaseEvaluator implements Evaluator {
	
	TranningConfig config;

	public BaseEvaluator(TranningConfig config) {
		this.config = config;
	}

	@Override
	public boolean isSatisfied(Individual individual) {
		double score = evaluateAsDouble(individual);
		if(score > config.getAcceptableScore()) {
			return true;
		}
		return false;
	}

}
