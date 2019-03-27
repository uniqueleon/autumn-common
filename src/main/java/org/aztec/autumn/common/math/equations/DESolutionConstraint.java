package org.aztec.autumn.common.math.equations;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class DESolutionConstraint {

	private DESolutionConstraintType type;
	private Object[] params;

	public DESolutionConstraintType getType() {
		return type;
	}

	public void setType(DESolutionConstraintType type) {
		this.type = type;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public DESolutionConstraint(DESolutionConstraintType type, Object[] params) {
		super();
		this.type = type;
		this.params = params;
	}

	public static DESolutionConstraint findConstraint(
			DESolutionConstraint[] constraints, DESolutionConstraintType type) {
		DESolutionConstraint desContraint = null;
		for (DESolutionConstraint desCont : constraints) {
			if (desCont.getType().equals(type)) {
				return desCont;
			}
		}
		return desContraint;
	}

	public static enum DESolutionConstraintType {
		ORDERED, SOLUTION_RANGE, POSITIVE, NO_DUPLICATE, NON_ZERO, NOT_ORDINARY,
		SOLUTION_MAX_NUM, FIXED_SOLUTION, EXCLUSIVE_SOLUTION, SOLUTION_SIZE
		,SOLUTION_EXCLUDE;
	}

	public static Map<DESolutionConstraintType, DESolutionConstraint> toMap(
			DESolutionConstraint[] contraints) {
		Map<DESolutionConstraintType, DESolutionConstraint> retMap = Maps
				.newHashMap();
		for (DESolutionConstraint cont : contraints) {
			retMap.put(cont.getType(), cont);
		}
		return retMap;
	}

	public static Map<DESolutionConstraintType, DESolutionConstraint> addSolutionSize(
			Map<DESolutionConstraintType, DESolutionConstraint> constraints,
			int size) {
		if (constraints == null) {
			constraints = Maps.newHashMap();
		}
		constraints.put(DESolutionConstraintType.SOLUTION_SIZE,
				new DESolutionConstraint(
						DESolutionConstraintType.SOLUTION_SIZE,
						new Object[] { size }));
		return constraints;
	}

	public static Map<DESolutionConstraintType, DESolutionConstraint> addRangeConstraint(
			Map<DESolutionConstraintType, DESolutionConstraint> constraints,
			Long[][] ranges) {
		if (constraints == null) {
			constraints = Maps.newHashMap();
		}
		constraints.put(DESolutionConstraintType.SOLUTION_RANGE,
				new DESolutionConstraint(
						DESolutionConstraintType.SOLUTION_RANGE,
						new Object[] { ranges }));
		return constraints;
	}

	public static DESolutionConstraint newOrderedConstraint() {
		return new DESolutionConstraint(DESolutionConstraintType.ORDERED,
				new Object[] {});
	}

	public static DESolutionConstraint newSolutionRangeConstraint(
			Long[][] ranges) {
		return new DESolutionConstraint(
				DESolutionConstraintType.SOLUTION_RANGE,
				new Object[] { ranges });
	}

	public static DESolutionConstraint newSolutionLimitConstraint(Integer limit) {
		return new DESolutionConstraint(
				DESolutionConstraintType.SOLUTION_MAX_NUM,
				new Object[] { limit });
	}

	public static DESolutionConstraint newExclusiveConstraint(
			List<List<Long>> exclusiveSolutions) {
		return new DESolutionConstraint(
				DESolutionConstraintType.EXCLUSIVE_SOLUTION,
				new Object[] { exclusiveSolutions });
	}
}