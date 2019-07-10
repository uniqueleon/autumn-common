package org.aztec.autumn.common.math;

import java.math.BigInteger;

public class PrimeTester {

	public PrimeTester() {
		// TODO Auto-generated constructor stub
	}
	
	private PrimeTestResult test(BigInteger testNum,BigInteger modulus,BigInteger roundLimit) {
		
		BigInteger round = BigInteger.valueOf(1l);
		BigInteger remainder = testNum.mod(modulus);
		if(roundLimit.equals(BigInteger.valueOf(0l)))
			return new PrimeTestResult(testNum, round, modulus,false);
		while(!remainder.isProbablePrime(10000)) {
			remainder = remainder.multiply(remainder);
			remainder = remainder.mod(modulus);
			round = round.add(BigInteger.valueOf(1l));
			if(round.compareTo(roundLimit) > 0) {
				return new PrimeTestResult(testNum, round, modulus,false);
			}
		}
		return new PrimeTestResult(testNum,modulus,remainder,round);
	}
	
	public PrimeTestResult test(long val1,long val2,long roundlimit) {
		return test(BigInteger.valueOf(val1),BigInteger.valueOf(val2),roundlimit > 0 ? BigInteger.valueOf(roundlimit) : BigInteger.valueOf(0l));
	}

	public static void main(String[] args) {
		
		int testCount = 0;
		PrimeTester pt = new PrimeTester();
		long curTime = System.currentTimeMillis();
		int roundLimit = 10;
		long modulus = 999983l;
		PrimeTestResult ptr = pt.test(System.currentTimeMillis(), modulus,roundLimit);
		while(!ptr.isSuccess()) {
			testCount ++;
			ptr = pt.test(System.currentTimeMillis(), modulus, roundLimit);
		}
		System.out.println("use time:" + (System.currentTimeMillis() - curTime) + ",count=" + testCount);
		System.out.println(ptr);
	}
}
