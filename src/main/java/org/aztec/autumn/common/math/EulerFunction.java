package org.aztec.autumn.common.math;

import java.util.List;

import org.aztec.autumn.common.utils.MathUtils;

public class EulerFunction {

	public EulerFunction() {
		// TODO Auto-generated constructor stub
	}

	public static long getResult(long n) {
		
		Double result = new Double(n);
		//List<Integer> factors = MathUtils.findFactors(n);
		List<Long> uniqueFactors =  MathUtils.findUniqueFactors(n);
		for(int i = 0;i < uniqueFactors.size();i++) {
			Long factor = uniqueFactors.get(i);
			double ratio = (1d - 1d / factor);
			result *= ratio;
		}
		return result.longValue();
	}
	
	public static Long getPrimitiveRoot(long testNum) {
		Long root = null;
		return root;
	}
	
	public static void main(String[] args) {
		System.out.println(EulerFunction.getResult(12));

		System.out.println(EulerFunction.getResult(135));
	}
}
