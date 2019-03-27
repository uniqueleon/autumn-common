package org.aztec.autumn.common.algorithm.genetic.demo;

import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;

public class BinomialEvaluator implements Evaluator {

	public BinomialEvaluator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluateAsDouble(Individual individual) {
		//
		// X=1.5 Ymax=0.25
		Double x = (Double) individual.getGenes()[0].get();
		double result = (-1) * x * x + 3 * x - 2;

		return result;
	}

	@Override
	public String evaluateAsString(Individual individual) {
		Double x = (Double) individual.getGenes()[0].get();
		return null;
	}

	@Override
	public Object evaluateAsObject(Individual individual) {
		return null;
	}

	@Override
	public <T> T evaluate(Individual individual, Class<T> resultBeanCls) {
		return null;
	}

	@Override
	public boolean isSatisfied(Individual individual) {
		return true;
	}

}
