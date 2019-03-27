package org.aztec.autumn.common.math.equations;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.ConstraintedDiophantineEquation.RunningMode;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;

import com.google.common.collect.Lists;

public class DiophantineResult {
	private List<Long> baseResults;
	private List<Long> coefficients;
	private List<List<Long>> solutions = Lists.newArrayList();
	private Boolean hasSolution = false;
	private Boolean infinite = true;
	private Long result;
	private Long gcd;

	public List<Long> getBaseResults() {
		return baseResults;
	}

	public Boolean getInfinite() {
		return infinite;
	}


	public void setInfinite(Boolean infinite) {
		this.infinite = infinite;
	}


	public void setBaseResults(List<Long> baseResults) {
		this.baseResults = baseResults;
	}

	public List<Long> getCoefficients() {
		return coefficients;
	}

	public void setCoefficients(List<Long> coefficients) {
		this.coefficients = coefficients;
	}

	public Boolean getHasSolution() {
		return hasSolution;
	}

	public void setHasSolution(Boolean hasSolution) {
		this.hasSolution = hasSolution;
	}

	public Long getResult() {
		return result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public List<List<Long>> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<List<Long>> solutions) {
		this.solutions = solutions;
	}

	public Long getGcd() {
		return gcd;
	}

	public void setGcd(Long gcd) {
		this.gcd = gcd;
	}

	public DiophantineResult(Long baseResult, Long coefficient, Long result, Long gcd, Boolean hasSolution) {
		super();
		this.baseResults = Lists.newArrayList();
		this.baseResults.add(baseResult);
		this.coefficients = Lists.newArrayList();
		this.coefficients.add(coefficient);
		this.result = result;
		this.gcd = gcd;
		this.hasSolution = hasSolution;
	}

	public DiophantineResult(Boolean hasSolution, Long[] factors,Long result) {
		super();
		this.hasSolution = hasSolution;
		baseResults = Lists.newArrayList();
		coefficients = Lists.newArrayList(factors);
		this.result = result;
		gcd = GreatestCommonDivisor.getGCD(factors).getGcd();
	}
	
	public DiophantineResult(Long[] factors,Long[] baseSolution,Long result,Long gcd) {
		super();
		this.hasSolution = baseSolution != null && baseSolution.length > 0 ? true : false;
		baseResults = Lists.newArrayList();
		coefficients = Lists.newArrayList();
		for (Long factor : factors) {
			coefficients.add(factor);
		}
		this.result = result;
		for(int i = 0;i < baseSolution.length;i++) {
			baseResults.add(baseSolution[i]);
		}
		this.gcd = gcd;
	}

	public DiophantineResult(ExtendedEuclideanEquation mergedEquation, Long[] factors, Long result) {
		this.gcd = mergedEquation.getCommonDivisor();
		Long multiple = result / mergedEquation.getCommonDivisor();
		this.baseResults = Lists.newArrayList();
		this.coefficients = Lists.newArrayList();
		for (int i = 0; i < factors.length; i++) {
			Long factor = factors[i];
			baseResults.add(mergedEquation.getDivisorMap().get(factor) * multiple);
			coefficients.add(factor);
		}
		hasSolution = true;
		this.result = result;
	}

	public DiophantineResult(SpecialDiophantineEquationClass specCls, Long[] factors, Long result,
			GreatestCommonDivisor gcd) {
		if (specCls == null) {
			this.hasSolution = false;
		} else {
			baseResults = Lists.newArrayList();
			coefficients = Lists.newArrayList();
			this.gcd = gcd.getGcd();
			coefficients = Lists.newArrayList();
			for (int i = 0; i < factors.length; i++) {
				coefficients.add(factors[i]);
			}
			this.result = result;
			this.hasSolution = true;

			long t1 = factors[0] / this.gcd;
			long t2 = factors[1] / this.gcd;
			long k = factors[0] / factors[1];
			switch (specCls) {
			case GCD:
				if (k == 0) {
					k = factors[1] / factors[0];
					baseResults.add((k + 1) * result / factors[0]);
					baseResults.add(- result / factors[0]);
				} else {
					baseResults.add(- result / factors[1]);
					baseResults.add((k + 1) * result / factors[1]);
				}
				break;
			case K:

				t1 = Math.max(factors[0],factors[1]) / gcd.getGcd();
				t2 = Math.min(factors[0],factors[1]) / gcd.getGcd();
				k = (t1 + 1) / t2;
				if(factors[0] > factors[1]) {
					baseResults.add(-result / this.gcd);
					baseResults.add(k * result / this.gcd);
				}
				else {
					baseResults.add(k * result / this.gcd);
					baseResults.add(-result / this.gcd);
				}
				break;
			case ZERO:
				baseResults.add(-t2);
				baseResults.add(t1);
				break;
			case SIMPLE:
				break;
			}
			this.hasSolution = true;
		}

	}
	
	public void constrain(Map<DESolutionConstraintType, DESolutionConstraint> constraints) {
		infinite = false;
		if (constraints != null) {

			setSolutions(constraints);
		}
	}

	private void setSolutions(Map<DESolutionConstraintType, DESolutionConstraint> constraints) {

		if(solutions == null) {
			solutions = Lists.newArrayList();
		}
		
		if(constraints.containsKey(DESolutionConstraintType.SOLUTION_RANGE)) {
			DESolutionAdjustment adjustment = new DESolutionAdjustment(this);
			Long[][] ranges = (Long[][]) constraints.get(DESolutionConstraintType.SOLUTION_RANGE).getParams()[0];
			if (coefficients.size() == 1) {
				Long testSolution = result / coefficients.get(0);
				if(testSolution <= ranges[0][1] && testSolution >= ranges[0][0]) {
					Long[] solution = new Long[] {testSolution};
					solutions.add(Lists.newArrayList(solution));
				}
				return ;
			}
			if(adjustment.hasAdditionalSolutions(ranges)) {
				DESolutionConstraint sizeConstraint = constraints.get(DESolutionConstraintType.SOLUTION_SIZE);
				int solutionSize = 1;
				
				Long[] factors = coefficients.toArray(new Long[coefficients.size()]);
				if(sizeConstraint != null){
					solutionSize = (int) sizeConstraint.getParams()[0];
				}
				List<List<Long>> solution = ConstraintedDiophantineEquation.findSolution(factors, result, ranges, RunningMode.CONGRUENCE,solutionSize);
				this.solutions.addAll(solution);
				/*adjustment.findConstraintSolutionInRange(constraints);
				adjustment.filtSolution(constraints);*/
			}
			else {
				if(hasSolution) {
					infinite = true;
				}
				return ;
			}
		} else if (constraints.containsKey(DESolutionConstraintType.FIXED_SOLUTION)) {
			DEFixedSolutionPicker picker = new DEFixedSolutionPicker(this);
			picker.findSolutionsAsSpecified(constraints);
		}
	}
	

	public boolean isDuplicate(List<Long> targetSolution) {
		
		for(List<Long> thisSolution : solutions) {
			for(int i = 0;i < thisSolution.size();i++) {
				if(!thisSolution.get(i).equals(targetSolution.get(i))) {
					break;
				}
				if(i == thisSolution.size() - 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isDuplicate(Long[] targetSolution) {
		
		for(List<Long> thisSolution : solutions) {
			for(int i = 0;i < thisSolution.size();i++) {
				if(!thisSolution.get(i).equals(targetSolution[i])) {
					break;
				}
				if(i == thisSolution.size() - 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	//private void 
	
	public boolean isSatified(Long[] results,boolean noDuplicate,boolean isOrder) {
		if (noDuplicate ||  isOrder)
			return true;
		return true;
	}
	
	public boolean isSatified(List<Long> results,Long[][] ranges,Boolean noDuplicate,Boolean isOrder) {
		
		List<Long> tmpResult = Lists.newArrayList();
		for (int i = 0;i < results.size();i++) {
			Long result = results.get(i);
			if (//鎸夊崌搴忔帓鍒�
					(isOrder != null && isOrder && i > 0 && result < results.get(i - 1)) 
					//涓嶈兘閲嶅
					|| (noDuplicate != null && noDuplicate && tmpResult.contains(result)) || 
					//瓒呭嚭鑼冨洿
					(i < ranges.length && (ranges[i][0] > result || ranges[i][1] < result))
			) {
				return false;
			}
			else {
				tmpResult.add(result);
			}
		}
		return true;
	}
	


	/**
	 * 鍙揩閫熸眰瑙ｆ柟绋嬬壒瑙ｇ殑绫� GCD绫昏〃绀虹郴鏁颁腑鍖呮嫭鏈�澶у叕绾︽暟鐨凞E鏂圭▼,K绫昏〃涓ょ郴缁熼櫎浜庡叕绾︽暟鐨勫晢涔嬮棿瀛樺湪K * t1 = t2 - K鐨勫叧绯�
	 * 
	 * @author 10064513
	 *
	 */
	public static enum SpecialDiophantineEquationClass {
		GCD, K, ZERO, SIMPLE;
	}

	public static SpecialDiophantineEquationClass getSpecialClassDE(Long[] factors, Long result,
			GreatestCommonDivisor gcd) {
		if (factors.length != 2)
			return null;
		if (result == 0)
			return SpecialDiophantineEquationClass.ZERO;
		if (factors[1] % factors[0] == 0 || factors[0] % factors[1] == 0) {
			return SpecialDiophantineEquationClass.GCD;
		}
		long t1 = Math.max(factors[0],factors[1]) / gcd.getGcd();
		long t2 = Math.min(factors[0],factors[1]) / gcd.getGcd();
		if ((t1 + 1) % t2 == 0) {
			return SpecialDiophantineEquationClass.K;
		}
		return SpecialDiophantineEquationClass.SIMPLE;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		if (hasSolution) {

			builder.append("\n BASE SOLUTION : [" + result + " = ");
			for (int i = 0; i < baseResults.size(); i++) {
				if (i != 0)
					builder.append(" + ");
				builder.append(baseResults.get(i) + " * " + coefficients.get(i));
			}
			builder.append(" ]\nSOLUTIONS :");
			for (List<Long> solution : solutions) {
				builder.append(" \n[" + result + " = ");
				for (int i = 0; i < solution.size(); i++) {
					if (i != 0)
						builder.append(" + ");
					builder.append(solution.get(i) + " * " + coefficients.get(i));
				}
				builder.append(" ]\n");
			}
		} else {
			builder.append("Result[" + result + "] has no solution with coefficients array " + coefficients);
		}
		return builder.toString();
	}

	public static boolean hasSolution(Long result, GreatestCommonDivisor gcd) {

		if (result % gcd.getGcd() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Long[] findSolvableFactors(final Long[] allFactors, Long result) {
		for (int i = 0; i < allFactors.length - 1; i++) {
			for (int j = i + 1; j < allFactors.length; j++) {
				GreatestCommonDivisor gcd = GreatestCommonDivisor.calculate(allFactors[i], allFactors[j]);
				if (!DiophantineResult.hasSolution(result, gcd)) {
					return new Long[] { allFactors[i], allFactors[j] };
				}
			}
		}
		return null;
	}


	public void validate() {
		Long tempResult = 0l;
		if(!getHasSolution()) {
			return ;
		}
		for (int i = 0; i < baseResults.size(); i++) {
			tempResult += baseResults.get(i) * coefficients.get(i);

		}
		if (!tempResult.equals(result)) {
			throw new ArithmeticException("The base solution[" + baseResults + "] doesn't match the equation!");
		}
		if(solutions != null && solutions.size() > 0) {
			for(List<Long> solution : solutions) {
				tempResult = 0l;
				for (int i = 0; i < solution.size(); i++) {
					tempResult += solution.get(i) * coefficients.get(i);

				}
				if (!tempResult.equals(result)) {

					throw new ArithmeticException("The solution" + solution + " doesn't match the equation ,while: "
							+ buildEquationString(solution,tempResult));
				}
			}
		}
	}
	
	private String buildEquationString(List<Long> solution,Long testResult) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0;i < coefficients.size();i++) {
			Long coefficient = coefficients.get(i);

			builder.append( coefficient + " * " + solution.get(i));
			if(i != coefficients.size() - 1) {
				builder.append(" + ");
			}
			else {
				builder.append(" = " + testResult + " ( != " + result + " )");
			}
		}
		return builder.toString();
	}
}
