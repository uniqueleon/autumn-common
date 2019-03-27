package org.aztec.autumn.common.math.equations;

import java.util.List;

import com.google.common.collect.Lists;

public class GreatestCommonDivisor{
	private Long gcd;
	private List<EuclideanEquation> equations;
	public Long getGcd() {
		return gcd;
	}
	public void setGcd(Long gcd) {
		this.gcd = gcd;
	}
	public List<EuclideanEquation> getEquations() {
		return equations;
	}
	public void setEquations(List<EuclideanEquation> equations) {
		this.equations = equations;
	}
	public GreatestCommonDivisor(Long gcd, List<EuclideanEquation> equations) {
		super();
		this.gcd = gcd;
		this.equations = equations;
	}
	
	public static GreatestCommonDivisor calculate(Long num1,Long num2) {
		Long remainder = Long.MAX_VALUE;
		Long divide1 = num1 > num2 ? num1 : num2;
		Long divide2 = num2 < num1 ? num2 : num1;
		List<EuclideanEquation> equations = Lists.newArrayList();
		while(remainder != 0) {
			remainder = divide1 % divide2;
			if(remainder == 1) {
				equations.add(new EuclideanEquation(divide1, divide1/divide2, divide2, remainder));
				return new GreatestCommonDivisor(1l, equations);
			} 
			else if(remainder == 0) {
				equations.add(new EuclideanEquation(divide1, divide1/divide2, divide2, 0l));
				return new GreatestCommonDivisor(divide2, equations);
			}
			equations.add(new EuclideanEquation(divide1, divide1/divide2, divide2, remainder));
			divide1 = divide2;
			divide2 = remainder;
			
		}
		return new GreatestCommonDivisor(divide2, equations);
	}
	

	public static GreatestCommonDivisor getGCD(Long[] factors) {
		if(factors.length == 0)
			return null;
		GreatestCommonDivisor gcd = new GreatestCommonDivisor(factors[0], Lists.newArrayList());
		for(int i = 1;i < factors.length;i++) {
			GreatestCommonDivisor tmpGcd = calculate(gcd.getGcd(), factors[i]);
			gcd.setGcd(tmpGcd.getGcd());
			gcd.getEquations().addAll(tmpGcd.getEquations());
		}
		return gcd;
	}
	
	public static List<GreatestCommonDivisor> getSequenceGCD(Long[] factors){
		List<GreatestCommonDivisor> retDivisors = Lists.newArrayList();
		Long gcd = null;
		for(int i = 0;i < factors.length;i++) {
			gcd = factors[i];
			GreatestCommonDivisor tmpGcd = new GreatestCommonDivisor(gcd, Lists.newArrayList());
			for(int j = i + 1; j < factors.length;j++) {
				tmpGcd = calculate(factors[j], gcd);
				gcd = tmpGcd.getGcd();
			}
			retDivisors.add(tmpGcd);
		}
		return retDivisors;
	}
	

	public static List<List<GreatestCommonDivisor>> getAllGCD(Long[] factors){
		List<List<GreatestCommonDivisor>> allGcds = Lists.newArrayList();
		for(int i = 0;i < factors.length - 1;i++) {
			List<GreatestCommonDivisor> rowGcds = Lists.newArrayList();
			for(int j = i + 1; j < factors.length ;j ++) {
				rowGcds.add(getGCD(new Long[] {factors[i],factors[j]}));
			}
			allGcds.add(rowGcds);
		}
		return allGcds;
	}
	
	public static Object[] findMinimumGCD(Long mainNum,Long[] nums) {
		GreatestCommonDivisor gcd = null;
		Integer findIndex = -1;
		if(nums != null && nums.length > 0) {
			for(int i = 0;i < nums.length;i++) {
				GreatestCommonDivisor testGcd = calculate(mainNum,nums[i]);
				if(gcd == null || gcd.getGcd() > testGcd.getGcd()) {
					gcd = testGcd;
					findIndex = i;
				}
			}
		}
		return new Object[] {findIndex,gcd};
	}
	
	public static Object[] findMinimumRatioGCD(Long mainNum,Long[] nums) {
		GreatestCommonDivisor gcd = null;
		Integer findIndex = -1;
		Long minRatio = 0l;
		if(nums != null && nums.length > 0) {
			for(int i = 0;i < nums.length;i++) {
				GreatestCommonDivisor testGcd = calculate(mainNum,nums[i]);
				Long testRatio = nums[i] / testGcd.getGcd();
				if(gcd == null || minRatio > testRatio) {
					gcd = testGcd;
					findIndex = i;
				}
			}
		}
		return new Object[] {findIndex,gcd};
	}
	
	@Override
	public String toString() {
		return "GreatestCommonDivisor [gcd=" + gcd + ", equations=" + equations + "]";
	}
	
	
}