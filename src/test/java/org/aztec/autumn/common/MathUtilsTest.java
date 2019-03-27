package org.aztec.autumn.common;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.math.BigDecimalUtils;
import org.aztec.autumn.common.math.EulerFunction;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.utils.MathUtils;
import org.junit.Test;

import com.beust.jcommander.internal.Lists;


public class MathUtilsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testBigDecimal();
		
		// 2 
		// 3 
		//diviveFactorTest();
		//divideMersenneNum(200461);
		byte[] bytes = DigestUtils.md5(new byte[] {});
		System.out.println(new String(bytes));
		/*double ratio = Math.pow(1d / 16, 2) + Math.pow( 15d /16, 2);
		double byteRatio = Math.pow(ratio, 8);
		System.out.println(byteRatio);*/
	}
	
	public static void divideMersenneNum(int power) {
		try {
			System.out.println("preparing....");
			String fileNamePrefix = "M" + power + "_factor_";
			BigDecimal cmpn = BigDecimalUtils.getMersenneNumber(power);
			System.out.println("dividing....");
			List<BigDecimal> factors = BigDecimalUtils.findFactors(cmpn);
			for(int i = 0;i < factors.size();i++) {
				System.out.println("output divide result...." + i);
				BigDecimal factor = factors.get(i);
				FileUtils.write(new File(fileNamePrefix + i), factor.toString(), "UTF-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void diviveFactorTest() {
		Long testNum = new Double(Math.pow(2, 103)).longValue();
		System.out.println(testNum);
		List<Long> factor = MathUtils.findAllFactors(testNum);
		System.out.println(factor);
	}
	
	
	public static void testBigDecimal() {
		BigDecimalUtils.getMersenneNumber(1);
		Long curTime = System.currentTimeMillis();
		//BigDecimal cmpn = BigDecimalUtils.getClosestMPN(50);
		//BigDecimal power3 = BigDecimalUtils.pow(6, 200461);
		//double power = Math.pow(6, 200);
		Long useTime = System.currentTimeMillis() - curTime;
		System.out.println("use time : " + useTime);
		/*try {
			//FileUtils.write(new File("test/mersene/mersene.txt"), cmpn.toPlainString(),"UTF-8");
			//FileUtils.write(new File("test/mersene/factor2.txt"), factor2.toString(),"UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//base.
		// 2 ^ 82589933 - 1
		// 2 * (2 ^ 82589932 - 1)
		//System.out.println(base.pow(82589933));
		
		//Integer test1 = 23;
		//2 ^ M - 1 n M is primitive
		//2 ^ M - 2 = 2 (2 ^ (M - 1) - 1)
		// if  2 ^ (M - 1) - 1 
		// because 2 | M - 1 , so M - 1 = 2t
		// so 1 + 2 = 3 | (2 ^ (M - 1) - 1)
		// 2 * 
		//System.out.println(Math.pow(2, 23));
		//8388607
		//
		//System.out.println(Integer.toBinaryString(23));
		//System.out.println(Integer.toBinaryString(89));
		// 2 ^ 23 - 1 = 8388607
		// 2 ^ 29 - 1 =  
		//devidePowerNum();
	}
	
	public static void calMerseneNum() {
		BigDecimal base = new  BigDecimal(2);
		// 82589932 = 2 * 2 * 103 * 200461
		// 82589932 = 412 * 200461
		// 200461 - 1
		BigDecimal one = new BigDecimal(1);
		int pow = 200461;
		BigDecimal add1 = base.pow(pow);
		//BigDecimal merseneBroNum = base.pow(82589932).subtract(new BigDecimal(1));
		BigDecimal factor1 = add1.add(one);
		//BigDecimal factor2 = merseneBroNum.divide(factor1);
		try {
			FileUtils.write(new File("test/mersene/factor1.txt"), factor1.toPlainString(),"UTF-8");
			//FileUtils.write(new File("test/mersene/factor2.txt"), factor2.toString(),"UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void devidePowerNum () {
		
		// 1 -> 10 + 1 -> 111
		// 111 -> 1110 + 111 -> 1001
		// 1001 -> 10010 + 1001 -> 11011
		// 11011 -> 110110 + 11011 ->  0001
		List<Long> factors = MathUtils.findAllFactors(82589932l);
		System.out.println(factors);
		for(Long factor : factors) {
			System.out.println(factor);
		}
	}
	
	public static void devideMerseneNum() {
		Long testMerseneNum = new Double(Math.pow(2, 29) - 1).longValue() ;
		System.out.println(testMerseneNum);
		List<Long> factors = MathUtils.findAllFactors(testMerseneNum);
		System.out.println(factors);
		for(Long factor : factors) {
			System.out.println(Long.toBinaryString(factor));
		}
	}
	
	
	public static void testDEMod(int base,int mod1, int mod2, int upperLimit) {

		BigDecimal bd = new BigDecimal(new Long(base));
		BigDecimal mod1bd = new BigDecimal(new Long(mod1));
		BigDecimal mod2bd = new BigDecimal(new Long(mod2));
		for(int i = 0;i < upperLimit;i++) {
			BigDecimal tmpBd = bd.add(mod1bd.multiply(new BigDecimal(i)));
			BigDecimal remainder = tmpBd.divideAndRemainder(mod2bd)[1];
			System.out.println(base + " + " + i + " * " + mod1 + " = " + remainder.intValue() + " (mod " + mod2 + ")");
			
		}
	}
	
	public static void testPowMod(int base,int modular, int upperLimit) {

		BigDecimal bd = new BigDecimal(new Long(base));
		BigDecimal mod = new BigDecimal(new Long(modular));
		for(int i = 0;i < upperLimit;i++) {
			BigDecimal tmpBd = bd.pow(i);
			BigDecimal remainder = tmpBd.divideAndRemainder(mod)[1];
			System.out.println(base + " ^ " + i + " = " + remainder.intValue() + " (mod " + mod.intValue() + ")");
			
		}
	}
	
	//@Test
	public void testEulerFunction() {
		int numSize = 100;
		Long testNum = RandomUtils.nextLong() % numSize;
		while(testNum == 0) {
			testNum = RandomUtils.nextLong() % numSize;
		}
		System.out.println("number :" + testNum);
		Long eulerNumber = EulerFunction.getResult(testNum);
		System.out.println("result:" + eulerNumber);
		System.out.println(eulerNumber);
		if(!validateEulerResult(testNum, eulerNumber)) {
			System.err.println("euler fail!");
			System.exit(-1);
		}
	}
	
	private boolean validateEulerResult(long number,long eulerResult) {
		long testGcdNumber = number - 1;
		for(long i = 2;i < number;i ++ ) {
			GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(new Long[] {i,number});
			if(gcd.getGcd() != 1) {
				testGcdNumber --;
			}
		}
		return testGcdNumber == eulerResult;
	}
	
	//@Test
	public void testFindFactors2() {


		Long testNum = 16l;
		System.out.println("testing number:" + testNum);
		List<Long> factors = MathUtils.findUniqueFactors(testNum);
		System.out.println("diviede factors:");
		System.out.println(factors);

		List<Long> allFactors = MathUtils.findAllFactors(testNum);
		System.out.println("all factors");
		System.out.println(allFactors);
	}
	
	//@Test
	public void testFindFactors() {
		
		int testTime = 10000;
		int numSize = 10000;
		for(int i = 0;i < testTime;i++) {

			Long testNum = RandomUtils.nextLong() % numSize;
			while(testNum == 0) {
				testNum = RandomUtils.nextLong() % numSize;
			}
			//Long testNum = 68l;
			System.out.println("testing number:" + testNum);
			List<Long> factors = MathUtils.findUniqueFactors(testNum);
			System.out.println("diviede factors:");
			System.out.println(factors);

			List<Long> allFactors = MathUtils.findAllFactors(testNum);
			System.out.println("all factors");
			System.out.println(allFactors);
			System.out.println(">>>>>>>>>>>>validating<<<<<<<<<<<<<");
			
			Long testResult = 1l;
			for(int j = 0;j < allFactors.size();j++) {
				testResult *= allFactors.get(j);
			}
			if(!testResult.equals(testNum)) {
				System.err.println("validate fail!!!!!!");
				System.exit(-1);
			}
			System.out.println(">>>>>>>>>>>>validate finished<<<<<<<<<<<<<");
		}
	}
	
	@Test
	public void testFindPrime() {
		List<Integer> primes = MathUtils.findPrimeFactor(100000000);
		System.out.println(primes);
	}

	//@Test
	public void test1() {
		List<Integer> pos = new ArrayList<>();
		pos.add(0);
		//pos.add(3);
		pos.add(4);
		pos.add(10);
		pos.add(11);
		BitSet bitSet = new BitSet();
		bitSet.set(1);
		bitSet.set(2);
		bitSet.set(5);
		bitSet.set(6);
		bitSet.set(7);
		bitSet.set(9);
		bitSet.set(12);
		System.out.println(MathUtils.isBitClear(2l, pos));
		
		List<Integer> testIndexes = MathUtils.getNextPermutation(Lists.newArrayList(), 3, new int[] {3,3,3});
		while(testIndexes != null) {
			System.out.println(testIndexes);
			testIndexes = MathUtils.getNextPermutation(testIndexes, 3, new int[] {3,3,3});
		}
	}

}
