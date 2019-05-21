package org.aztec.autumn.common.math.equations.impl.de;

import java.util.List;
import java.util.Map;

public interface CustomerizedValidator {

	public static interface PARAMETER_KEYS {
		public static String NEIGHBORHOOD_GROUP_DATA = "neighborhoodGroup";
	}
	
	public boolean isValid(ValidateParameter validateParam);
	
	public static class ValidateParameter{
		private int index;
		private Long[] factors;
		private Long result;
		private List<Long> found;
		private Long[][] ranges;
		private Long testSolution;
		private Map<String,Object> params;
		private Integer specialIndex;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
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
		public List<Long> getFound() {
			return found;
		}
		public void setFound(List<Long> found) {
			this.found = found;
		}
		public Long[][] getRanges() {
			return ranges;
		}
		public void setRanges(Long[][] ranges) {
			this.ranges = ranges;
		}
		public Long getTestSolution() {
			return testSolution;
		}
		public void setTestSolution(Long testSolution) {
			this.testSolution = testSolution;
		}
		public Map<String, Object> getParams() {
			return params;
		}
		public void setParams(Map<String, Object> params) {
			this.params = params;
		}
		
		public Integer getSpecialIndex() {
			return specialIndex;
		}
		public void setSpecialIndex(Integer specialIndex) {
			this.specialIndex = specialIndex;
		}
		public ValidateParameter(int index, Long[] factors, Long result,
				List<Long> found, Long[][] ranges, Long testSolution,
				Map<String, Object> params,int specialIndex) {
			super();
			this.index = index;
			this.factors = factors;
			this.result = result;
			this.found = found;
			this.ranges = ranges;
			this.testSolution = testSolution;
			this.params = params;
			this.specialIndex = specialIndex;
		}
		
	}
}
