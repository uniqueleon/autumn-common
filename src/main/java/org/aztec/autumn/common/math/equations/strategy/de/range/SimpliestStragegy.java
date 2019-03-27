package org.aztec.autumn.common.math.equations.strategy.de.range;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentResult;
import org.aztec.autumn.common.math.equations.strategy.BaseAdjustmentStrategy;

public class SimpliestStragegy extends BaseAdjustmentStrategy{

	public SimpliestStragegy() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public AdjustmentResult makeAdjustment(Map<DESolutionConstraintType, DESolutionConstraint> constraints,
			Long[] solutions, List<Long> factors, Map<Integer, AdjustmentResult> searchedHistory,
			Object... otherParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
