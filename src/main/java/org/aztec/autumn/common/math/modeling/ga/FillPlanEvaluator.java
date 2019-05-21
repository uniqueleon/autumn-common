package org.aztec.autumn.common.math.modeling.ga;

import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.math.modeling.ga.evaluator.SolutionEvaluatorFactory;

public class FillPlanEvaluator implements Evaluator {

	@Override
	public double evaluateAsDouble(Individual individual) {
		PackingPlan plan = individual.adapt(PackingPlan.class);
		double score = 0;
		if(plan != null){
			SolutionEvaluatorFactory evalutorFactory = SolutionEvaluatorFactory.getInstance();
			for(SolutionEvaluator se : evalutorFactory.getEvaluators()){
				score += (se.getScore(individual) * se.getWeight());
			}
		}
		return score;
	}

	@Override
	public String evaluateAsString(Individual individual) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object evaluateAsObject(Individual individual) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T evaluate(Individual individual, Class<T> resultBeanCls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSatisfied(Individual individual) {
		double score = evaluateAsDouble(individual);
		return score >= AlgorithmConst.MAXIUM_SCORE;
	}

}
