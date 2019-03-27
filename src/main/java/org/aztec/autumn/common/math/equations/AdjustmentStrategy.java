package org.aztec.autumn.common.math.equations;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentResult;

public interface AdjustmentStrategy {

	public AdjustmentResult makeAdjustment(Map<DESolutionConstraintType, DESolutionConstraint> constraints,Long[] solutions,
			List<Long> factors,Map<Integer,AdjustmentResult> searchedHistory,Object... otherParams);
}
