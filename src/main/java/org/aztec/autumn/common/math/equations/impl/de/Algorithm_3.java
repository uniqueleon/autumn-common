package org.aztec.autumn.common.math.equations.impl.de;

import java.util.List;

import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;

import com.beust.jcommander.internal.Lists;

public class Algorithm_3 extends BaseConstraintDEAlgorithm {

	@Override
	protected List<Long> doFind(Long[] factors, Long result, Long[][] ranges, List<List<Long>> histories) {
		// TODO Auto-generated method stub
		List<Long> baseSolution = getContextObject(ContextKeys.BASE_SOLUTION);
		List<GreatestCommonDivisor> seqGcd = getContextObject(ContextKeys.SEQUENCE_GCD);
		
		List<Long> tempSolution = Lists.newArrayList();
		tempSolution.addAll(baseSolution);
		List<Long> finedSolution = adjustSolution(tempSolution, ranges, seqGcd);
		Integer lastIndex = factors.length - 1;
		Long lastSolution = finedSolution.get(lastIndex);
		if(lastSolution >= ranges[lastIndex][0] && lastSolution <= ranges[lastIndex][1]) {
			return finedSolution;
		}
		else {
			
		}
		return null;
	}
	
	private List<Long> adjustSolution(List<Long> testSolution,Long[][] ranges,List<GreatestCommonDivisor> seqGcd) {

		List<Long> tempSolution = Lists.newArrayList();
		for(int i = 0;i < testSolution.size() - 1;i++) {

			Long gcd = seqGcd.get(i).getGcd();
			Long nextGcd = seqGcd.get(i + 1).getGcd();
			Long currentSolution = tempSolution.get(i);
			Long nextSolution = tempSolution.get(i + 1);
			Long effector1 = nextGcd / gcd;
			Long effector2 = nextGcd / gcd;
			// 0 13 7 -6 -13
			Long step = (ranges[i][0] - currentSolution / effector1 );
			currentSolution += step * effector1;
			if(currentSolution < ranges[i][0]) {	
				step ++;
				currentSolution += effector1;
			}
			nextSolution += step * effector2;
			tempSolution.set(i, currentSolution);
			tempSolution.set(i + 1, nextSolution);
		}
		return tempSolution;
	}

}
