package org.aztec.autumn.common.math.equations.impl.ga;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.GeneGenerator;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;
import org.aztec.autumn.common.math.equations.SortableNumber;
import org.aztec.autumn.common.math.equations.SortableNumber.Ordering;

public class DEGeneGenerator implements GeneGenerator{
	
	TranningConfig config;

	public DEGeneGenerator(TranningConfig config) {
		this.config = config;
	}

	@Override
	public Gene generate(int index, TranningConfig config) {
		Long[][] ranges = (Long[][])
				config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.SOLUTION_RANGES);
		Long[][] factors = (Long[][]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.SOLVING_FACTORS);
		Long factor = factors[index][0];
		Long newSolution = 0l;
		if(isSelectable(index)) {
			factor = findFactor(index, config);
			if(factor != null){
				Long solutionRange = ranges[index][1] - ranges[index][0] + 1;
				int step = RandomUtils.nextInt(solutionRange.intValue());
				newSolution = ranges[index][0] + step;
			}
			else {
				factor =  factors[index][0];
				newSolution = 0l;
			}
		}
		return new IDEGene(factor, newSolution, factors[index], ranges[index],config);
	}
	
	private Long findFactor(int index, TranningConfig config) {
		Long[][] factors = (Long[][]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.SOLVING_FACTORS);
		Long[] factorLimits = (Long[]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.FACTOR_LIMIT);
		Long targetResult = (Long) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.TARGET_RESULT);
		Long[] thisFactors = factors[index];
		int chooseIndex = RandomUtils.nextInt(factors[index].length);
		Long factor = null;
		Integer maxTryTime = factors[index].length * factors[index].length;
		do {
			factor = thisFactors[chooseIndex];
			if(targetResult < factor) {
				chooseIndex = RandomUtils.nextInt(factors[index].length);
				maxTryTime --;
				factor = null;
				continue;
			}
			Long[] otherFactors = new Long[thisFactors.length - 1];
			for(int i = 0;i < thisFactors.length;i++) {
				if(i == chooseIndex)
					continue;
				if(i < chooseIndex) {
					otherFactors[i] = thisFactors[i];
				}
				else {
					otherFactors[i - 1] = thisFactors[i];
				}
			}
			List<SortableNumber> sortNumbers = SortableNumber.sort(otherFactors, Ordering.ASC);
			for(int i = 1; i < factorLimits.length;i++) {
				if(sortNumbers.get(i - 1).getNumber() > factorLimits[i]) {
					chooseIndex = RandomUtils.nextInt(factors[index].length);
					maxTryTime --;
					factor = null;
					continue;
				}
			}
			break;
		}while(maxTryTime > 0);
		return factor;
	}

	private boolean isSelectable(int index) {
		Long[] factorLimits = (Long[]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.FACTOR_LIMIT);
		Long[][] factors = (Long[][]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.SOLVING_FACTORS);

		List<SortableNumber> sortNumbers = 
				SortableNumber.sort(factors[index], Ordering.ASC);
		List<SortableNumber> sortLimits = 
				SortableNumber.sort(factorLimits, Ordering.ASC);
		for(int i = 0;i < sortLimits.size();i++) {
			if(sortNumbers.get(i).getNumber() > sortLimits.get(i).getNumber()) {
				return false;
			}
		}
		return true;
	}


}
