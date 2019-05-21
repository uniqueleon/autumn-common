package org.aztec.autumn.common.math.modeling.ga;

import org.aztec.autumn.common.algorithm.genetic.Individual;

public interface SolutionEvaluator {

	public double getScore(Individual individual);
	public double getWeight();
}
