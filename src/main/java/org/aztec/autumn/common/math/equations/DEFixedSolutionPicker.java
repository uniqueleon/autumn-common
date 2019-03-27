package org.aztec.autumn.common.math.equations;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;

import com.google.common.collect.Lists;

public class DEFixedSolutionPicker {
	

	private DiophantineResult equationResult;

	public DEFixedSolutionPicker(DiophantineResult equationResult) {
		this.equationResult = equationResult;
	}


	
	public void findSolutionsAsSpecified(Map<DESolutionConstraintType, DESolutionConstraint> constraints) {

		List<Long> coefficients = equationResult.getCoefficients();
		List<List<Long>> solutions = equationResult.getSolutions();
		Long[][] fixedSolutions = (Long[][]) constraints.get(DESolutionConstraintType.FIXED_SOLUTION).getParams()[0];
		Long[] factors = new Long[coefficients.size()];
		coefficients.toArray(factors);
		List<GreatestCommonDivisor> gcds = GreatestCommonDivisor.getSequenceGCD(factors);
		Long tempResult = new Long(equationResult.getResult());
		List<Long> newFoundSolution = Lists.newArrayList();
		
		int tryTime = 0;
		for(int i = 0;i < fixedSolutions.length;i++) {
			Long[] fixedSolution = fixedSolutions[i];
			if(fixedSolution != null) {
				tryTime += fixedSolution.length;
			}
		}
		do {
			newFoundSolution = findFixedSolution(0, 0, tempResult, newFoundSolution, fixedSolutions, gcds);
			if(newFoundSolution != null && newFoundSolution.size() > 0) {
				solutions.add(newFoundSolution);
			}
			newFoundSolution = Lists.newArrayList();
			tryTime --;
		} while(newFoundSolution != null && tryTime > 0);
		
	}
	
	private List<Long> findFixedSolution(int i,int j,Long tempResult, List<Long> solution,Long[][] fixedResult,List<GreatestCommonDivisor> gcds) {
		

		List<Long> coefficients = equationResult.getCoefficients();
		List<Long> baseResults = equationResult.getBaseResults();
		List<Long> trySolution = Lists.newArrayList(solution);
		if(j == fixedResult[i].length) {
			return null;
		}
		List<Long> result = null;
		Long testResult = fixedResult[i][j];
		Long newTmpResult = tempResult ;
		GreatestCommonDivisor gcd = gcds.get(i);
		if(equationResult.hasSolution(newTmpResult, gcd)) {
			if(i == baseResults.size() - 1) {
				if(newTmpResult % gcd.getGcd() == 0) {
					trySolution.add(newTmpResult/ gcd.getGcd());
					if( !equationResult.isDuplicate(trySolution)) {
						return trySolution;
					}
					else {
						return null;
					}
				}
				else {
					return null;
				}
			}
			else {
				newTmpResult -= testResult * coefficients.get(i);
				trySolution.add(testResult);
			}
			result = findFixedSolution(i + 1, j, newTmpResult,trySolution,fixedResult,gcds);
			if(result == null) {
				trySolution = Lists.newArrayList(solution);
			}
			while(j < fixedResult[i].length - 1 && result == null) {
				j++;
				result = findFixedSolution(i, j, newTmpResult, trySolution, fixedResult, gcds);
			}
		}
		else {
			return null;
		}
		return result;
	}
	
	
	private boolean hasAdditionalSolutions(Map<DESolutionConstraintType, DESolutionConstraint> constraints) {


		List<Long> coefficients = equationResult.getCoefficients();
		List<List<Long>> solutions = equationResult.getSolutions();
		List<Long> baseResults = equationResult.getBaseResults();
		
		return true;
	}
}
