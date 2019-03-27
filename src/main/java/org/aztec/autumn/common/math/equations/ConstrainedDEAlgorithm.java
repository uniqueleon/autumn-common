package org.aztec.autumn.common.math.equations;

import java.util.List;

/**
 * 带约束不定方程求解法
 * 
 * @author 10064513
 *
 */
public interface ConstrainedDEAlgorithm {

	public List<Long> findSolution(Long[] factors, Long result,
			Long[][] ranges,List<Long> baseResult,List<List<Long>> histories);
	
	public List<List<Long>> findSolutions(Long[] factors, Long result,
			Long[][] ranges,List<Long> baseResult,int solutionNum);
}
