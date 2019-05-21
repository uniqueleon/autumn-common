package org.aztec.autumn.common.math.equations.impl.de;

import java.util.List;
import java.util.Stack;

import org.aztec.autumn.common.math.equations.ConstrainedDEAlgorithm;
import org.aztec.autumn.common.math.equations.SortableNumber;
import org.aztec.autumn.common.math.equations.SortableNumber.Ordering;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;

public class CongruenceAlgorithm extends BaseConstraintDEAlgorithm implements ConstrainedDEAlgorithm {

	public CongruenceAlgorithm() {
		// TODO Auto-generated constructor stub
	}


	@Override
	protected List<Long> doFind(Long[] factors, Long result, Long[][] ranges, List<List<Long>> histories) {
		// TODO Auto-generated method stub
		return findSolutionInSpecialWay(factors, result, ranges);
	}


	private List<Long> findSolutionInSpecialWay(Long[] factors, Long result, Long[][] ranges) {
		List<SortableNumber> numbers = SortableNumber.sort(factors, Ordering.ASC);
		SortableNumber targetNumber = numbers.get(0);
		Long modular = targetNumber.getNumber().longValue();
		List<Long> possibleSolution = Lists.newArrayList();
		Long tmpResult = result;
		setContextObject(ContextKeys.MODULAR, modular);
		initCongruenceSolution(factors, ranges, modular, result);
		setContextObject(ContextKeys.SKIP_INDEX, targetNumber.getIndex());
		possibleSolution = pickUpFinalResult(factors, ranges, tmpResult);
		return possibleSolution;
	}
	
	
	@Override
	protected boolean isFinalRound(int round) {
		Long[] factors = getContextObject(ContextKeys.NEW_FACTORS);
		if(round == factors.length) {
			return true;
		}
		return false;
	}


	@Override
	protected Integer getMaxRound(Long[] factors) {
		// TODO Auto-generated method stub
		return factors.length + 1;
	}


	@Override
	protected List<Long> findFinalSolutions(Stack<SearchStep> searchPath, Long result, Long[] factors, Long[][] ranges,
			int round) {
		Long modular = getContextObject(ContextKeys.MODULAR);
		Integer targetIndex = getContextObject(ContextKeys.SKIP_INDEX);
		List<Long> solutions = toSolutions(searchPath);
		Long tempResult = recalculateResult(result, solutions, factors);
		Long finalSolution = tempResult / modular;
		if (tempResult % modular == 0 && finalSolution <= ranges[targetIndex][1]
				&& finalSolution >= ranges[targetIndex][0]
						&& isValid(round, factors, result, solutions, finalSolution, ranges)) {
			solutions.set(targetIndex, finalSolution);
			return solutions;
		} else {
			return null;
		}
	}
	

	@Override
	protected List<Long> getCandiadateSolution(int round,Long result) {
		return getCandiadateSolution2(round, result);
	}
	
	protected List<Long> getCandiadateSolution2(int round,Long result) {
		Long[] factors = super.getNewFactors();
		Long modular = getContextObject(ContextKeys.MODULAR);
		Long[][] ranges = getContextObject(ContextKeys.SOLUTION_RANGES);
		Long[] range = ranges[round];
		Integer skipIndex = getContextObject(ContextKeys.SKIP_INDEX);
		List<SolutionInformation> solutionInfos = getContextObject(ContextKeys.SOLUTION_INFORMATION);
		SolutionInformation thisInfo = solutionInfos.get(round);
		Long maxiumSolutionSize = range[1] - range[0] + 1;
		List<Long> newSolutions  = Lists.newArrayList();
		Long leastRemainder = MathUtils.getLeastRemainder(result, modular);
		if(skipIndex != null && skipIndex == round) {
			newSolutions.add(0l);
		}
		else if(round == factors.length - 1) {
			
			List<Long> possibleSolution = thisInfo.getSolutions().get(leastRemainder);
			return (List<Long>) (possibleSolution == null ? Lists.newArrayList() : possibleSolution);
		}
		else {
			
			int closestIndex = -1;
			List<Long> possibleRemainders = thisInfo.getPossibleRemainders();
			int cursor = 0;
			Long testRemainder = possibleRemainders.get(cursor);
			while(testRemainder > leastRemainder) {
				cursor++;
				if(cursor >= possibleRemainders.size() ) {
					cursor = 0;
					break;
				}
				testRemainder = possibleRemainders.get(cursor);
			}
			closestIndex = cursor;
			for(int i = closestIndex ; i < possibleRemainders.size();i++) {
				List<Long> possibleSolution = thisInfo.getSolutions().get(possibleRemainders.get(i));
				if(possibleSolution != null) {
					mergedList(newSolutions, possibleSolution);
				}
				if (newSolutions.size() >= maxiumSolutionSize) {
					break;
				}
			}
			if (newSolutions.size() < maxiumSolutionSize) {
				for(int i = 0; i < closestIndex;i++) {
					List<Long> possibleSolution = thisInfo.getSolutions().get(possibleRemainders.get(i));
					if(possibleSolution != null) {
						mergedList(newSolutions, possibleSolution);
					}
					if (newSolutions.size() >= maxiumSolutionSize) {
						break;
					}
				}
			}
		}
		return newSolutions;
	}
	
	protected List<Long> getSpecialIndexNewSolutions(Long[] range){
		List<Long> newSolutions = Lists.newArrayList();
		newSolutions.add(0l);
		return newSolutions;
	}

	@Override
	protected Integer getSpecialIndex() {
		return getContextObject(ContextKeys.SKIP_INDEX);
	}

	private void mergedList(List<Long> l1, List<Long> l2) {
		for (int i = 0; i < l2.size(); i++) {
			if (!l1.contains(l2.get(i))) {
				l1.add(l2.get(i));
			}
		}
	}
	

}
