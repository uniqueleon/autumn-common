package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

public interface ScoreEvaluator {

	public double getScore();
	
	public static class EvaluateParameter{
		private Long[] factors;
		private Long[] heights;
		private Long result;
		private List<Long> solution;
		
	}
}
