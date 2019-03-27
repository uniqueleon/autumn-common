package org.aztec.autumn.common.math.equations;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class ExtendedEuclideanEquation{
	Map<Long,Long> divisorMap;
	private Long commonDivisor;
	
	public Map<Long, Long> getDivisorMap() {
		return divisorMap;
	}
	public void setDivisorMap(Map<Long, Long> divisorMap) {
		this.divisorMap = divisorMap;
	}
	public Long getCommonDivisor() {
		return commonDivisor;
	}
	public void setCommonDivisor(Long remainder) {
		this.commonDivisor = remainder;
	}
	public boolean isSolved(Long[] targets) {
		for(Long target : targets) {
			if(target.equals(1l))
				continue;
			if(!divisorMap.containsKey(target)) {
				return false;
			}
		}
		return true;
	}
	
	public ExtendedEuclideanEquation(EuclideanEquation eEqual) {
		divisorMap = Maps.newHashMap();
		if(eEqual.getRemainder() == 0) {

			commonDivisor = eEqual.getDivisor();
			if(eEqual.getDividend().equals(eEqual.getDivisor())) {
				divisorMap.put(eEqual.getDivisor(), 1l);
			}
			else {
				divisorMap.put(eEqual.getDividend(), 1l);
				divisorMap.put(eEqual.getDivisor(),  - (eEqual.getMultiple() - 1));
			}
		}
		else {
			commonDivisor = eEqual.getRemainder();
			
			divisorMap.put(eEqual.getDivisor(),  - eEqual.getMultiple());
			divisorMap.put(eEqual.getDividend(), 1l);
		}
		validate();
	}
	
	public boolean isDuplicated(EuclideanEquation equation) {
		
		if(divisorMap.size() == 3 && new Long(1).equals(divisorMap.get(equation.getRemainder()))
				&& new Long(1).equals(divisorMap.get(equation.getDividend())) && 
				divisorMap.get(equation.getMultiple()) == equation.getMultiple()) {
			return true;
		} else if(equation.getRemainder() == 0 && divisorMap.containsKey(equation.getDivisor())
				&& divisorMap.containsKey(equation.getDividend())) {
			return true;
		}
		return false;
	}
	
	public boolean add(EuclideanEquation equation) {
		if((!divisorMap.containsKey(equation.getRemainder())
				&& !divisorMap.containsKey(equation.getDivisor()))
				|| isDuplicated(equation))
			return false;

		Long multi = divisorMap.get(equation.getRemainder());
		if(equation.getRemainder() == 0) {
			multi = divisorMap.get(equation.getDivisor());
			Long addFactor1 = divisorMap.get(equation.getDividend());
			if(addFactor1 == null) {
				divisorMap.put(equation.getDividend(), multi);
			}
			else {
				divisorMap.put(equation.getDividend(), addFactor1 + multi);
			}
			divisorMap.remove(equation.getDivisor());
		}
		else {
			Long addFactor1 = divisorMap.get(equation.getDividend());
			if(addFactor1 == null) {
				divisorMap.put(equation.getDividend(), multi);
			}
			else {
				divisorMap.put(equation.getDividend(), addFactor1 + multi);
			}
			Long addFactor2 = divisorMap.get(equation.getDivisor());
			if(addFactor2 == null) {
				divisorMap.put(equation.getDivisor(), multi * equation.getMultiple());
			}
			else {
				divisorMap.put(equation.getDivisor(), addFactor2 + (multi * -1 * equation.getMultiple()));
			}
			divisorMap.remove(equation.getRemainder());
		}
		validate();
		return true;
	}
	

	public static ExtendedEuclideanEquation findEquations(
			Long[] factors,GreatestCommonDivisor gcd,Long result) {
		Long remainder = gcd.getGcd();
		EuclideanEquation equation = findEquation(remainder, gcd.getEquations());
		ExtendedEuclideanEquation mergedEquation = null;
		if(equation != null) {
			mergedEquation = new ExtendedEuclideanEquation(equation);
			while(equation != null) {
				Long divisor = equation.getDivisor();
				Long dividend = equation.getDividend();
				if (divisor != null && divisor != 0) {
					equation = findEquation(divisor, gcd.getEquations());
				}
				if(equation != null) {
					mergedEquation.add(equation);
				}
				if (dividend != null && dividend != 0) {
					equation = findEquation(dividend, gcd.getEquations());
				}
				if(equation != null) {
					mergedEquation.add(equation);
				}
				if(mergedEquation.isSolved(factors)) {
					return mergedEquation;
				}
			}
		}
		return mergedEquation;
	}
	

	private static EuclideanEquation findEquation(Long remainder,List<EuclideanEquation> equations) {
		for(EuclideanEquation equation : equations) {
			if(equation.getRemainder().equals(remainder)) {
				return equation;
			}
			else if (equation.getRemainder() == 0 && equation.getDivisor() == remainder) {
				return equation;
			}
		}
		return null;
	}
	
	
	
	public void validate() {
		Long testResult = 0l;
		for(Entry<Long,Long>  entries : divisorMap.entrySet()) {
			
			testResult += entries.getValue() * entries.getKey();
		}
		//System.out.println("validate value:" + testResult);
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MergedEuclideanEquation [" + commonDivisor + "=");
		for(Entry<Long,Long>  entries : divisorMap.entrySet()) {
			builder.append( entries.getValue()  + " * " + entries.getKey() + " + ");
		}
		builder.append(" ]");
		return builder.toString();
	}
	
	
}