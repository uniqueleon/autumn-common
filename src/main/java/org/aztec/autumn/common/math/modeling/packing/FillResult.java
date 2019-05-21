package org.aztec.autumn.common.math.modeling.packing;

import java.util.Map;
import java.util.Queue;


public interface FillResult {

	public Integer getTotalUsedCount();
	public Map<String,Long> getUsedCount();
	public Queue<FillStep> getSteps();
}
