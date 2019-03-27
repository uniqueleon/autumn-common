package org.aztec.autumn.common.utils;

public class BetUtils {

	public BetUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static double picks(double x,double numdar) {
		
		double result = Math.pow(Math.E, -numdar) * Math.pow(numdar, x) / factorial(x);
		return result;
	}

	public static long factorial(Double x) {
		long result = 1;
		for(int i = 1;i <= x.longValue();i++) {
			result *= i;
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(picks(1,1));
	}
}
