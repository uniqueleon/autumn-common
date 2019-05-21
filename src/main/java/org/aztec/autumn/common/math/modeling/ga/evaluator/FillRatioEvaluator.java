package org.aztec.autumn.common.math.modeling.ga.evaluator;

import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.math.modeling.ga.AlgorithmConst;
import org.aztec.autumn.common.math.modeling.ga.PackingPlan;
import org.aztec.autumn.common.math.modeling.ga.PackingStep;
import org.aztec.autumn.common.math.modeling.ga.SolutionEvaluator;

public class FillRatioEvaluator implements SolutionEvaluator {

	@Override
	public double getScore(Individual individual) {
		PackingPlan plan = individual.adapt(PackingPlan.class);
		if(plan.isPerfect()){
			return AlgorithmConst.MAXIUM_SCORE;
		}
		double totalVolumn = 0;
		for(Gene gene : plan.getGenes()){
			PackingStep ps = (PackingStep) gene;
			totalVolumn += ps.getLength() * ps.getWidth() * ps.getHeight() * ps.getNum();
		}
		return totalVolumn;
	}

	@Override
	public double getWeight() {
		return 1.0;
	}


}
