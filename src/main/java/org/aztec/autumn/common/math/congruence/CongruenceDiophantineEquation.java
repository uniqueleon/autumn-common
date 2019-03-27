package org.aztec.autumn.common.math.congruence;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.MathConstant;
import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.utils.MathUtils;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

public class CongruenceDiophantineEquation {

	private Long modular;
	private Long[] resultRange = new Long[2];
	private Long remainder;
	private List<Long> coefficient;
	private List<List<Long>> solutions;
	private List<List<Long>> exclusiveSolutions;
	private Long[][] ranges;
	private Integer maxSolutionCount = MathConstant.DEFAULT_SOLUTION_MAX_COUNT;

	public CongruenceDiophantineEquation(Long modular, Long remainder, List<Long> coefficients, Long[][] ranges,
			List<List<Long>> exclusiveSolutions, Integer maxSolutionCount) {
		this.modular = modular;
		this.remainder = remainder;
		this.coefficient = Lists.newArrayList();
		for(int i = 0;i < coefficients.size();i++) {
			coefficient.add(coefficients.get(i) % modular);
		}
		this.ranges = ranges;
		this.maxSolutionCount = maxSolutionCount;
		this.exclusiveSolutions = exclusiveSolutions;
		calculatePossibleResult();
	}

	private void calculatePossibleResult() {
		Long[] possibleResults = MathUtils.findPossibleRange(ranges, coefficient);
		Long possibleMinResult = (possibleResults[0] / modular) * modular + remainder;
		Long possibleMaxResult = (possibleResults[1] / modular) * modular + remainder;
		resultRange = new Long[] { possibleMinResult, possibleMaxResult };
	}

	public List<List<Long>> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<List<Long>> solutions) {
		this.solutions = solutions;
	}

	public List<List<Long>> getExclusiveSolutions() {
		return exclusiveSolutions;
	}

	public void setExclusiveSolutions(List<List<Long>> exclusiveSolutions) {
		this.exclusiveSolutions = exclusiveSolutions;
	}

	public Long getModular() {
		return modular;
	}

	public void setModular(Long modular) {
		this.modular = modular;
	}

	public Long getRemainder() {
		return remainder;
	}

	public void setRemainder(Long remainder) {
		this.remainder = remainder;
	}

	public List<Long> getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(List<Long> coefficient) {
		this.coefficient = coefficient;
	}

	public void solve() {
		solutions = Lists.newArrayList();
		Long tmpResult = resultRange[0];
		Long[] factors = new Long[coefficient.size()];
		coefficient.toArray(factors);
		Map<DESolutionConstraintType, DESolutionConstraint> constraints = DESolutionConstraint
				.addRangeConstraint(Maps.newHashMap(), ranges);
		if (maxSolutionCount != null) {
			constraints.put(DESolutionConstraintType.SOLUTION_MAX_NUM,
					DESolutionConstraint.newSolutionLimitConstraint(maxSolutionCount));
		}
		if (exclusiveSolutions != null) {
			constraints.put(DESolutionConstraintType.EXCLUSIVE_SOLUTION,
					DESolutionConstraint.newExclusiveConstraint(exclusiveSolutions));
		}
		while (tmpResult <= resultRange[1]) {
			DiophantineResult result = DiophantineEquation.getSolution(factors, tmpResult);
			result.constrain(constraints);
			if(!isExclusive(result.getSolutions())){
				solutions.addAll(result.getSolutions());
				if (solutions.size() > maxSolutionCount) {
					break;
				}
			}
			tmpResult += modular;
		}
	}
	
	
	private boolean isExclusive(List<List<Long>> otherSolution) {
		long matchCount = 0;
		for(int i = 0;i < otherSolution.size();i++) {
			for(List<Long> excSolution : exclusiveSolutions) {
				if(excSolution.equals(otherSolution.get(i))) {
					matchCount ++;
				}
			}
		}
		if(matchCount == otherSolution.size())
			return true;
		return false;
	}

}
