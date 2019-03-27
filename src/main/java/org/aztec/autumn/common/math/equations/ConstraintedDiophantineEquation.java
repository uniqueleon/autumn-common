package org.aztec.autumn.common.math.equations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.impl.de.CongruenceAlgorithm;
import org.aztec.autumn.common.math.equations.impl.de.SimpleAlgorithm;

public class ConstraintedDiophantineEquation {
	
	public final static Map<RunningMode,ConstrainedDEAlgorithm> algorithms = new HashMap<>();
	
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
}
