package org.aztec.autumn.common.math.equations.impl.de;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.aztec.autumn.common.math.equations.ConstrainedDEAlgorithm;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.math.equations.impl.de.CustomerizedValidator.PARAMETER_KEYS;
import org.aztec.autumn.common.math.equations.impl.de.CustomerizedValidator.ValidateParameter;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class BaseConstraintDEAlgorithm implements
		ConstrainedDEAlgorithm {

	protected static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

	protected static class ContextKeys {
		public static final String REMAINDER_COMBINATION_SELECTOR = "remainderCombineSelector";
		public static final String SOLUTION_INFORMATION = "solutionInfos";
		public static final String SLOW_REMAINDER_LIST = "slowList";
		public static final String TOLERANT_COUNT = "tolerantNumber";
		public static final String SEQUENCE_GCD = "sequenceGCDs";
		public static final String FAIL_LIST = "failList";
		public static final String OLD_FACTORS = "oldFactors";
		public static final String NEW_FACTORS = "newFactors";
		public static final String OLD_RESULT = "oldResult";
		public static final String NEW_RESULT = "newResult";
		public static final String TEMP_RESULT = "tmpResult";
		public static final String NEW_GCD = "newGcd";
		public static final String OLD_GCD = "oldGcd";
		public static final String BASE_SOLUTION = "baseSolution";
		public static final String SOLUTION_RANGES = "ranges";
		public static final String CANDIDATE_SOLUTIONS = "candidateSolutions";
		public static final String SKIP_INDEX = "skipIndex";
		public static final String MODULAR = "modular";
		// public static final String SOLUTIONS_SELECTOR = ""
		public static final String LAST_FINISHED_STEPS = "lastFinishedStep";
	}

	protected abstract List<Long> doFind(Long[] factors, Long result,
			Long[][] ranges, List<List<Long>> histories);

	@Override
	public List<Long> findSolution(Long[] factors, Long result,
			Long[][] ranges, List<Long> baseResult, List<List<Long>> histories) {
		init(factors, ranges, baseResult, result);
		List<Long> solutions = doFind(getNewFactors(), getNewResult(), ranges,
				histories);
		context.set(null);
		return solutions;
	}

	@Override
	public List<List<Long>> findSolutions(Long[] factors, Long result,
			Long[][] ranges, List<Long> baseResult, int solutionNum) {
		// TODO Auto-generated method stub
		List<List<Long>> solutions = Lists.newArrayList();
		int count = solutionNum;
		init(factors, ranges, baseResult, result);
		while (count > 0) {
			List<Long> solution = doFind(getNewFactors(), getNewResult(),
					ranges, new ArrayList<List<Long>>());
			if (solution == null)
				count = 0;
			else {
				solutions.add(solution);
				count--;
			}
		}
		context.set(null);
		return solutions;
	}

	protected void init(Long[] factors, Long[][] ranges,
			List<Long> baseSolution, Long result) {
		if (context.get() == null) {
			context.set(new HashMap<String, Object>());
		}
		List<GreatestCommonDivisor> allGcd = GreatestCommonDivisor
				.getSequenceGCD(factors);
		long gcd = allGcd.get(0).getGcd();
		Long[] newFactors = new Long[factors.length];
		for (int i = 0; i < factors.length; i++) {
			newFactors[i] = factors[i] / gcd;
		}
		Long newResult = result / gcd;
		List<GreatestCommonDivisor> newGcd = GreatestCommonDivisor
				.getSequenceGCD(newFactors);
		setContextObject(ContextKeys.NEW_FACTORS, newFactors);
		setContextObject(ContextKeys.OLD_FACTORS, factors);
		setContextObject(ContextKeys.NEW_RESULT, newResult);
		setContextObject(ContextKeys.OLD_RESULT, result);
		setContextObject(ContextKeys.SEQUENCE_GCD, newGcd);
		setContextObject(ContextKeys.OLD_GCD, allGcd);
		setContextObject(ContextKeys.FAIL_LIST, Lists.newArrayList());
		setContextObject(ContextKeys.BASE_SOLUTION, baseSolution);
		setContextObject(ContextKeys.SOLUTION_RANGES, ranges);
	}

	protected Long getNewResult() {
		return getContextObject(ContextKeys.NEW_RESULT);
	}

	protected Long getOldResult() {
		return getContextObject(ContextKeys.NEW_RESULT);
	}

	protected Long[] getNewFactors() {
		return getContextObject(ContextKeys.NEW_FACTORS);
	}

	protected Long[] getOldFactors() {
		return getContextObject(ContextKeys.OLD_FACTORS);
	}

	protected <T> T getContextObject(String key) {
		return context.get().containsKey(key) ? (T) context.get().get(key)
				: null;
	}

	protected void setContextObject(String key, Object object) {
		if (context.get() == null) {
			Map<String, Object> contextMap = Maps.newHashMap();
			context.set(contextMap);
		}
		context.get().put(key, object);
	}

	protected List<Long> filt(int index, int excludeIndex, Long[] factors,
			Long[][] ranges, Long result, List<Long> solutions,
			List<Long> newSolutions) {

		if (index == excludeIndex) {
			return newSolutions;
		}
		List<GreatestCommonDivisor> seqGcds = getContextObject(ContextKeys.SEQUENCE_GCD);
		GreatestCommonDivisor gcd = seqGcds.get(index);
		Long gcdValue = gcd.getGcd();
		boolean isAllEvenNumber = true;
		boolean factorEven = (factors[index] % 2 == 0);
		Long remainResult = new Long(result);
		boolean resultEven = remainResult % 2 == 0;
		if (excludeIndex != -1) {
			if (index == factors.length - 1) {
				gcdValue = factors[excludeIndex];
			} else if (index > excludeIndex) {
				gcdValue = GreatestCommonDivisor.getGCD(
						new Long[] { gcdValue, factors[excludeIndex] })
						.getGcd();
			}
			if (factors[excludeIndex] % 2 != 0) {
				isAllEvenNumber = false;
			}
		}
		/*
		 * for(int i = 0;i < solutions.size();i++) { remainResult = -
		 * solutions.get(i) * factors[i]; }
		 */
		Long factor = factors[index];
		List<Long> finedSolutions = Lists.newArrayList();
		Long minResult = 0l;
		Long maxResult = 0l;

		for (int i = index + 1; i < factors.length; i++) {

			if (factors[i] % 2 != 0) {
				isAllEvenNumber = false;
			}
			if (i == excludeIndex)
				continue;
			minResult += ranges[i][0] * factors[i];
			maxResult += ranges[i][1] * factors[i];
		}
		if (excludeIndex != -1) {
			minResult += ranges[excludeIndex][0] * factors[excludeIndex];
			maxResult += ranges[excludeIndex][1] * factors[excludeIndex];
		}
		if (isAllEvenNumber && !resultEven && factorEven)
			return Lists.newArrayList();
		for (Long testSolution : newSolutions) {
			Long tmpResult = remainResult - factor * testSolution;
			if (tmpResult <= maxResult && tmpResult >= minResult
					&& tmpResult % gcdValue == 0
					&& (!isAllEvenNumber || (tmpResult % 2 == 0))
					&& isValid(index, factors ,result,solutions, testSolution, ranges)) {
				finedSolutions.add(testSolution);
			}
		}
		return finedSolutions;
	}

	protected boolean isValid(int index, Long[] factors,Long result,List<Long> solutions, long solution,
			Long[][] ranges) {
		int[] neighborhoods = getContextObject(RUNNING_PARAMETERS.NEIGHBORHOOD);
		Map<String, Object> validatorParams = Maps.newHashMap();
		if (neighborhoods != null) {
			validatorParams.put(PARAMETER_KEYS.NEIGHBORHOOD_GROUP_DATA,
					neighborhoods);
		}
		List<CustomerizedValidator> validators = CustomerizedValidatorFactory
				.getValidators((int[]) getContextObject(RUNNING_PARAMETERS.CUSTORMERIZED_VALIDATOR));
		if (validators != null && validators.size() > 0) {
			for (CustomerizedValidator validator : validators) {
				if (!validator.isValid(new ValidateParameter(index, factors,result,solutions, ranges, solution,
						validatorParams,getSpecialIndex()))) {
					return false;
				}
			}
		}
		return true;
	}

	protected List<Long> pickUpFinalResult(List<List<Long>> candidateSolutions,
			Long[] factors, Long[][] ranges, Long result) {
		setContextObject(ContextKeys.CANDIDATE_SOLUTIONS, candidateSolutions);
		return pickUpFinalResult(factors, ranges, result);
	}

	protected List<Long> getCandiadateSolution(int round, Long result) {
		List<List<Long>> candidateSolutions = getContextObject(ContextKeys.CANDIDATE_SOLUTIONS);
		if (candidateSolutions.size() > round) {
			return candidateSolutions.get(round);
		}
		return Lists.newArrayList();
	}

	protected boolean isFinalRound(int round) {
		Long[] factors = getContextObject(ContextKeys.NEW_FACTORS);
		if (round == factors.length - 2)
			return true;
		return false;
	}

	protected List<Long> findFinalSolutions(Stack<SearchStep> searchPath,
			Long result, Long[] factors, Long[][] ranges, int round) {

		List<Long> testSolutions = toSolutions(searchPath);
		Long tmpResult = recalculateResult(result, testSolutions, factors);
		List<List<Long>> possibleSolution = MathUtils.findDEPossibleSolution(
				new Long[] { factors[round], factors[round + 1] }, tmpResult,
				new Long[][] { ranges[round], ranges[round + 1] });
		if (possibleSolution.size() > 0) {
			testSolutions.add(possibleSolution.get(0).get(0));
			testSolutions.add(possibleSolution.get(0).get(1));
			return testSolutions;
		}
		return null;
	}

	protected Integer getSpecialIndex() {
		return -1;
	}

	protected List<Long> getSpecialIndexNewSolutions(Long[] range) {
		List<Long> newSolutions = Lists.newArrayList();
		long beginCur = range[0];
		while (beginCur <= range[1]) {
			newSolutions.add(beginCur);
			beginCur++;
		}
		return newSolutions;
	}

	protected Integer getMaxRound(Long[] factors) {
		return factors.length;
	}

	private Stack<SearchStep> changePath(Stack<SearchStep> searchPath) {

		SearchStep lastFinalStep = null;
		int round = searchPath.size() - 1;
		do {
			lastFinalStep = searchPath.peek();
			if (round == getSpecialIndex()) {
				searchPath.pop();
				round = searchPath.size() - 1;
			} else if (lastFinalStep.getLastSelector() < lastFinalStep
					.getSolutions().size() - 1) {
				lastFinalStep
						.setLastSelector(lastFinalStep.getLastSelector() + 1);
				break;
			} else {
				lastFinalStep = searchPath.pop();
				round = searchPath.size() - 1;
			}
		} while (round >= 0);
		return searchPath;
	}
	
	protected boolean isTimeOut(Long beginTime){
		Long timeout = getContextObject(RUNNING_PARAMETERS.DEFAULT_TIME_OUT);
		if(timeout != null){
			Long curTime = System.currentTimeMillis();
			if(curTime - beginTime > timeout){
				return true;
			}
		}
		return false;
	}

	protected List<Long> pickUpFinalResult(Long[] factors, Long[][] ranges,
			Long result) {

		int round = 0;
		// Long tmpResult = new Long(result);
		Long beginTime = System.currentTimeMillis();
		Integer maxRound = getMaxRound(factors);
		Stack<SearchStep> searchPath = getLastFinishedSteps();
		if (searchPath != null) {
			searchPath = changePath(searchPath);
			round = searchPath.size();
			if (searchPath.size() == 0)
				return null;
		} else {
			searchPath = new Stack<>();
		}
		while (round < maxRound && round >= 0) {

			if(isTimeOut(beginTime)){
				return null;
			}
			SearchStep lastStep = searchPath.isEmpty() ? null : searchPath
					.peek();
			List<Long> testSolutions = toSolutions(searchPath);
			if (isFinalRound(round)) {
				List<Long> finalSolution = findFinalSolutions(searchPath,
						result, factors, ranges, round);
				if (finalSolution != null) {
					markLastStep(searchPath);
					return finalSolution;
				}
				markFail(testSolutions);
				round--;
				continue;
			}
			if (round == getSpecialIndex()) {
				if (!(lastStep != null && lastStep.getRound().equals(
						getSpecialIndex()))) {
					searchPath.push(new SearchStep(round, 0,
							getSpecialIndexNewSolutions(ranges[round])));
					round++;
				} else {
					searchPath.pop();
					round--;
				}
				continue;
			} else {

				if (lastStep != null && lastStep.getRound() == round) {
					List<Long> newSolutions = lastStep.getSolutions();
					if (lastStep.getLastSelector() == newSolutions.size() - 1) {
						searchPath.pop();
						round--;
					} else {
						lastStep.setLastSelector(lastStep.getLastSelector() + 1);
						round++;
					}
				} else {
					Integer selectIndex = 0;
					Long tempResult = recalculateResult(result, testSolutions,
							factors);
					List<Long> newSolutions = getCandiadateSolution(round,
							tempResult);
					newSolutions = filt(round, getSpecialIndex(), factors,
							ranges, tempResult, testSolutions, newSolutions);

					if (newSolutions.size() == 0) {
						if (searchPath.isEmpty()) {
							return null;
						} else {
							round--;
						}
					} else {
						setContextObject(ContextKeys.TEMP_RESULT, tempResult);
						searchPath.push(new SearchStep(round, selectIndex,
								newSolutions));
						round++;
					}
				}
			}
		}
		return null;
	}

	protected class SearchStep {
		private Integer round;
		private Integer lastSelector;
		private List<Long> solutions;

		public Integer getRound() {
			return round;
		}

		public void setRound(Integer round) {
			this.round = round;
		}

		public Integer getLastSelector() {
			return lastSelector;
		}

		public void setLastSelector(Integer lastSelector) {
			this.lastSelector = lastSelector;
		}

		public List<Long> getSolutions() {
			return solutions;
		}

		public void setSolutions(List<Long> solutions) {
			this.solutions = solutions;
		}

		public SearchStep(Integer round, Integer lastSelector,
				List<Long> solutions) {
			super();
			this.round = round;
			this.lastSelector = lastSelector;
			this.solutions = solutions;
		}

		public SearchStep clone() {
			List<Long> newSolution = Lists.newArrayList();
			newSolution.addAll(solutions);
			SearchStep newStep = new SearchStep(new Integer(round),
					new Integer(lastSelector), newSolution);
			return newStep;
		}

	}

	protected List<Long> toSolutions(Stack<SearchStep> searchStep) {
		List<Long> solutions = Lists.newArrayList();
		for (int i = 0; i < searchStep.size(); i++) {
			SearchStep step = searchStep.get(i);
			solutions.add(step.getSolutions().get(step.getLastSelector()));
		}
		return solutions;
	}

	protected Long recalculateResult(Long result, List<Long> solutions,
			Long[] factors) {
		Long tempResult = new Long(result);
		for (int i = 0; i < solutions.size(); i++) {
			tempResult -= solutions.get(i) * factors[i];
		}
		return tempResult;
	}

	private void markFail(List<Long> solutions) {
		List<Integer> failList = getContextObject(ContextKeys.FAIL_LIST);
		Integer hashCode = calculateHashCode(solutions);
		if (hashCode != 0)
			failList.add(hashCode);
		setContextObject(ContextKeys.FAIL_LIST, failList);
	}

	private boolean isFailBefore(List<Long> solutions) {
		List<Integer> failList = getContextObject(ContextKeys.FAIL_LIST);
		if (failList.contains(calculateHashCode(solutions)))
			return true;
		return false;
	}

	private Integer calculateHashCode(List<Long> solutions) {

		Integer hashCode = 0;
		if (solutions.size() > 0) {
			Long[] longArr = solutions.toArray(new Long[solutions.size()]);
			hashCode = Arrays.hashCode(longArr);
		}
		return hashCode;
	}

	protected void initCongruenceSolution(Long[] factors, Long[][] ranges,
			Long modular, Long result) {
		List<SolutionInformation> solutionInfos = Lists.newArrayList();
		for (int i = 0; i < factors.length; i++) {
			solutionInfos.add(new SolutionInformation(i, factors, modular,
					ranges[i]));
		}
		setContextObject(ContextKeys.SOLUTION_INFORMATION, solutionInfos);
	}

	protected class SolutionInformation {

		private Long factor;
		private Integer index;
		private List<Long> possibleRemainders;
		private Map<Long, List<Long>> solutions;

		public SolutionInformation(Integer index, Long[] factors, Long modular,
				Long[] range) {
			this.factor = factors[index];
			this.index = index;
			init(modular, range);
		}

		private void init(Long modular, Long[] range) {
			possibleRemainders = Lists.newArrayList();
			solutions = Maps.newHashMap();
			solutions = new HashMap<>();
			boolean direction = true;
			Long remainder = direction ? 0 : modular - 1l;
			Long beginSolution = range[0];
			while (beginSolution <= range[1]) {
				remainder = MathUtils.getLeastRemainder(beginSolution * factor,
						modular);
				if (!possibleRemainders.contains(remainder)) {
					possibleRemainders.add(remainder);
				}
				if (solutions.containsKey(remainder)) {
					List<Long> solution = solutions.get(remainder);
					solution.add(beginSolution);
				} else {
					List<Long> solution = Lists.newArrayList();
					solution.add(beginSolution);
					solutions.put(remainder, solution);
				}
				beginSolution++;
			}
			Collections.sort(possibleRemainders, new Comparator<Long>() {

				@Override
				public int compare(Long o1, Long o2) {
					// TODO Auto-generated method stub
					return o2 - o1 > 0 ? 1 : (o2 == o1 ? 0 : -1);
				}
			});
			validate(range);
		}

		public void validate(Long[] range) {
			Long totalCount = range[1] - range[0] + 1;
			Long solutionCount = 0l;
			for (List<Long> solution : solutions.values()) {
				solutionCount += solution.size();
			}
			if (!totalCount.equals(solutionCount)) {

				System.err.println("init fail!");
				System.exit(-1);
			}
		}

		public List<Long> getPossibleRemainders() {
			return possibleRemainders;
		}

		public Map<Long, List<Long>> getSolutions() {
			return solutions;
		}

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

	}

	protected Stack<SearchStep> getLastFinishedSteps() {
		if (context.get().containsKey(ContextKeys.LAST_FINISHED_STEPS)) {
			return (Stack<SearchStep>) context.get().get(
					ContextKeys.LAST_FINISHED_STEPS);
		}
		return null;
	}

	protected void markLastStep(Stack<SearchStep> steps) {
		setContextObject(ContextKeys.LAST_FINISHED_STEPS, steps);
	}

	@Override
	public void setContext(String key, Object object) {
		setContextObject(key, object);
	}

}
