package org.aztec.autumn.common.math.modeling.sales;

public class MarketPlace {
	
	

	public MarketPlace() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// 
		//30 * 1000 = 30000 10000
		System.out.println("final result:" + accumulateData(100000000));
		System.out.println(calculateEarnning(100,100000,1.0));
	}
	
	public static double calculateEarnning(int num,int totalNum,double effector ) {
		double result = totalNum / num;
		return result * effector;
	}
	
	
	public static double accumulateData(int num) {
		double accumulateResult = 0d;
		for(int i = 0;i < num;i++) {
			accumulateResult += 1d / (i + 1);
			System.out.println(accumulateResult);
		}
		return num * accumulateResult;
	}
}
