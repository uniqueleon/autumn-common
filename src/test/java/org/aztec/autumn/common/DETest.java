package org.aztec.autumn.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.math.congruence.CongruenceDiophantineEquation;
import org.aztec.autumn.common.math.congruence.CongruenceEquation;
import org.aztec.autumn.common.math.congruence.CongruenceEquationResult;
import org.aztec.autumn.common.math.equations.ConstraintedDiophantineEquation;
import org.aztec.autumn.common.math.equations.ConstraintedDiophantineEquation.RunningMode;
import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.equations.zs._3DEquation;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class DETest {

	private static AtomicLong maxSolutionCount = new AtomicLong(0l);
	
	private static Map<String,Object> testContext = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		//test11();
		
		//test12();
		// 
		// oricode : 24 bit
		// code : [result 65535 16 bit][ index 256 8bit] = 24 bit 
		// code : 1111111111111111
		// code2 : [head 4bit][rare 4bit] = 8bit
		//test13();
		//test14();
		
		
		//test5(32,10000l,1l,1,1);
		//System.out.println();
		//test5(1000,1000l,100l,1,3);
		test15();
	}
	
	public static void test15() { 
		
		long base = -5;
		// 2,3
		long result = 1 + 35 * base;
		long totalResult = 4 * 56 + 6 * 50 + 1 * 35;
		while(result <= totalResult) {

			DiophantineResult dr = ConstraintedDiophantineEquation.getSolutions(new Long[] {56l,50l,35l}, result,
					new Long[][] {{-4l,4l},{-6l,6l},{-1l,1l}},RunningMode.CONGRUENCE,100 );
			if(dr.getHasSolution()) {
				System.out.println(dr);
			}
			base ++;
			result = 1 + 35 * base;
		}
		/*long result = 106;
		DiophantineResult dr = ConstraintedDiophantineEquation.getSolutions(new Long[] {56l,50l,35l}, result,
				new Long[][] {{-4l,4l},{-6l,6l},{-1l,1l}},RunningMode.CONGRUENCE,100 );
		if(dr.getHasSolution()) {
			System.out.println(dr);
		}*/
	}
	
	public static void test14() {
		/*Long[] factors = new Long[] {45l,75l,170l,55l,95l,225l,48l,90l,225l,50l,90l,225l};
		Long result = 280l;
		Long[][] ranges = new Long[][] {{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l}};
		DiophantineResult dr = DiophantineEquation.getSolution(factors, result, ranges, 10);
		System.out.println(dr);*/
		Long[] factors = new Long[] {45l,75l,170l,225l,225l,225l};
		Long result = 270l;
		Long[][] ranges = new Long[][] {{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l}};
		DiophantineResult dr = DiophantineEquation.getSolution(factors, result, ranges, 10);
		System.out.println(dr);
	}
	
	//@Test
	public void test13Error() {

		//1 * 11 + 3 * 3 + 5 * 8 + 6 * 6 + 8 * 9 + 10 * 8 + 12 * 2 + 14 * 6 + 15 * 8 + 16 * 1 = 492
		//1 * 11 + 3 * 3 + 5 * 8 + 6 * 6 + 8 * 9 + 10 * 8 + 12 * 2 + 14 * 6 + 15 * 8 + 16 * 1 =492
		Long[] factors = new Long[] {11l, 3l, 8l, 6l , 9l , 8l , 2l, 6l, 8l, 1l};
		List<Long> oriSolutions = Lists.newArrayList(new Long[] {1l,3l,5l,6l,8l,10l,12l,14l,15l,16l});
		Long multiple = 1l;
		for(int i = 0;i < factors.length;i++) {
			multiple *= factors[i];
		}
		System.out.println("multi:" + multiple);
		Long result = 492l;
		Long[][] ranges = new Long[][] {{0l,16l},{0l,16l},{0l,16l},{0l,16l},{0l,16l},{0l,16l},{0l,16l},{0l,16l},{0l,16l},{0l,16l}};
		DiophantineResult dr = DiophantineEquation.getSolution(factors, result, ranges, 1024);
		System.out.println(dr);
		for(List<Long> testSolution : dr.getSolutions()) {
			if(testSolution.equals(oriSolutions)) {
				System.out.println("founded!");
				break;
			}
		}
	}
	
	@Test
	public  void test13() {
		for(int i = 0;i < 10;i++) {
			System.out.println("doing " + i + " th test!");
			test12(24,16,24,128,65535,128);
		}
	}

	//@Test
	public void test12() {

		DiophantineResult dr = DiophantineEquation.getSolution(new Long[]{2l,1l,3l}, 5l, new Long[][]{{0l,6l},{0l,3l},{0l,5l}}, 10);
		System.out.println(dr);
		System.out.println(dr.getSolutions().size());
	}
	
	public void test12(int factorSize,int factorLimit,int posLimit,int solutionCount,int resultLimit,int indexLimit) {
		List<Long> originSolution = Lists.newArrayList();
		int posGap = posLimit / factorSize;
		if(posGap == 1) {
			posGap = 2;
		}
		Long curSolution = null; 
		Long[] factors = new Long[factorSize];
		Long result = 0l;
		Long[][] ranges = new Long[factorSize][2];
		StringBuilder builder = new StringBuilder();
		Long factorSum = null;
		for(int i = 0;i < factorSize;i ++) {
			Long tempNum = RandomUtils.nextLong() % factorLimit;
			while(tempNum == 0) {
				tempNum = RandomUtils.nextLong() % factorLimit;
			}
			if(factorSum == null) {
				factorSum = tempNum;
			}
			else {
				factorSum += i * tempNum;
			}
			factors[i] = new Long(factorSum);
			factors[i] = new Long(i + 1);
			if(curSolution == null) {
				curSolution = RandomUtils.nextLong() % posGap;
			}
			else {
				curSolution += RandomUtils.nextLong() % posGap + 1;
			}
			originSolution.add(new Long(curSolution));
			result += curSolution * factors[i];
			if(i == 0 || i == factors.length - 1) {
				ranges[i] = new Long[] {curSolution,curSolution};
			}
			else {
				ranges[i] = new Long[] {0l,new Long(posLimit)};
			}
			if(i != 0)
				builder.append(" + ");
			builder.append( curSolution + " * " + factors[i]);
		}
		builder.append( " = " + result);
		System.out.println("finding solution................for  " + builder.toString());
		DiophantineResult dr = DiophantineEquation.getSolution(factors, result, ranges, solutionCount);
		boolean find = false;
		int findIndex = -1;
		System.out.println("origin result :[" + originSolution + "]");
		for(int i = 0;i < dr.getSolutions().size();i++) {
			List<Long> testSolution = dr.getSolutions().get(i);
			if(testSolution.equals(originSolution)) {
				System.out.println("find target solution index[" + i + "]");
				find = true;
				findIndex = i;
				break;
			}
		}
		
		if(!find) {
			System.err.println(" equation" + builder.toString() + " not found its origin solution!");
			System.exit( -1);
		}
	}
	

	public void test11(){
		/*DiophantineResult dr = DiophantineEquation.getSolution(new Long[]{2l,1l,3l}, 5l, new Long[][]{{0l,6l},{0l,3l},{0l,5l}}, 10);
		System.out.println(dr);*/
		DiophantineResult dr = DiophantineEquation.getSolution(new Long[]{4l,5l,13l,3l}, 1 * 4l + 3 * 5l + 7 * 13l + 12 * 3l, new Long[][]{{0l,16l},{0l,16l},{0l,16l},{0l,16l}}, 100);
		System.out.println(dr);
		System.out.println(dr.getSolutions().size());
		System.out.println(7 * 21 + 4 * 11 + 32 * 31);
	}
	
	public void test10() {
		Long factor = 88l;
		Long modular = 4l;
		Long cursor = 0l;
		Long[] ranges = new Long[] { 0l, 30l };
		Long solutionCount = 0l;
		ranges = new Long[] {15l, 101l};
		CongruenceEquationResult cer = CongruenceEquation.solve(factor, modular, 0l);
		if (cer.isSolved()) {
			List<Long> solution = cer.findSolutions(ranges);
			if(solution.size() > 0){
				System.out.println(cer);
				System.out.println(solution);
				solutionCount += solution.size();
			}
		}
		/*while (cursor < modular) {
			CongruenceEquationResult cer = CongruenceEquation.solve(factor, modular, cursor);
			if (cer.isSolved()) {
				List<Long> solution = cer.findSolutions(ranges);
				if(solution.size() > 0){
					System.out.println(cer);
					System.out.println(solution);
					solutionCount += solution.size();
				}
			}
			cursor++;
		}*/
		System.out.println("count:" + solutionCount);
	}
	
	
	public void testQuick(Long result,Long[] factors,Long[][] ranges) {
		long curTime = System.currentTimeMillis();
		DiophantineResult dr = ConstraintedDiophantineEquation.getSolutions(factors, result, 
				ranges,RunningMode.CONGRUENCE);
		dr.validate();
		long eclipseTime = System.currentTimeMillis() - curTime;
		if(eclipseTime > 1000 || dr.getSolutions().size() == 0) {
			String writeString = String.format("Time out! %1$s = %2$d , use time = %3$d ms!\n ", new Object[] {Arrays.toString(factors),result,eclipseTime});
			//writ
			writeData(writeString);
		}
		System.out.println(dr);
	}
	
	public void testNormal(Long result,Long[] factors,Long[][] ranges) {
		DiophantineResult dr = ConstraintedDiophantineEquation.getSolutions(factors, result, 
				ranges,RunningMode.SIMPLE);
		dr.validate();
		System.out.println(dr);
	}
	
	public void test8() {

		Long[] factors = new Long[] {5l,8l,11l};
		Long[][] ranges = new Long[][] {{0l,3l},{0l,3l},{0l,3l}};
		testQuick(45l, factors, ranges);
		
	}
	
	public void test7() {
		//Long result = 955 * 98l + 182 * 97l + 703 * 76l + 571l * 71l + 317 * 87l;
		//Long[] factors = new Long[] {955l,182l,703l,571l,317l};
		//Long[][] ranges = new Long[][] {{0l,100l},{0l,100l},{0l,100l},{0l,100l},{0l,100l}};
		Long result = 25642l;
		Long[] factors = new Long[] {562l,943l,782l,684l,973l,660l};
		Long[][] ranges = new Long[][] {{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l}};
		Long curTime = System.currentTimeMillis();
		testQuick(result, factors, ranges);
		System.out.println("time use[Quick]:" + (System.currentTimeMillis() - curTime));
		curTime = System.currentTimeMillis();
		testNormal(result, factors, ranges);
		System.out.println("time use[normal]:" + (System.currentTimeMillis() - curTime));
	}
	
	
	private void test3(int testTime) {
		Random random = new Random();
		for(int i = 0;i < testTime;i++) {
			test3(random);
		}
	}
	

	private void test4() {
		List<Long> testEfficient = Lists.newArrayList();
		
		testEfficient.add(4l);
		testEfficient.add(8l);
		testEfficient.add(10l);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>TEST BEGIN[" + testEfficient + "]<<<<<<<<<<<<<");
		_3DEquation _3de = new _3DEquation();
		_3de.init(testEfficient);
		List<Long[]> solutions = _3de.getAcceptableSolution( new Long[][] {{-4l,0l},{-1l,0l},{-1l,0l}}, Lists.newArrayList(),1);
		
		for(Long[] solution : solutions) {
			System.out.println("solution:" + Arrays.toString(solution));
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>TEST END<<<<<<<<<<<<<");
		_3de.validate(testEfficient, solutions);
	}
	
	private void test3(Random random) {
		List<Long> testEfficient = Lists.newArrayList();
		
		testEfficient.add(Math.abs(random.nextLong() % 100) + 1);
		testEfficient.add(Math.abs(random.nextLong() % 100) + 1);
		testEfficient.add(Math.abs(random.nextLong() % 100) + 1);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>TEST BEGIN[" + testEfficient + "]<<<<<<<<<<<<<");
		_3DEquation _3de = new _3DEquation();
		_3de.init(testEfficient);
		List<Long[]> solutions = _3de.getAcceptableSolution( new Long[][] {{0l,100l},{0l,100l},{-200l,0l}}, Lists.newArrayList(),1);
		
		for(Long[] solution : solutions) {
			System.out.println("solution:" + Arrays.toString(solution));
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>TEST END<<<<<<<<<<<<<");
		_3de.validate(testEfficient, solutions);
	}
	
	private void test2() {
	
		CongruenceDiophantineEquation cde = new CongruenceDiophantineEquation(4l, 0l, Lists.newArrayList(new Long[] {3l,5l}),
				new Long[][] {{0l,3l},{0l,3l}}, Lists.newArrayList(), 1000);
		cde.solve();
		List<List<Long>> solutions = cde.getSolutions();
		for(List<Long> solution : solutions) {
			System.out.println(solution);
		}
		
	}
	
	private void test1() {
		//Map<DESolutionConstraintType,DESolutionConstraint> constraints  = getConstrantsMap();
		//8X+24Y+18Z = 210
		//Long[] factors = new Long[] {5l,9l,10l};
		

		//Long[] factors = new Long[] {2l,3l,5l};
		
		// 2-2 ,3-0,5-1,h=1
		//tmpResult = 3l;
		// 0-0 ,3-0,4-1,h=2
		//tmpResult = 7l;
		// 0,3-1,4-1,h = 1
		long curTime = System.currentTimeMillis();
		//Long[] factors = new Long[] {4l,4l,3l,5l,8l,11l,10l,8l,10l,7l,5l,6l,7l,3l};
		//Long tmpResult = 67l;
		Map<DESolutionConstraintType,DESolutionConstraint> constraints = new HashMap<>();
		//constraints.put(DESolutionConstraintType.SOLUTION_RANGE, DESolutionConstraint.newSolutionRangeConstraint(new Long[][] {{0l,3l},{0l,3l},{0l,3l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l}}));
		//testSingle(constraints,new Long[] {4l,5l,39l},62l);
		
		//testSingle(constraints, new Long[] {4l,4l,3l,5l,8l,11l,10l,8l,10l,7l,5l,6l,7l}, 100l);
		
		constraints.put(DESolutionConstraintType.SOLUTION_RANGE, DESolutionConstraint.newSolutionRangeConstraint(new Long[][] {{0l,3l},{0l,3l},{0l,3l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l}}));
		//testSingle(constraints,new Long[] {4l,5l,39l},62l);
				
		testSingle(constraints, new Long[] {4l,4l,3l,5l,8l,11l,10l,8l,12l,13l}, 100l);
				
		//3, 10, 2, 1, 1, 1, 1, 1, 0, 0 , 0, 0 ,0
		//3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1
		//  { 4 * 3, 4 * 2, 3 * 1, 5 * 1, 8 * 1,11 * 1, 10 * 1, 8 * 1, 10 * 1,7 * 1,5 * 1,6 * 1, 7 * 1} = 100
		//testSingle(constraints, new Long[] {4l,4l,3l}, 91l);
		//Map<DESolutionConstraintType,DESolutionConstraint> constraints = new HashMap<>();
		//constraints.put(DESolutionConstraintType.SOLUTION_RANGE, DESolutionConstraint.newSolutionRangeConstraint(new Long[][] {{0l,4l},{0l,4l},{0l,4l}}));
		//testSingle(constraints,new Long[] {4l,5l,39l},62l);
		long usedTime = System.currentTimeMillis() - curTime;
		System.out.println("useTime:"  +  (usedTime));
		//System.out.println(GreatestCommonDivisor.getGCD(new Long[] {8l,106l}));
		System.out.println(maxSolutionCount.get());
	}
	
	private static Map<DESolutionConstraintType,DESolutionConstraint>  getConstrantsMap() {
		Map<DESolutionConstraintType,DESolutionConstraint> constraints = Maps.newHashMap();
		
		//Long[][] range = new Long[][] {{0l,3l},{0l,3l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l},{0l,1l}};
		
		//Long[][] range = new Long[][] {{0l,6l},{0l,3l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l},{0l,5l}};
		Long[][] range = new Long[][] {{0l,10l},{0l,10l},{0l,10l}};
		
		//Long[][] range = new Long[][] {{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l}};
		//Long[][] range = new Long[][] {{0l,6l},{0l,3l},{0l,5l}};
		
		//constraints.put(DESolutionConstraintType.ORDERED, new DESolutionConstraint(DESolutionConstraintType.ORDERED, new Object[] {}));
		//Long[][] range = new Long[][] {{0l,2l},{0l,4l},{0l,3l}};
		//Long[][] range = new Long[][] {{0l,10l},{0l,10l}};
		constraints.put(DESolutionConstraintType.SOLUTION_RANGE, new DESolutionConstraint(DESolutionConstraintType.SOLUTION_RANGE, new Object[] {range}));
		constraints.put(DESolutionConstraintType.SOLUTION_MAX_NUM, new DESolutionConstraint(DESolutionConstraintType.SOLUTION_MAX_NUM, new Object[] {1000}));
		
		//Long[][] fixedResult = new Long[][] {{2l},{1l,4l},{1l,3l}};
		//constraints.put(DESolutionConstraintType.FIXED_SOLUTION, new DESolutionConstraint(DESolutionConstraintType.FIXED_SOLUTION, new Object[] {fixedResult}));
		return constraints;
	}
	
	public void testTwoFactorSingle(int i,int j) {

		//testMultiple(constraints, new Long[] {16l,16l}, 2, 17,100);
	}
	
	public void testMultiple(Map<DESolutionConstraintType,DESolutionConstraint> constraints,
			Long[] factors,long remainder,int modular,int bitCount) {
		
		int solutionCount = 0;
		long realRange = 0l;
		for(int i = 0;i < bitCount;i++) {
			realRange += (modular - 1) * (bitCount - i);
		}
		//System.out.println(realRange);
		int testTime = (int) (realRange / modular);
		for(int i = 0;i < testTime;i++) {
			long testResult = i * modular + remainder;
			DiophantineResult dr = DiophantineEquation.getSolution(factors,testResult
					);
			if(dr.getHasSolution() && dr.getSolutions().size() > 0) {
				//System.out.println(dr.getResult());
				//System.out.println(dr.getSolutions());
				
				solutionCount += dr.getSolutions().size();
			}
			
		}
		System.out.println("final solution count:" + solutionCount);
		if(maxSolutionCount.get() < solutionCount) {
			maxSolutionCount.set(solutionCount);
		}
		if(solutionCount > 15) {
			System.err.print(factors[0] + "-" + factors[1]);
			System.err.println("what the suck!");
			System.exit(-1);
		}
	}
	
	public static void testSingle(Map<DESolutionConstraintType,DESolutionConstraint> constraints,Long[] factors,long testResult) {
		DiophantineResult dr = DiophantineEquation.getSolution(factors,testResult
				);
		//dr.validate();
		dr.constrain(constraints);
		dr.validate();
		System.out.println(dr);
		int solutionCount = 0;
		if(dr.getSolutions().size() > 0) {
			solutionCount += dr.getSolutions().size();
		}
		System.out.println("final solution count:" + solutionCount);
	}
	
	public static void testConstraint(Long[] factors,Long result,Long[][] ranges) {
		
		System.out.println(">>>>>>>>>>>>USING CONGRUENCE SELECTOR ALGORITHM<<<<<<<<<<<<<");
		long curTime = System.currentTimeMillis();
		DiophantineResult dr = ConstraintedDiophantineEquation.getSolutions(factors, result, 
				ranges,RunningMode.CONGRUENCE);
		dr.validate();
		System.out.println(dr);
		if(dr.getSolutions().size() == 0) {
			System.err.println("factors[ " + Arrays.toString(factors) + " ] = " + result + " is not solved!");
			System.exit(-1);
		}
		long eclipseTime = (System.currentTimeMillis() - curTime);
		System.out.println("use time :" + eclipseTime);
		
		System.out.println(">>>>>>>>>>>>USING CONGRUENCE SELECTOR ALGORITHM<<<<<<<<<<<<<");
	}
	
	public static void writeData(String writeString) {
		try {
			File dataFile = new File("test/slowFactors.txt");
			RandomAccessFile raf = new RandomAccessFile(new File("test/slowFactors.txt"), "rw");
			FileChannel fc = raf.getChannel();
			fc.position(raf.length());
			ByteBuffer writeBuff = ByteBuffer.allocate(writeString.length());
			writeBuff.put(writeString.getBytes());
			writeBuff.flip();
			fc.write(writeBuff);
			fc.close();
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testNormal(Long[] factors,Long result,Long[][] ranges) {

		long curTime = System.currentTimeMillis();
		DiophantineResult dr = ConstraintedDiophantineEquation.getSolutions(factors, result, 
				ranges,RunningMode.SIMPLE);
		dr.validate();
		System.out.println("normal :" + (System.currentTimeMillis() - curTime));
		System.out.println(dr);
	}
	
	
	public static void test5(int testFactorNum,Long factorRange,Long numberLimit,int testTime,int testType) {
		long curTime = System.currentTimeMillis();
		
		for(int i = 0;i < testTime;i++) {
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>RUNGING " + i + "th test<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			Map<DESolutionConstraintType, DESolutionConstraint> constraints = new HashMap<>();
			Random random = new Random();
			Long[][] ranges = new Long[testFactorNum][2];
			Long[] coefficients = new Long[testFactorNum];
			
			Long result = 0l;
			Long[] originSolutions = new Long[testFactorNum];
			Long[] testFactors = new Long[testFactorNum];
			for(int j = 0;j < testFactorNum;j++) {
				Long testNum = ( Math.abs(random.nextLong()) % numberLimit);
				if(numberLimit == 1) {
					testNum = ( Math.abs(random.nextLong()) % 2);
				}
				coefficients[j] = ( Math.abs(random.nextLong()) % factorRange ) + 1;
				result += testNum * coefficients[j];
				
				ranges[j][0] = 0l;
				ranges[j][1] = numberLimit;
				originSolutions[j] = testNum;
			}
			constraints.put(DESolutionConstraintType.SOLUTION_RANGE,
					DESolutionConstraint.newSolutionRangeConstraint(ranges));
			
			StringBuilder resultBuilder = new StringBuilder();
			resultBuilder.append("########TEST SOLUTIONS [");
			for(int k = 0;k < testFactorNum;k++) {
				if(k != 0) {
					resultBuilder.append(" + ");
				}
				resultBuilder.append(originSolutions[k] + " * " + coefficients[k]);
				if(k == testFactorNum - 1) {
					resultBuilder.append(" = " + result);
				}
			}
			resultBuilder.append("]##############\n");
			System.out.println(resultBuilder);
			switch (testType) {
			case 1:
				testSingle(constraints, coefficients, result);
				break;

			case 2:
				testConstraint(coefficients, result, ranges);
				break;

			case 3:
				testNormal(coefficients, result, ranges);
				break;

			default:
				break;
			}
			//
			//
			//

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>RUNGING " + i + "th test<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}

		// 3, 10, 2, 1, 1, 1, 1, 1, 0, 0 , 0, 0 ,0
		// 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1
		// { 4 * 3, 4 * 2, 3 * 1, 5 * 1, 8 * 1,11 * 1, 10 * 1, 8 * 1, 10 * 1,7 * 1,5 *
		// 1,6 * 1, 7 * 1} = 100
		// testSingle(constraints, new Long[] {4l,4l,3l}, 91l);
		// Map<DESolutionConstraintType,DESolutionConstraint> constraints = new
		// HashMap<>();
		// constraints.put(DESolutionConstraintType.SOLUTION_RANGE,
		// DESolutionConstraint.newSolutionRangeConstraint(new Long[][]
		// {{0l,4l},{0l,4l},{0l,4l}}));
		// testSingle(constraints,new Long[] {4l,5l,39l},62l);
		long usedTime = System.currentTimeMillis() - curTime;
		System.out.println("useTime:" + (usedTime));
		// System.out.println(GreatestCommonDivisor.getGCD(new Long[] {8l,106l}));
		System.out.println(maxSolutionCount.get());
	}
	
	
	public void test9() {
		//OS20180730004120
		Long[][] items = new Long[][] {{170l,75l,45l},{225l,95l,55l},{225l,90l,48l},{225l,90l,50l}};
		
		Long[][] ranges = new Long[][] {{0l,1l},{0l,1l},{0l,1l},{0l,1l}};
		// ,3#
		Long[][] boxes = new Long[][] {{530l,365l,180l},{280l,177l},{195l,140l},{350l,270l},{340l,245l}};
		for(int j = 0;j < boxes.length;j++) {
			Long[] box = boxes[j];
			for(int k = 0;k < box.length;k++) {
				Long boxLength = box[k];
				for(int i = 0; i < items.length;i++) {
					Long[] item = items[i];
					for(int l = 0; l < item.length;l++) {
						
					}
				}
			}
		}
	}
}
