package org.aztec.autumn.common.math.equations.strategy.de.range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.AdjustmentStrategy;
import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentResult;
import org.aztec.autumn.common.math.equations.strategy.BaseAdjustmentStrategy;

import com.google.common.collect.Lists;

public class ComplexStrategy extends BaseAdjustmentStrategy implements AdjustmentStrategy{

	public ComplexStrategy() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public AdjustmentResult makeAdjustment(Map<DESolutionConstraintType, DESolutionConstraint> constraints, Long[] solutions,
			List<Long> factors, Map<Integer, AdjustmentResult> searchedHistory, Object... otherParams) {
		// TODO Auto-generated method stub
		init(searchedHistory);
		Long[][] ranges = (Long[][]) constraints.get(DESolutionConstraintType.SOLUTION_RANGE).getParams()[0];
		Long[] simplifiedSolution = solutions;
		if (!isSimplified(solutions, ranges)) {
			SimpleRangeAdjustmentStrategy sras = new SimpleRangeAdjustmentStrategy();
			AdjustmentResult firstResult = sras.makeAdjustment(constraints, solutions, factors, new HashMap<>(),
					otherParams);
			if (firstResult != null) {
				simplifiedSolution = firstResult.getSolution();
			}
		}
		
		return null;
	}
	
	private boolean isSimplified(Long[] solution,Long[][] ranges) {
		for(int i = 0;i < solution.length - 1;i++) {
			if(solution[i] < ranges[i][0] || solution[i] > ranges[i][1]) {
				return false;
			}
		}
		return true;
	}
	

	
	
	private List<Integer> getExculsiveIndexes(Long[] tmpSolutions){
		List<Integer> indexes = Lists.newArrayList();
		/*for(AdjustmentSnapshot snapshot : failHistory) {
			if(snapshot.getNextStep().getMovableStep())
		}*/
		return indexes;
	}
}
