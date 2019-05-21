package org.aztec.autumn.common.math.equations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.impl.de.CongruenceAlgorithm;
import org.aztec.autumn.common.math.equations.impl.de.SimpleAlgorithm;

public class ConstraintedDiophantineEquation {
	
	public final static Map<RunningMode,ConstrainedDEAlgorithm> algorithms = new HashMap<>();
	
	public static interface RUNNING_PARAMETERS{
		public static final String CUSTORMERIZED_VALIDATOR = "customerValidator";
		public static final String NEIGHBORHOOD = "neighborhoods";
		public static final String DEFAULT_TIME_OUT = "timeout";
	}
	

	
	static {
		algorithms.put(RunningMode.SIMPLE, new SimpleAlgorithm());
		//algorithms.put(RunningMode.LARGE_SCALE, new CongruenceAlgorithm2());
		//algorithms.put(RunningMode.LARGE_SCALE, new CongruenceAlgorithm());
		algorithms.put(RunningMode.CONGRUENCE, new CongruenceAlgorithm());
		//algorithms.put(RunningMode.BACK_UP, new CongruenceAlgorithm2());
		//algorithms.put(RunningMode.LARGE_SCALE, new Algorithm_4());
	}

	public static enum RunningMode {
		// 穷举法
		SIMPLE("simple"),
		// 利用同余性质来求解
		CONGRUENCE("congruence"),
		BACK_UP("backup");;
		
		String name;

		private RunningMode(String name) {
			this.name = name;
		}
		
	}

	public static DiophantineResult getSolutions(Long[] factors, Long result, Long[][] ranges, RunningMode runMode) {
		DiophantineResult dr = DiophantineEquation.getSolution(factors, result);
		List<Long> possibleSolution = null;
		ConstrainedDEAlgorithm algorithm = algorithms.get(runMode);
		if (algorithm != null) {
			possibleSolution = algorithm.findSolution(factors, result, ranges,dr.getBaseResults(), new ArrayList<List<Long>>());
			if(possibleSolution != null) {
				dr.getSolutions().add(possibleSolution);
			}
		}
		return dr;
	}
	
	public static DiophantineResult getSolutions(Long[] factors, Long result, Long[][] ranges, RunningMode runMode,int solutionNum) {
		DiophantineResult dr = DiophantineEquation.getSolution(factors, result);
		ConstrainedDEAlgorithm algorithm = algorithms.get(runMode);
		dr.getSolutions().addAll(algorithm.findSolutions(factors, result, ranges,dr.getBaseResults(), solutionNum));
		return dr;
	}

	public static List<Long> findSolution(Long[] factors, Long result, Long[][] ranges, Long valve) {
		return findSolution(factors, result, ranges,RunningMode.CONGRUENCE);
		/*Long testValve = 1l;
		for (int i = 0; i < ranges.length; i++) {
			testValve *= (ranges[i][1] - ranges[i][0]) + 1;
		}
		return findSolution(factors, result, ranges, testValve > valve ? RunningMode.CONGRUENCE : RunningMode.SIMPLE);*/
	}

	public static List<Long> findSolution(Long[] factors, Long result, Long[][] ranges, RunningMode runMode) {
		List<Long> possibleSolution = null;
		ConstrainedDEAlgorithm algorithm = algorithms.get(runMode);
		if (algorithm != null) {
			possibleSolution = algorithm.findSolution(factors, result, ranges,new ArrayList<Long>(), new ArrayList<List<Long>>());
		}
		return possibleSolution;
	}
	
	public static List<List<Long>> findSolution(Long[] factors, Long result, Long[][] ranges, RunningMode runMode,int size) {
		List<List<Long>> possibleSolution = null;
		ConstrainedDEAlgorithm algorithm = algorithms.get(runMode);
		if (algorithm != null) {
			possibleSolution = algorithm.findSolutions(factors, result, ranges,new ArrayList<Long>(), size);
		}
		return possibleSolution;
	}
	

	public static List<List<Long>> findSolution(CalculatingParameter calParams) {
		List<List<Long>> possibleSolution = null;
		ConstrainedDEAlgorithm algorithm = algorithms.get(calParams.getRunMode());
		
		if (algorithm != null) {
			if(calParams.getValidatorIndexes() != null){
				algorithm.setContext(RUNNING_PARAMETERS.CUSTORMERIZED_VALIDATOR, calParams.getValidatorIndexes());
				algorithm.setContext(RUNNING_PARAMETERS.NEIGHBORHOOD, calParams.getGroupData());
				algorithm.setContext(RUNNING_PARAMETERS.DEFAULT_TIME_OUT, calParams.getTimeout());
				
			}
			possibleSolution = algorithm.findSolutions(calParams.getFactors(), calParams.getResult(), 
					calParams.getRanges(),new ArrayList<Long>(), calParams.getSize());
		}
		return possibleSolution;
	}
	
	

	public static class CalculatingParameter{
		private Long[] factors;
		private Long result;
		private Long[][] ranges;
		private RunningMode runMode;
		private int size;
		private int[] validatorIndexes;
		private int[] groupData;
		private Long timeout = 10l;
		//private Long timeout = null;
		public Long[] getFactors() {
			return factors;
		}
		public void setFactors(Long[] factors) {
			this.factors = factors;
		}
		public Long getResult() {
			return result;
		}
		public void setResult(Long result) {
			this.result = result;
		}
		public Long[][] getRanges() {
			return ranges;
		}
		public void setRanges(Long[][] ranges) {
			this.ranges = ranges;
		}
		public RunningMode getRunMode() {
			return runMode;
		}
		public void setRunMode(RunningMode runMode) {
			this.runMode = runMode;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public int[] getValidatorIndexes() {
			return validatorIndexes;
		}
		public int[] getGroupData() {
			return groupData;
		}
		public void setGroupData(int[] groupData) {
			this.groupData = groupData;
		}
		public void setValidatorIndexes(int[] validatorIndexes) {
			this.validatorIndexes = validatorIndexes;
		}
		
		public Long getTimeout() {
			return timeout;
		}
		public void setTimeout(Long timeout) {
			this.timeout = timeout;
		}
		public CalculatingParameter(Long[] factors, Long result, Long[][] ranges,
				RunningMode runMode,int solutionSize) {
			super();
			this.factors = factors;
			this.result = result;
			this.ranges = ranges;
			this.runMode = runMode;
			this.size = solutionSize;
		}
		
		
	}
}
