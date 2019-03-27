package org.aztec.autumn.common.math.equations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentResult;
import org.aztec.autumn.common.math.equations.strategy.de.range.ComplexStrategy;
import org.aztec.autumn.common.math.equations.strategy.de.range.SimpleRangeAdjustmentStrategy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 不定方程解调整器，非线程安全
 * @author 10064513
 *
 */
public class DESolutionAdjustment {
	
	private DiophantineResult equationResult;
	private AdjustmentStrategy simpleStrategy = new SimpleRangeAdjustmentStrategy();
	private AdjustmentStrategy complexStrategy = new ComplexStrategy();
	private static ThreadLocal<Map<Integer,AdjustmentResult>> histories = new ThreadLocal<>(); 

	public DESolutionAdjustment(DiophantineResult result) {
		// TODO Auto-generated constructor stub
		this.equationResult = result;
		histories.set(Maps.newConcurrentMap());
		//histories.set(Maps.newHashMap());
	}

	public void findConstraintSolutionInRange(
			Map<DESolutionConstraintType, DESolutionConstraint> constraints) {
		if(equationResult.getCoefficients().size() == 2) {
			generateSolution(constraints);
		}
		else {
			generateComplexDESolution(constraints);
		}
	}
	
	private void generateSolution(
			Map<DESolutionConstraintType, DESolutionConstraint> constraints) {
		Long[][] ranges = (Long[][]) constraints.get(DESolutionConstraintType.SOLUTION_RANGE).getParams()[0];
		Integer solutionMaxNum = constraints.containsKey(DESolutionConstraintType.SOLUTION_MAX_NUM) ? 
				(Integer)constraints.get(DESolutionConstraintType.SOLUTION_MAX_NUM).getParams()[0] : 
					null;
		equationResult.getSolutions().addAll(findSolutionBySimpliestWay(ranges, solutionMaxNum));
	}
	
	private void generateComplexDESolution(
			Map<DESolutionConstraintType, DESolutionConstraint> constraints) {

		boolean isOrder= constraints.containsKey(DESolutionConstraintType.ORDERED),
				noDuplicate = constraints.containsKey(DESolutionConstraint.DESolutionConstraintType.NO_DUPLICATE);
		
		List<Long> coefficients = equationResult.getCoefficients();
		Long[] factors = coefficients.toArray(coefficients.toArray(new Long[coefficients.size()]));
		List<List<GreatestCommonDivisor>> allGcds = GreatestCommonDivisor.getAllGCD(factors);
		findBasedConstraintSolution(constraints, isOrder, noDuplicate,allGcds);
		List<List<Long>> solutions = equationResult.getSolutions();
		if(solutions.size() > 0) {
			//Long 
			
			DESolutionConstraint solMaxNumConstrt = constraints.get(DESolutionConstraintType.SOLUTION_MAX_NUM);
			Integer solMaxNum = null;
			if(solMaxNumConstrt != null) {
				solMaxNum = (Integer) solMaxNumConstrt.getParams()[0];
			}
			Integer solutionIndex = 0;

			do {
				if(solMaxNum != null) {
					if(solutions.size() >= solMaxNum) {
						break;
					}
				}
				Long[] tmpSolutions = solutions.get(solutionIndex).toArray(new Long[equationResult.getBaseResults().size()]);
				AdjustmentResult adjustment = complexStrategy.makeAdjustment(constraints, tmpSolutions, equationResult.getCoefficients(), histories.get(), new Object[] {allGcds});
				if(adjustment != null) {
					Long[] findSulution = adjustment.getSolution();
					if(findSulution != null) {
						solutions.add(Lists.newArrayList(findSulution));
					}
				}
				solutionIndex ++;
			}while(solutionIndex < solutions.size());
			
		}
	}


	private List<List<Long>> findSolutionBySimpliestWay(Long[][] ranges,Integer solutionMaxNum) {
		int n = 0;
		List<List<Long>> solutions = Lists.newArrayList();
		
		Long[] baseResults = equationResult.getBaseResults().toArray(new Long[2]);
		long coefficient1 = equationResult.getCoefficients().get(1) / equationResult.getGcd();
		long coefficient2 = equationResult.getCoefficients().get(0) / equationResult.getGcd();
		Long[] stepRanges = new Long[] {Math.max(((ranges[0][0] - baseResults[0]) / coefficient1), ((baseResults[1] - ranges[1][1]) / coefficient2)),
				Math.min(((baseResults[1] - ranges[1][0]) / coefficient2) , ((ranges[0][1] - baseResults[0]) / coefficient1))};
		int endStep = stepRanges[1].intValue();
		if(solutionMaxNum != null && ((stepRanges[0].intValue() + solutionMaxNum) < (stepRanges[0].intValue() + stepRanges[1].intValue()))) {
			endStep = stepRanges[0].intValue() + solutionMaxNum;
		}
		for(int i = stepRanges[0].intValue();i <=  endStep;i++) {
			List<Long> solution = Lists.newArrayList();
			solution.add(baseResults[0] + i * coefficient1);
			solution.add(baseResults[1] - i * coefficient2);
			solutions.add(solution);
		}
		return solutions;
	}
	
	
	public void filtSolution(Map<DESolutionConstraintType, DESolutionConstraint> constraints) {
		List<List<Long>> filtSolution = Lists.newArrayList();
		boolean isOrder= constraints.containsKey(DESolutionConstraintType.ORDERED),
				noDuplicate = constraints.containsKey(DESolutionConstraint.DESolutionConstraintType.NO_DUPLICATE);

		List<List<Long>> solutions = equationResult.getSolutions();
		Long[][] ranges = (Long[][]) constraints.get(DESolutionConstraintType.SOLUTION_RANGE).getParams()[0];
		for(List<Long> solution : solutions) {
			if(equationResult.isSatified(solution, ranges,noDuplicate, isOrder)) {
				filtSolution.add(solution);
			}
		}
		solutions = filtSolution;
	}
	
	
	
	private void findBasedConstraintSolution(Map<DESolutionConstraintType, DESolutionConstraint> constraints,
			boolean isOrder,boolean noDuplicate,List<List<GreatestCommonDivisor>> allGcds) {

		Long[] baseSolution = new Long[equationResult.getBaseResults().size()];
		equationResult.getBaseResults().toArray(baseSolution);
		long curTime = System.currentTimeMillis();
		AdjustmentResult adjustment = simpleStrategy.makeAdjustment(constraints, baseSolution, equationResult.getCoefficients(), histories.get(), new Object[] {allGcds});
		System.out.println("First adjust use:" + (System.currentTimeMillis() - curTime));
		if(adjustment.getSolution() != null) {
			equationResult.getSolutions().add(Lists.newArrayList(adjustment.getSolution()));
		}
		/*Long[] solution = findSolution(baseSolution, ranges, allGcds, constraints);
		if(solution != null) {
			equationResult.getSolutions().add(Lists.newArrayList(solution));
		}*/
	}
	
	
	
	
	
	public boolean hasAdditionalSolutions(Long[][] ranges) {

		List<Long> coefficients = equationResult.getCoefficients();
		List<List<Long>> solutions = equationResult.getSolutions();
		if(ranges.length < equationResult.getBaseResults().size()) {
			return true;
		}
		Long maxResult = 0l;
		Long minResult = 0l;
		for(int i = 0 ;i < ranges.length;i++) {
			maxResult += ranges[i][1] * coefficients.get(i);
			minResult += ranges[i][0] * coefficients.get(i);
		}
		if(equationResult.getResult() == maxResult) {
			Long[] matchSolution = new Long[coefficients.size()];
			for(int i = 0;i < ranges.length;i++) {
				matchSolution[i] = ranges[i][1];
			}
			solutions.add(Lists.newArrayList(matchSolution));
		}
		if(equationResult.getResult() == minResult) {
			Long[] matchSolution = new Long[coefficients.size()];
			for(int i = 0;i < ranges.length;i++) {
				matchSolution[i] = ranges[i][0];
			}
			solutions.add(Lists.newArrayList(matchSolution));
		}
		return equationResult.getResult() <= maxResult && equationResult.getResult() >= minResult;
	}
	
	
	public static void main(String[] args) {
		/*Long[] testArr = new Long[] {1l,2l,3l};
		Long[] testArr2 = new Long[] {1l,3l,2l};
		System.out.println(Arrays.hashCode(testArr));
		System.out.println(Arrays.hashCode(testArr2));

		Long[] testArr3 = new Long[] {2l,1l,3l};
		System.out.println(Arrays.hashCode(testArr3));*/

		Long[] testArrZ = new Long[] {-1l,1l};

		System.out.println(Arrays.hashCode(testArrZ));
		
	}
}
