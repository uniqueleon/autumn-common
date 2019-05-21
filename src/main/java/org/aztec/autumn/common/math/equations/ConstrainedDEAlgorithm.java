package org.aztec.autumn.common.math.equations;

import java.util.List;

/**
 * ��Լ����������
 * 
 * @author 10064513
 *
 */
public interface ConstrainedDEAlgorithm {
	
	public static interface RUNNING_PARAMETERS{
		public static final String CUSTORMERIZED_VALIDATOR = "customerValidator";
		public static final String NEIGHBORHOOD = "neighborhoods";
		public static final String DEFAULT_TIME_OUT = "timeout";
	}
	
	public void setContext(String key,Object object);

	public List<Long> findSolution(Long[] factors, Long result,
			Long[][] ranges,List<Long> baseResult,List<List<Long>> histories);
	
	public List<List<Long>> findSolutions(Long[] factors, Long result,
			Long[][] ranges,List<Long> baseReuslt,int solutionNum);
}
