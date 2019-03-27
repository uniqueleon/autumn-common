package org.aztec.autumn.common.math.equations.impl.ga;

import java.util.Collections;
import java.util.List;

import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;

import com.google.common.collect.Lists;

public class DEEvaluator implements Evaluator{
	
	private TranningConfig config;
	

	public DEEvaluator(TranningConfig config) {
		this.config = config;
	}

	@Override
	public double evaluateAsDouble(Individual individual) {
		
		Long targetResult = (Long) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.TARGET_RESULT);
		if(individual instanceof DiophatineEquationIndividual) {
			DiophatineEquationIndividual ind = (DiophatineEquationIndividual) individual;
			if(!isGeneValid(ind.getGenes())) {
				return 0;
			}
			Long equationValue = ind.getResult();
			if(equationValue > targetResult)
				return 0;
			return 1d / (targetResult - equationValue + 1);
		}
		return 0;
	}
	
	private boolean isGeneValid(Gene[] genes) {

		Long[] factorLimits = (Long[]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.FACTOR_LIMIT);
		Long[][] factors = (Long[][]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.SOLVING_FACTORS);

		for(int i = 0 ;i < genes.length;i++) {
			IDEGene gene = (IDEGene) genes[i];
			Long thisFactor = gene.getFactor();
			if(thisFactor > factorLimits[0]) {
				return false;
			}
			boolean found = false;
			Long[] thisFactors = factors[i];
			List<Long> sortFactors = Lists.newArrayList();
			for(int j = 0;j < thisFactors.length;j++) {
				if(!found && thisFactors[j] == thisFactor) {
					found = true;
					continue;
				}
				sortFactors.add(thisFactors[j]);
			}
			Collections.sort(sortFactors);
			List<Long> compareFactors = Lists.newArrayList();
			for(int j = 1;j < factorLimits.length;j++) {
				compareFactors.add(factorLimits[j]);
			}
			Collections.sort(compareFactors);
			for(int j = 0;j < factorLimits.length - 1;j++) {
				if(sortFactors.get(j) > compareFactors.get(j)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String evaluateAsString(Individual individual) {
		// TODO Auto-generated method stub
		return "" + evaluateAsDouble(individual);
	}

	@Override
	public Object evaluateAsObject(Individual individual) {
		// TODO Auto-generated method stub
		return evaluateAsDouble(individual);
	}

	@Override
	public <T> T evaluate(Individual individual, Class<T> resultBeanCls) {
		// TODO Auto-generated method stub
		return (T) new Double( evaluateAsDouble(individual));
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
