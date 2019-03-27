package org.aztec.autumn.common.math.equations;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.DiophantineResult.SpecialDiophantineEquationClass;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DiophantineEquation {
	
	private static DiophantineResult doFastPolyDE(List<GreatestCommonDivisor> gcds,Long[] factors,
			Long result) {
		DiophantineResult baseResult;
		Object[] solvableObjs = findSolvableFactors(factors, factors.length % 2 == 0 ? 2 : 3, result);
		if(solvableObjs != null) {
			baseResult = doPolynomialDE((List<GreatestCommonDivisor>)solvableObjs[2],(Long[])solvableObjs[1], result);

			Long[] baseSolution = new Long[factors.length];
			Integer[] indexs = (Integer[])solvableObjs[0];
			for(int i = 0;i < factors.length;i++) {
				for(int j = 0;j < indexs.length;j++) {
					if(i == indexs[j]) {
						baseSolution[i] = baseResult.getBaseResults().get(j);
					}
				}
				if(baseSolution[i] == null) {
					baseSolution[i] = 0l;
				}
			}
			DiophantineResult mergedResult = new DiophantineResult(factors,baseSolution,result,gcds.get(0).getGcd());
			return mergedResult;
		}
		else {
			return doPolynomialDE(gcds, factors, result);
		}
	}
	
	private static Object[] findSolvableFactors(Long[] allFactors,int choiceSize,Long result) {
		Object[] returnObj = null;
		for(int i = 0;i < allFactors.length;i++) {
			for(int j = i+1; j < allFactors.length;j++) {
				if(choiceSize == 2) {
					Long[] testFactors = new Long[] {allFactors[i],allFactors[j]};
					List<GreatestCommonDivisor> gcds = GreatestCommonDivisor.getSequenceGCD(testFactors);
					if(DiophantineResult.hasSolution(result, gcds.get(0))) {
						return new Object[] {new Integer[]{i,j},testFactors,gcds};
					}
				}
				else {
					for(int k = j + 1;k < allFactors.length;k++) {
						Long[] testFactors = new Long[] {allFactors[i],allFactors[j],allFactors[k]};
						List<GreatestCommonDivisor> gcds = GreatestCommonDivisor.getSequenceGCD(testFactors);
						if(DiophantineResult.hasSolution(result, gcds.get(0))) {
							return new Object[] {new Integer[]{i,j,k},testFactors,gcds};
						}
					}
				}
			}
		}
		return returnObj;
	}

	
	private static DiophantineResult doPolynomialDE(List<GreatestCommonDivisor> gcds,Long[] factors,
			Long result){
		Long tempResult = result;
		DiophantineResult solution = null;
		for(int i = 0;i < factors.length - 1;i++) {
			GreatestCommonDivisor gcd = gcds.get(i + 1);
			Long[] calculateFactors = new Long[] {factors[i],gcd.getGcd()};
			DiophantineResult dr = getBaseDEResult(calculateFactors, tempResult);
			if(solution == null) {
				solution = new DiophantineResult(dr.getBaseResults().get(0), dr.getCoefficients().get(0),
						result,gcds.get(0).getGcd(),true);
			}
			else {
				solution.getBaseResults().add(dr.getBaseResults().get(0));
				solution.getCoefficients().add(factors[i]);
			}
			if (i == factors.length - 2) {
				solution.getBaseResults().add(dr.getBaseResults().get(1));
				solution.getCoefficients().add(dr.getCoefficients().get(1));
			}
			tempResult = tempResult - dr.getBaseResults().get(0) * factors[i];
		}
		return solution;
	}
	

	public static DiophantineResult getSolution(Long[] factors, Long result,Long[][] ranges,int solutionCount){
		Map<DESolutionConstraintType, DESolutionConstraint> constraints = Maps.newHashMap();

		constraints.put(DESolutionConstraintType.SOLUTION_RANGE,
				new DESolutionConstraint(DESolutionConstraintType.SOLUTION_RANGE, new Object[] { ranges }));
		constraints.put(DESolutionConstraintType.SOLUTION_SIZE,
				new DESolutionConstraint(DESolutionConstraintType.SOLUTION_SIZE, new Object[] { solutionCount }));
		try {
			DiophantineResult equationResult = DiophantineEquation.getSolution(factors, result);
			if(equationResult == null || !equationResult.getHasSolution()){
				return null;
			}
			equationResult.constrain(constraints);
			equationResult.validate();
			return equationResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static DiophantineResult getSolution(Long[] factors, Long result,Long[][] ranges){
		Map<DESolutionConstraintType, DESolutionConstraint> constraints = Maps.newHashMap();

		constraints.put(DESolutionConstraintType.SOLUTION_RANGE,
				new DESolutionConstraint(DESolutionConstraintType.SOLUTION_RANGE, new Object[] { ranges }));
		try {
			DiophantineResult equationResult = DiophantineEquation.getSolution(factors, result);
			if(equationResult == null){
				return null;
			}
			equationResult.constrain(constraints);
			equationResult.validate();
			return equationResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static DiophantineResult getSolution(Long[] factors, Long result) {
		if(factors.length == 0) {
			throw new IllegalArgumentException("factors should be provided!");
		}
		if(factors.length == 1) {
			if(result % factors[0] == 0) {
				DiophantineResult equationResult = new DiophantineResult(true, factors,result);
				equationResult.setBaseResults(Lists.newArrayList(new Long[] {result / factors[0]}));
				return equationResult;
			}
			else {
				return null;
			}
			
		}
		else if(factors.length > 2) {

			List<GreatestCommonDivisor> gcds = GreatestCommonDivisor.getSequenceGCD(factors);
			GreatestCommonDivisor gcd = gcds.get(0);
			if(DiophantineResult.hasSolution(result, gcd)) {
				return doFastPolyDE(gcds, factors, result);
				//return doPolynomialDE(gcds, factors, result, constraints);
			}
		}
		else if(factors.length == 2) {
			return getBaseDEResult(factors, result);
		}
		return null;
	}
	
	private static DiophantineResult getBaseDEResult(Long[] factors,Long result){

		GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(factors);
		DiophantineResult dr = null;
		if(!DiophantineResult.hasSolution(result,gcd)) {
			dr = new DiophantineResult(false, factors,result);
			return dr;
		}
		SpecialDiophantineEquationClass deCls = DiophantineResult.getSpecialClassDE(factors, result, gcd);
		if(!deCls.equals(SpecialDiophantineEquationClass.SIMPLE)) {
			dr = new DiophantineResult(deCls, factors, result, gcd);
		}
		else {
			ExtendedEuclideanEquation mergedEquation = ExtendedEuclideanEquation.findEquations(factors, gcd,result);
			dr = new DiophantineResult(mergedEquation,factors, result);
		}
		return dr;
	}
	
	
	public static List<DiophantineResult> getModularDiophatineEquationSolution(Long[] factors, Long result, 
			Long modular,Long maxModularMultiple) {
		List<DiophantineResult> resultList = Lists.newArrayList();
		for(int i = 0;i < maxModularMultiple;i++) {
			Long realResult =  modular * maxModularMultiple + result;
			DiophantineResult desResult = getSolution(factors, realResult);
			if(desResult.getHasSolution()) {
				resultList.add(desResult);
			}
		}
		return resultList;
	}
	
	
}
