package org.aztec.autumn.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.math.EulerFunction;
import org.aztec.autumn.common.math.MathException;
import org.aztec.autumn.common.math.MathException.GeneralErrorCode;
import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.utils.security.UnsupportAlgorithmException;

import com.google.common.collect.Lists;

public class MathUtils {

	private static Integer MAX_PRIME_RANGE = 10000;
	private static List<Integer> INIT_PRIMES = Lists.newArrayList(new Integer[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29,
			31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139,
			149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257,
			263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383,
			389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509,
			521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647,
			653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797,
			809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941,
			947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063,
			1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201,
			1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319,
			1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471,
			1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531, 1543, 1549, 1553, 1559, 1567, 1571, 1579, 1583, 1597,
			1601, 1607, 1609, 1613, 1619, 1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723,
			1733, 1741, 1747, 1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873,
			1877, 1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997, 1999, 2003, 2011,
			2017, 2027, 2029, 2039, 2053, 2063, 2069, 2081, 2083, 2087, 2089, 2099, 2111, 2113, 2129, 2131, 2137, 2141,
			2143, 2153, 2161, 2179, 2203, 2207, 2213, 2221, 2237, 2239, 2243, 2251, 2267, 2269, 2273, 2281, 2287, 2293,
			2297, 2309, 2311, 2333, 2339, 2341, 2347, 2351, 2357, 2371, 2377, 2381, 2383, 2389, 2393, 2399, 2411, 2417,
			2423, 2437, 2441, 2447, 2459, 2467, 2473, 2477, 2503, 2521, 2531, 2539, 2543, 2549, 2551, 2557, 2579, 2591,
			2593, 2609, 2617, 2621, 2633, 2647, 2657, 2659, 2663, 2671, 2677, 2683, 2687, 2689, 2693, 2699, 2707, 2711,
			2713, 2719, 2729, 2731, 2741, 2749, 2753, 2767, 2777, 2789, 2791, 2797, 2801, 2803, 2819, 2833, 2837, 2843,
			2851, 2857, 2861, 2879, 2887, 2897, 2903, 2909, 2917, 2927, 2939, 2953, 2957, 2963, 2969, 2971, 2999, 3001,
			3011, 3019, 3023, 3037, 3041, 3049, 3061, 3067, 3079, 3083, 3089, 3109, 3119, 3121, 3137, 3163, 3167, 3169,
			3181, 3187, 3191, 3203, 3209, 3217, 3221, 3229, 3251, 3253, 3257, 3259, 3271, 3299, 3301, 3307, 3313, 3319,
			3323, 3329, 3331, 3343, 3347, 3359, 3361, 3371, 3373, 3389, 3391, 3407, 3413, 3433, 3449, 3457, 3461, 3463,
			3467, 3469, 3491, 3499, 3511, 3517, 3527, 3529, 3533, 3539, 3541, 3547, 3557, 3559, 3571, 3581, 3583, 3593,
			3607, 3613, 3617, 3623, 3631, 3637, 3643, 3659, 3671, 3673, 3677, 3691, 3697, 3701, 3709, 3719, 3727, 3733,
			3739, 3761, 3767, 3769, 3779, 3793, 3797, 3803, 3821, 3823, 3833, 3847, 3851, 3853, 3863, 3877, 3881, 3889,
			3907, 3911, 3917, 3919, 3923, 3929, 3931, 3943, 3947, 3967, 3989, 4001, 4003, 4007, 4013, 4019, 4021, 4027,
			4049, 4051, 4057, 4073, 4079, 4091, 4093, 4099, 4111, 4127, 4129, 4133, 4139, 4153, 4157, 4159, 4177, 4201,
			4211, 4217, 4219, 4229, 4231, 4241, 4243, 4253, 4259, 4261, 4271, 4273, 4283, 4289, 4297, 4327, 4337, 4339,
			4349, 4357, 4363, 4373, 4391, 4397, 4409, 4421, 4423, 4441, 4447, 4451, 4457, 4463, 4481, 4483, 4493, 4507,
			4513, 4517, 4519, 4523, 4547, 4549, 4561, 4567, 4583, 4591, 4597, 4603, 4621, 4637, 4639, 4643, 4649, 4651,
			4657, 4663, 4673, 4679, 4691, 4703, 4721, 4723, 4729, 4733, 4751, 4759, 4783, 4787, 4789, 4793, 4799, 4801,
			4813, 4817, 4831, 4861, 4871, 4877, 4889, 4903, 4909, 4919, 4931, 4933, 4937, 4943, 4951, 4957, 4967, 4969,
			4973, 4987, 4993, 4999, 5003, 5009, 5011, 5021, 5023, 5039, 5051, 5059, 5077, 5081, 5087, 5099, 5101, 5107,
			5113, 5119, 5147, 5153, 5167, 5171, 5179, 5189, 5197, 5209, 5227, 5231, 5233, 5237, 5261, 5273, 5279, 5281,
			5297, 5303, 5309, 5323, 5333, 5347, 5351, 5381, 5387, 5393, 5399, 5407, 5413, 5417, 5419, 5431, 5437, 5441,
			5443, 5449, 5471, 5477, 5479, 5483, 5501, 5503, 5507, 5519, 5521, 5527, 5531, 5557, 5563, 5569, 5573, 5581,
			5591, 5623, 5639, 5641, 5647, 5651, 5653, 5657, 5659, 5669, 5683, 5689, 5693, 5701, 5711, 5717, 5737, 5741,
			5743, 5749, 5779, 5783, 5791, 5801, 5807, 5813, 5821, 5827, 5839, 5843, 5849, 5851, 5857, 5861, 5867, 5869,
			5879, 5881, 5897, 5903, 5923, 5927, 5939, 5953, 5981, 5987, 6007, 6011, 6029, 6037, 6043, 6047, 6053, 6067,
			6073, 6079, 6089, 6091, 6101, 6113, 6121, 6131, 6133, 6143, 6151, 6163, 6173, 6197, 6199, 6203, 6211, 6217,
			6221, 6229, 6247, 6257, 6263, 6269, 6271, 6277, 6287, 6299, 6301, 6311, 6317, 6323, 6329, 6337, 6343, 6353,
			6359, 6361, 6367, 6373, 6379, 6389, 6397, 6421, 6427, 6449, 6451, 6469, 6473, 6481, 6491, 6521, 6529, 6547,
			6551, 6553, 6563, 6569, 6571, 6577, 6581, 6599, 6607, 6619, 6637, 6653, 6659, 6661, 6673, 6679, 6689, 6691,
			6701, 6703, 6709, 6719, 6733, 6737, 6761, 6763, 6779, 6781, 6791, 6793, 6803, 6823, 6827, 6829, 6833, 6841,
			6857, 6863, 6869, 6871, 6883, 6899, 6907, 6911, 6917, 6947, 6949, 6959, 6961, 6967, 6971, 6977, 6983, 6991,
			6997, 7001, 7013, 7019, 7027, 7039, 7043, 7057, 7069, 7079, 7103, 7109, 7121, 7127, 7129, 7151, 7159, 7177,
			7187, 7193, 7207, 7211, 7213, 7219, 7229, 7237, 7243, 7247, 7253, 7283, 7297, 7307, 7309, 7321, 7331, 7333,
			7349, 7351, 7369, 7393, 7411, 7417, 7433, 7451, 7457, 7459, 7477, 7481, 7487, 7489, 7499, 7507, 7517, 7523,
			7529, 7537, 7541, 7547, 7549, 7559, 7561, 7573, 7577, 7583, 7589, 7591, 7603, 7607, 7621, 7639, 7643, 7649,
			7669, 7673, 7681, 7687, 7691, 7699, 7703, 7717, 7723, 7727, 7741, 7753, 7757, 7759, 7789, 7793, 7817, 7823,
			7829, 7841, 7853, 7867, 7873, 7877, 7879, 7883, 7901, 7907, 7919, 7927, 7933, 7937, 7949, 7951, 7963, 7993,
			8009, 8011, 8017, 8039, 8053, 8059, 8069, 8081, 8087, 8089, 8093, 8101, 8111, 8117, 8123, 8147, 8161, 8167,
			8171, 8179, 8191, 8209, 8219, 8221, 8231, 8233, 8237, 8243, 8263, 8269, 8273, 8287, 8291, 8293, 8297, 8311,
			8317, 8329, 8353, 8363, 8369, 8377, 8387, 8389, 8419, 8423, 8429, 8431, 8443, 8447, 8461, 8467, 8501, 8513,
			8521, 8527, 8537, 8539, 8543, 8563, 8573, 8581, 8597, 8599, 8609, 8623, 8627, 8629, 8641, 8647, 8663, 8669,
			8677, 8681, 8689, 8693, 8699, 8707, 8713, 8719, 8731, 8737, 8741, 8747, 8753, 8761, 8779, 8783, 8803, 8807,
			8819, 8821, 8831, 8837, 8839, 8849, 8861, 8863, 8867, 8887, 8893, 8923, 8929, 8933, 8941, 8951, 8963, 8969,
			8971, 8999, 9001, 9007, 9011, 9013, 9029, 9041, 9043, 9049, 9059, 9067, 9091, 9103, 9109, 9127, 9133, 9137,
			9151, 9157, 9161, 9173, 9181, 9187, 9199, 9203, 9209, 9221, 9227, 9239, 9241, 9257, 9277, 9281, 9283, 9293,
			9311, 9319, 9323, 9337, 9341, 9343, 9349, 9371, 9377, 9391, 9397, 9403, 9413, 9419, 9421, 9431, 9433, 9437,
			9439, 9461, 9463, 9467, 9473, 9479, 9491, 9497, 9511, 9521, 9533, 9539, 9547, 9551, 9587, 9601, 9613, 9619,
			9623, 9629, 9631, 9643, 9649, 9661, 9677, 9679, 9689, 9697, 9719, 9721, 9733, 9739, 9743, 9749, 9767, 9769,
			9781, 9787, 9791, 9803, 9811, 9817, 9829, 9833, 9839, 9851, 9857, 9859, 9871, 9883, 9887, 9901, 9907, 9923,
			9929, 9931, 9941, 9949, 9967, 9973 });
	private static List<Integer> _2_BASE_PSEUDO_PRIME = Lists.newArrayList(new Integer[] { 341, 561, 645, 1105, 1387,
			1729, 1905, 2047, 2465, 2701, 2821, 3277, 4033, 4369, 4371, 4681, 5461, 6601, 7957, 8321, 8481, 8911 });
	private static List<Integer> FIBONACCI_NUMS = Lists.newArrayList(
			new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765 });

	public static Double getStandardDeviation(Integer[] samples) {

		Double average = new Double(0);
		for (int i = 0; i < samples.length; i++) {
			average += samples[i];
		}
		average = average / samples.length;
		Double temp = new Double(0);
		for (int i = 0; i < samples.length; i++) {
			temp += Math.pow((samples[i] - average), 2);
		}
		temp = temp / samples.length;
		temp = Math.pow(temp, 0.5);
		return temp;
	}

	public static List<Integer> findPrimeFactor(Integer upperLimit) {
		List<Integer> tempList = new ArrayList<Integer>();
		for (int i = 0; i < upperLimit; i++) {
			tempList.add(i);
		}
		List<Integer> primeNum = new ArrayList<>();
		primeNum.add(2);
		primeNum.add(3);
		for (int i = 4; i < upperLimit; i++) {
			Integer tempNum = tempList.get(i);
			if (tempNum == null)
				continue;
			boolean isPrime = true;
			for (int j = 0; j < primeNum.size(); j++) {
				isPrime = (tempNum % primeNum.get(j) == 0) ? false : true;
				if (!isPrime)
					break;
			}
			if (isPrime) {
				primeNum.add(tempNum);
				for (int k = 2; (k * tempNum) < upperLimit; k++) {
					tempList.set(k * tempNum, null);
				}
			}
		}
		return primeNum;
	}

	public static List<Integer> findFactor(Integer testNum, List<Integer> primeFactors) {
		List<Integer> factors = new ArrayList<>();

		Integer workTemp = testNum;
		for (int i = 0; i < primeFactors.size(); i++) {
			Integer primeNum = primeFactors.get(i);
			if (testNum == primeNum) {
				factors.add(primeNum);
				break;
			} else if (testNum < primeNum) {
				break;
			}
			if (workTemp % primeNum == 0) {
				factors.add(primeNum);
				do {
					workTemp = workTemp / primeNum;
				} while (workTemp % primeNum == 0);
			}
		}
		return factors;
	}

	public static List<Long> findUniqueFactors(Long testNum) {
		List<Long> factors = new ArrayList<>();
		Long testTime = new Double(Math.pow(testNum, 0.5)).longValue();
		Long tempNum = testNum.longValue();
		for (long i = 2; i <= testTime; i++) {
			if (tempNum % i == 0) {
				factors.add(i);
				tempNum = tempNum / i;
				while (tempNum >= i && tempNum % i == 0) {
					tempNum = tempNum / i;
				}
			}
			if (tempNum == 1 || tempNum < i)
				break;
		}
		if (tempNum != 1) {
			factors.add(tempNum);
		}
		return factors;
	}

	public static List<Long> findAllFactors(Long testNum) {
		List<Long> uniqueFactors = findUniqueFactors(testNum);
		List<Long> allFactors = Lists.newArrayList();
		Long tempNum = new Long(testNum);
		for (int i = 0; i < uniqueFactors.size(); i++) {
			Long testFactor = uniqueFactors.get(i);
			while (tempNum >= testFactor && tempNum % testFactor == 0) {
				allFactors.add(testFactor);
				tempNum = tempNum / testFactor;
			}
		}
		return allFactors;
	}

	public static boolean isFactorSatisfied(int testNum, List<Integer> primeNum, int factorDemand) {
		Integer testDivNum = Math.abs(testNum);
		List<Integer> factorList = findFactor(testDivNum, primeNum);
		if (factorList.size() == factorDemand) {
			return true;
		}
		return false;
	}

	public static boolean isBitClear(long testNum, List<Integer> pos) {
		BitSet bitSet = BitSet.valueOf(new long[] { testNum });
		for (int i = 0; i < pos.size(); i++) {
			if (bitSet.get(pos.get(i)))
				return false;
		}
		return true;
	}

	public static int multiple(int[] nums) {
		int result = 1;
		for (int num : nums) {
			result *= num;
		}
		return result;
	}

	public static Long[] findPossibleRange(Long[][] ranges, List<Long> coefficients) {
		Long minResult = 0l;
		Long maxResult = 0l;
		for (int i = 0; i < ranges.length; i++) {
			minResult += ranges[i][0] * (coefficients.size() > i ? coefficients.get(i) : 1);
			maxResult += ranges[i][1] * (coefficients.size() > i ? coefficients.get(i) : 1);
		}
		return new Long[] { minResult, maxResult };
	}

	public static Long findModularInversor(Long base, Long modular) {
		if (base == 1)
			return modular;
		if (modular.equals(base)) {
			return 1l;
		}
		DiophantineResult dr = DiophantineEquation.getSolution(new Long[] { base, modular }, 1l);
		if (!dr.getHasSolution()) {
			throw new MathException(GeneralErrorCode.NO_MODULAR_INVERSOR, String
					.format("The modular inversor of [%1$S,%2$S] is not exists!", new Object[] { base, modular }));
		}
		long inversor = dr.getBaseResults().get(0);
		return getLeastRemainder(inversor, modular);
	}

	public static Long getLeastRemainder(Long remainder, Long modular) {
		if (remainder == 0 || remainder % modular == 0)
			return 0l;
		if (remainder > 0) {
			return remainder > modular ? remainder % modular : remainder;
		} else {
			return -(remainder / modular - 1) * modular + remainder;
		}
	}

	public static Stack<Long> getNRadixNumberAsStack(Long number, Integer redix) {
		Stack<Long> numberStack = new Stack<Long>();
		Long tmpNum = number;
		if (number == 0) {
			numberStack.push(0l);
			return numberStack;
		} else {
			while (tmpNum != 0) {
				numberStack.push(tmpNum % redix);
				tmpNum = tmpNum / redix;
				if (tmpNum < redix) {
					numberStack.push(tmpNum);
					break;
				}
			}
		}
		return numberStack;
	}

	public static String getNRadixNumberAsString(Long number, Integer redix) {
		StringBuilder numberstr = new StringBuilder();
		Stack<Long> numberQueue = getNRadixNumberAsStack(number, redix);
		while (!numberQueue.isEmpty()) {

			numberstr.append(numberQueue.pop() + "(" + redix + ")");
		}
		return numberstr.toString();
	}

	/**
	 * 获取下一个排列组合
	 * 
	 * @param indexes
	 * @param size
	 * @param limit
	 * @param maxIndex
	 * @return
	 */
	public static List<Integer> getNextCombination(List<Integer> indexes, int size, int limit, int maxIndex) {
		if (size > maxIndex + 1)
			throw new IllegalArgumentException(
					"Can't get Combination(size=" + size + ") of number(limit=" + maxIndex + ")");
		List<Integer> newIndexes = Lists.newArrayList();
		if (indexes == null || indexes.size() == 0) {
			for (int i = 0; i < size; i++) {
				newIndexes.add(i);
			}
			return newIndexes;
		}
		newIndexes.addAll(indexes);
		int changeIndex = indexes.size() - 1;
		Integer testIndex = new Integer(newIndexes.get(changeIndex));
		while (testIndex > (maxIndex - (size - changeIndex))) {
			changeIndex--;
			if (changeIndex < 0 || changeIndex == limit)
				return null;
			testIndex = newIndexes.get(changeIndex);
		}
		for (int i = changeIndex; i < newIndexes.size(); i++) {
			Integer newValue = testIndex + (i - changeIndex + 1);
			newIndexes.set(i, newValue);
		}

		return newIndexes;
	}

	public static List<Integer> getNextPermutation(List<Integer> indexes, int size, int[] limits) {
		List<Integer> newIndexes = Lists.newArrayList();
		if (indexes == null || indexes.size() != size) {
			for (int i = 0; i < size; i++) {
				newIndexes.add(0);
			}
			return newIndexes;
		} else {
			for (int i = 0; i < indexes.size(); i++) {
				newIndexes.add(indexes.get(i));
			}
		}

		int changeIndex = newIndexes.size() - 1;
		Integer testIndex = new Integer(newIndexes.get(changeIndex));
		while (testIndex >= limits[changeIndex] - 1) {
			changeIndex--;
			if (changeIndex < 0)
				return null;
			testIndex = newIndexes.get(changeIndex);
		}
		newIndexes.set(changeIndex, testIndex + 1);
		for (int i = changeIndex + 1; i < newIndexes.size(); i++) {
			newIndexes.set(i, 0);
		}
		return newIndexes;
	}

	/**
	 * 求组合数
	 * 
	 * @param base
	 * @param size
	 * @return
	 */
	public static Long getCombinationNumber(long base, long size) {
		long count = 0l;
		Long result = 1l;
		while (count < size) {
			result *= (base - count);
			result /= (count + 1);
			count++;
		}
		return result;
	}

	public static List<Long> findPossibleNumbers(Long[] range, Long modular, Long remainder) {
		Long minResult = new Double(Math.ceil(range[0] / modular * 1d)).longValue() * modular + remainder;
		Long maxResult = new Double(Math.floor(range[1] / modular * 1d)).longValue() * modular + remainder;
		if (maxResult > range[1]) {
			maxResult -= modular;
		}
		if (minResult < range[0]) {
			minResult += modular;
		}
		if (minResult > range[1] || maxResult < range[0]) {
			return Lists.newArrayList();
		}
		Long cursor = minResult;
		List<Long> solutions = Lists.newArrayList();
		while (cursor <= maxResult) {
			solutions.add(cursor);
			cursor += modular;
		}
		return solutions;
	}

	public static List<List<Long>> findDEPossibleSolution(Long[] factors, Long result, Long[][] ranges) {
		if (factors.length != 2) {
			throw new ArithmeticException("Only support two factors diophantine equation!");
		}
		if (ranges.length != 2) {
			throw new ArithmeticException("Solution range is not match the factor size!");
		}
		DiophantineResult dr = DiophantineEquation.getSolution(factors, result);
		List<List<Long>> solutions = Lists.newArrayList();
		if (dr.getHasSolution()) {
			Long result1 = new Long(dr.getBaseResults().get(0));
			Long result2 = new Long(dr.getBaseResults().get(1));
			Long factor1 = dr.getCoefficients().get(1) / dr.getGcd();
			Long factor2 = -dr.getCoefficients().get(0) / dr.getGcd();
			Long moveStep1 = result1 > ranges[0][0]
					? -new Double(Math.floor((result1 - ranges[0][0]) / factor1.doubleValue())).longValue()
					: new Double(Math.ceil((ranges[0][0] - result1) / factor1.doubleValue())).longValue();

			result1 += moveStep1 * factor1;
			result2 += moveStep1 * factor2;
			while (result1 < ranges[0][1]) {
				if (result2 >= ranges[0][0] && result2 <= ranges[0][1]) {
					List<Long> findSolution = Lists.newArrayList();
					findSolution.add(new Long(result1));
					findSolution.add(new Long(result2));
					solutions.add(findSolution);
				}
				result1 += factor1;
				result2 += factor2;
			}

		}
		return solutions;
	}

	public static boolean isPrime(int number) throws UnsupportAlgorithmException {
		if (number >= MAX_PRIME_RANGE)
			throw new UnsupportAlgorithmException("Number exceed!");
		return INIT_PRIMES.contains(new Integer(number));
	}

	public static boolean isPseudoPrime(int number, int base) throws UnsupportAlgorithmException {
		if (isPrime(number))
			return false;
		BigDecimal bd1 = new BigDecimal(number);
		BigDecimal bd2 = new BigDecimal(base);
		BigDecimal bdp = bd2.pow(number - 1);
		BigDecimal remainder = bdp.divideAndRemainder(bd1)[1];
		if (remainder.intValue() == 1) {
			return true;
		}
		return false;
	}

	public static List<Integer> findPseudoPrimes(int upperLimit, int base) throws UnsupportAlgorithmException {
		List<Integer> pseudoPrimes = Lists.newArrayList();
		for (int i = 4; i < upperLimit; i++) {
			if (isPseudoPrime(i, base)) {
				pseudoPrimes.add(i);
			}
		}
		return pseudoPrimes;
	}
	

	public static List<Integer> getRandomPermutation(List<Integer> indexes,int size,int[] limits
			,List<Integer> histories){
		List<Integer> newIndexes = Lists.newArrayList();
		double maxSize = 1;
		int count = 0;
		for(int i = 0;i < limits.length;i++){
			maxSize *= limits[i];
		}
		if(histories.size() >= maxSize){
			return null;
		}
		if(indexes == null || indexes.size() != size) {
			for(int i = 0;i < size;i++) {
				newIndexes.add(0);
			}
			return newIndexes;
		}
		else {
			for(int i = 0;i < indexes.size();i++){
				newIndexes.add(RandomUtils.nextInt(limits[i]));
			}
			if(histories.contains(newIndexes.hashCode())){
				newIndexes.clear();
				for(int i = 0;i < indexes.size();i++){
					newIndexes.add(RandomUtils.nextInt(limits[i]));
				}
				count++;
				if(count > maxSize){
					return null;
				}
			}
		}
		return newIndexes;
	}
	
	public static List<Integer> getRandomCombination(List<Integer> indexes,int size,int[] limits
			,List<Integer> histories){
		List<Integer> newIndexes = Lists.newArrayList();
		double maxSize = 1;
		int count = 0;
		for(int i = 0;i < limits.length;i++){
			maxSize *= limits[i];
		}
		if(histories.size() >= maxSize){
			return null;
		}
		if(indexes == null || indexes.size() != size) {
			for(int i = 0;i < size;i++) {
				Integer num = RandomUtils.nextInt(limits[i]);
				while(newIndexes.contains(num)){
					num = RandomUtils.nextInt(limits[i]);
				}
				newIndexes.add(num);
			}
			return newIndexes;
		}
		else {
			for(int i = 0;i < indexes.size();i++){
				Integer num = RandomUtils.nextInt(limits[i]);
				while(newIndexes.contains(num)){
					num = RandomUtils.nextInt(limits[i]);
				}
				newIndexes.add(num);
			}
			if(histories.contains(newIndexes.hashCode())){
				newIndexes.clear();
				for(int i = 0;i < indexes.size();i++){
					Integer num = RandomUtils.nextInt(limits[i]);
					while(newIndexes.contains(num)){
						num = RandomUtils.nextInt(limits[i]);
					}
					newIndexes.add(num);
				}
				count++;
				if(count > maxSize){
					return null;
				}
			}
		}
		return newIndexes;
	}

	public static List<Integer> findFibonacciNumber(int upperLimit) {
		List<Integer> fibonacciNums = Lists.newArrayList();
		fibonacciNums.add(1);
		fibonacciNums.add(1);
		int currentSize = 2;
		while (fibonacciNums.get(currentSize - 1) < upperLimit) {
			int newNum = fibonacciNums.get(currentSize - 1) + fibonacciNums.get(currentSize - 2);
			if (newNum > upperLimit)
				break;
			fibonacciNums.add(newNum);
			currentSize = fibonacciNums.size();
		}
		return fibonacciNums;
	}
	
	public static boolean isFibonacciNumber(int num) {
		return FIBONACCI_NUMS.contains(new Integer(num));
	}
	
	public static boolean isPrimeRoot(int num,int base) {
		int eulerNum = new Long(EulerFunction.getResult(num)).intValue();
		BigDecimal bd1 = new BigDecimal(base);
		BigDecimal bdp = bd1.pow(eulerNum);
		BigDecimal mod = new BigDecimal(num);
		BigDecimal remainder = bdp.divideAndRemainder(mod)[1];
		return remainder.longValue() == 1 ? true : false;
	}

	public static void main(String[] args) {

		try {
			// System.out.println(Math.pow(6, 1104) % 1105);
			System.out.println(isFibonacciNumber(3));
			System.out.println(isPrimeRoot(135,2));
			System.out.println(Math.pow(2, 72) % 135);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
