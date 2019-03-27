package org.aztec.autumn.common.math;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.aztec.autumn.common.utils.FileUtils;
import org.aztec.autumn.common.utils.MathUtils;

import com.beust.jcommander.internal.Lists;

public class BigDecimalUtils {
	
	private static List<Integer> mersenneNumPowers = Lists.newArrayList();
	private static List<BigDecimal> primes = Lists.newArrayList();
	private static List<BigDecimal> mersennePossibleFactors = Lists.newArrayList();
	private static BigDecimal zero = new BigDecimal(0);
	
	static {
		try {
			URL url = ClassLoader.getSystemResource("mersenne_number.txt");
			System.out.println(url.getFile());
			File powerFile = new File(url.getFile());
			BufferedReader br = new BufferedReader(new FileReader(powerFile));
			String line = br.readLine();
			while(line != null && !line.isEmpty()) {
				mersenneNumPowers.add(Integer.parseInt(line));
				line = br.readLine();
			}
			URL url2 = ClassLoader.getSystemResource("1000000_primes.txt");
			File primeFile = new File(url2.getFile());
			String txtStr = FileUtils.readFileAsString(primeFile);
			String[] primeTxts = txtStr.split(",");
			for(String primeTxt : primeTxts) {
				primeTxt = primeTxt.trim();
				BigDecimal prime = new BigDecimal(primeTxt);
				primes.add(prime);
				if(isMersenneFactor(prime)) {
					mersennePossibleFactors.add(prime);
				}
			}
			Collections.sort(mersennePossibleFactors,new Comparator<BigDecimal>() {

				@Override
				public int compare(BigDecimal o1, BigDecimal o2) {
					// TODO Auto-generated method stub
					return o2.subtract(o1).compareTo(zero);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	public BigDecimalUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isMersenneFactor(BigDecimal factor) {
		
		//2kp + 1
		BigDecimal mod1 = new BigDecimal(2);
		// 1 (mod 8) 7 (mod 8)
		BigDecimal mod2 = new BigDecimal(8);
		BigDecimal remainder = factor.divideAndRemainder(mod1)[1];
		if(remainder.intValue() != 1)
			return false;
		remainder = factor.divideAndRemainder(mod2)[1];
		if(remainder.intValue() != 1 && remainder.intValue() != 7) {
			return false;
		}
		return true;
	}
	
	public int getFoundMersennePrimeNumberPower(int no) {
		return mersenneNumPowers.get(no);
	}
	
	public static BigDecimal getMersenneNumber(int power) {
		BigDecimal base = new BigDecimal(2);
		BigDecimal pow = base.pow(power);
		return pow.subtract(new BigDecimal(1));
	}

	public static BigDecimal getClosestMPN(int power) {
		
		BigDecimal mersennerNum = getMersenneNumber(power);
		return mersennerNum.subtract(new BigDecimal(1));
	}
	
	public static List<BigDecimal> divideCMPN(int power) {

		BigDecimal cmpn = getClosestMPN(power);
		int closestPower = power - 1;
		List<Long> factors = MathUtils.findAllFactors(closestPower + 0l);
		Collections.sort(factors);
		Long factor = factors.get(factors.size() - 1);
		BigDecimal factor1 = getMersenneNumber(factor.intValue());
		List<BigDecimal> retFactors = Lists.newArrayList();
		retFactors.add(factor1);
		return retFactors;
	}
	
	public static List<BigDecimal> findMersenneFactor(int power,BigDecimal mersenneNum){

		List<BigDecimal> factors = Lists.newArrayList();
		BigDecimal modular = new BigDecimal(2 * power);
		for(BigDecimal mpf : mersennePossibleFactors) {
			BigDecimal remainder = mpf.divideAndRemainder(modular)[1];
			if(remainder.intValue() != 1) {
				continue;
			}
		}
		return factors;
	}
	
	public static List<BigDecimal> findFactors(BigDecimal num) {
		List<BigDecimal> factors = Lists.newArrayList();
		BigDecimal tmp = num.subtract(new BigDecimal(0));
		boolean finished = false;
		for(int i = 0;i < primes.size();i++) {
			BigDecimal divisor = primes.get(i);

			BigDecimal[] divResult = tmp.divideAndRemainder(divisor);
			if(tmp.compareTo(divisor) == -1) {
				finished = true;
				break;
			}
			while(divResult[1].compareTo(zero) == 0) {
				factors.add(divisor);
				tmp = divResult[0];
				if(tmp.compareTo(divisor) == -1) {
					finished = true;
					break;
				}
				divResult = tmp.divideAndRemainder(divisor);
			}
			System.out.println(divResult[1].intValue());
		}
		if(!finished) {
			throw new ArithmeticException("unsupport to devide this num!this num may be prime!");
		}
		return factors;
	}
	
	
	public static BigDecimal pow(int base,int power) {
		BigDecimal bigBase = new BigDecimal(base);
		return bigBase.pow(power);
	}
	
	
	public static BigDecimal pollardDivide(BigDecimal dividend ,int lowerBound,int upperBound) {
		BigDecimal mul = dividend.add(new BigDecimal(0));
		
		return mul;
	}
}
