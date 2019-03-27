package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContext.ContextKeys;
import org.aztec.autumn.common.math.modeling.packing.impl.FillParameter;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResult;
import org.aztec.autumn.common.math.modeling.packing.impl.FillUnit;
import org.aztec.autumn.common.utils.MathUtils;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RectangleSurfaceFiller {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RectangleSurfaceFiller.class);

	public FillResult fill(FillParameter fillParam) {

		FillResult result = new FillResult(fillParam.getStartPoint(), fillParam.getFillType(),
				fillParam.getStartPoint().getLocation());
		FillResult bestResult = null;
		while (!result.isFull() && fillParam.getMaxTryTime() > 0) {
			int lastStep = result.getCurrentStep();
			tryFill(result, fillParam);
			int newStep = result.getCurrentStep();
			if (result.isSuccess()) {
				return result;
			} else if (lastStep == newStep) {
				if (bestResult == null) {
					bestResult = result.clone();
				} else {
					if (bestResult.getTotalUsedCount() < result.getTotalUsedCount()) {
						bestResult = result.clone();
					}
				}
				result.backward();
				fillParam.setMaxTryTime(fillParam.getMaxTryTime() - 1);
			}
		}
		return bestResult;
	}

	private List<Integer> findSolvableEquationFactorIndexs(List<Integer> lastPath, int chooseIndex, Long result,
			Long height, List<List<FillUnit>> fillableUnits, int[] limits) {
		int objectSize = fillableUnits.size();
		List<Integer> newPath = MathUtils.getNextPermutation(lastPath, objectSize, limits);
		while (newPath != null && !DiophantineResult.hasSolution(result, calculateGCDs(newPath, fillableUnits))) {
			newPath = MathUtils.getNextPermutation(newPath, objectSize, limits);
		}
		return newPath;

	}

	private GreatestCommonDivisor calculateGCDs(List<Integer> indexes, List<List<FillUnit>> fillableUnits) {
		Long[] factors = new Long[indexes.size()];
		for (int i = 0; i < indexes.size(); i++) {
			factors[i] = fillableUnits.get(i).get(indexes.get(i)).getBase();
		}
		GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(factors);
		return gcd;
	}

	private boolean isSolutionExists(FillUnit startPoint, List<List<FillUnit>> fillObjects) {
		BinPackingContext context = BinPackingContextFactory.getContext();
		List<Long> sortEdges = context.get(ContextKeys.SORT_EDGES);
		Long base = startPoint.getBase();
		if (base < sortEdges.get(0)) {
			return false;
		}
		Long lowestUnit = Long.MAX_VALUE;
		for (int i = 0; i < fillObjects.size(); i++) {
			for (FillUnit unit : fillObjects.get(i)) {
				if (unit.getBase() < lowestUnit) {
					lowestUnit = unit.getBase();
				}
			}
		}
		if (base < lowestUnit) {
			return false;
		}
		return true;
	}

	private List<List<FillUnit>> getFillableUnits(FillParameter fillParam, Long[][] ranges, FillUnit startPoint) {

		Long base = startPoint.getBase();
		Long height = startPoint.getHeight();
		List<List<FillUnit>> fillableUnits = Lists.newArrayList();
		List<List<FillUnit>> allUnits = fillParam.getItemFillUnits();
		for (int i = 0; i < allUnits.size(); i++) {
			List<FillUnit> unit = allUnits.get(i);
			Long[] range = ranges[i];
			boolean isSolutionExists = range[1] > range[0] ? true : false;
			Double[][] surfacedData = ((Item) unit.get(0).getBelongObject()).getChoosedSurfaceData();
			Long shortOne = new Double(Math.min(surfacedData[0][0], surfacedData[0][1])).longValue();
			Long longOne = new Double(Math.max(surfacedData[0][0], surfacedData[0][1])).longValue();
			Long[] compareArr1 = new Long[] { shortOne, longOne };
			Long shortOne1 = new Double(Math.min(base, height)).longValue();
			Long longOne1 = new Double(Math.max(base, height)).longValue();
			Long[] compareArr2 = new Long[] { shortOne1, longOne1 };
			if (isSolutionExists && (compareArr2[1] >= compareArr1[1] && compareArr2[0] >= compareArr1[0])) {
				fillableUnits.add(unit);
			}
		}
		return fillableUnits;
	}

	private Long[][] recalculateRange(FillParameter fillParam, FillResult tmpFillResult,
			List<List<FillUnit>> fillableUnits) {
		Long[][] ranges = new Long[fillableUnits.size()][2];
		for (int i = 0; i < fillableUnits.size(); i++) {
			FillUnit fillUnit = fillableUnits.get(i).get(0);
			ranges[i][0] = 0l;
			Item item = (Item) fillUnit.getBelongObject();
			Long objNum = item.getNumber();
			Long itemCount = tmpFillResult.getUsedCount().get(item.getId());
			ranges[i][1] = objNum - (itemCount == null ? 0 : itemCount);
		}
		return ranges;
	}

	private Long[][] getPossibleRanges(FillResult tmpFillResult, FillParameter fillParam) {

		List<List<FillUnit>> fillObjects = fillParam.getItemFillUnits();
		Long[][] ranges = new Long[fillObjects.size()][2];
		for (int i = 0; i < ranges.length; i++) {
			ranges[i][0] = 0l;
			Item item = fillParam.getItems().get(i);
			Long objNum = fillParam.getItems().get(i).getNumber();
			Long itemCount = tmpFillResult.getUsedCount().get(item.getId());
			ranges[i][1] = objNum - (itemCount == null ? 0 : itemCount);

		}
		return ranges;
	}

	private boolean isSolvable(FillUnit startPoint, List<Integer> factorIndex, List<List<FillUnit>> fillableUnit) {

		for (int i = 0; i < factorIndex.size(); i++) {
			FillUnit targetUnit = fillableUnit.get(i).get(factorIndex.get(i));
			if (targetUnit.getHeight() > startPoint.getHeight()) {
				return false;
			}
		}

		return true;
	}

	private FillResult tryFill(FillResult result, FillParameter fillParam) {

		if (result.isFull() || result.isSuccess()) {
			return result;
		}
		Long[][] ranges = getPossibleRanges(result, fillParam);

		List<FillUnit> fillTargets = Lists.newArrayList();
		fillTargets.addAll(result.getUnfilledArea());
		for (int k = 0; k < fillTargets.size(); k++) {
			FillUnit startPoint = fillTargets.get(k);
			Long containerBase = startPoint.getBase();
			List<List<FillUnit>> fillObjects = getFillableUnits(fillParam, ranges, startPoint);
			if (containerBase == 0 || !isSolutionExists(startPoint, fillObjects))
				continue;
			List<Integer> solvableFactorIndex = null;
			List<Long> solution = Lists.newArrayList();
			if (result.hasSearchBefore() && result.getCandidateSolution() != null) {
				solvableFactorIndex = result.getLastChooseIndex();
				solution = result.getCandidateSolution();
			} else {
				if (result.hasSearchBefore()) {
					solvableFactorIndex = result.getLastChooseIndex();
				}
				solvableFactorIndex = findNewIndexes(fillParam, startPoint, fillObjects, solvableFactorIndex);
				if (solvableFactorIndex == null) {
					result.setAllSearch(true);
					return null;
				}
				List<List<Long>> solutions = solveEquation(fillObjects, solvableFactorIndex, containerBase,
						recalculateRange(fillParam, result, fillObjects), fillParam.getSolutionCount());
				while (solutions.size() == 0 && solvableFactorIndex != null) {
					solvableFactorIndex = findNewIndexes(fillParam, startPoint, fillObjects, solvableFactorIndex);
					if(solvableFactorIndex == null) {
						break;
					}
					solutions = solveEquation(fillObjects, solvableFactorIndex, containerBase,
							recalculateRange(fillParam, result, fillObjects), fillParam.getSolutionCount());
				}
				if (solvableFactorIndex == null) {
					result.setAllSearch(true);
					return null;
				}
				solution = solutions.get(0);
				if (!result.hasSearchBefore()) {
					result.setCandidateSolutions(solutions);
				}
			}
			List<FillUnit> chooseUnits = Lists.newArrayList();
			for (int i = 0; i < solvableFactorIndex.size(); i++) {
				chooseUnits.add(fillObjects.get(i).get(solvableFactorIndex.get(i)));
			}
			try {
				result.forward(fillParam, startPoint, solvableFactorIndex, solution, chooseUnits);
				if (result.score() > 0) {
					return result;
				}
				/*
				 * if(result.isFull()) break;
				 */
			} catch (BinPackingException e) {
				LOG.debug(e.getMessage() + "---(errorCode:" + e.getErrorCode() + ")");
			}

		}
		return result;
	}

	private List<Integer> findNewIndexes(FillParameter fillParam, FillUnit startPoint, List<List<FillUnit>> fillObjects,
			List<Integer> solvableFactorIndex) {

		Integer objectSize = fillObjects.size();

		Long containerBase = startPoint.getBase();
		Long height = startPoint.getHeight();
		Long maxTryTime = countStepMaxTryTime(fillParam, fillObjects);
		int[] limits = new int[objectSize];
		for (int i = 0; i < objectSize; i++) {
			limits[i] = 2;
		}
		solvableFactorIndex = findSolvableEquationFactorIndexs(solvableFactorIndex, 0, containerBase, height,
				fillObjects, limits);

		if (solvableFactorIndex == null)
			return null;
		while (!isSolvable(startPoint, solvableFactorIndex, fillObjects)) {
			solvableFactorIndex = findSolvableEquationFactorIndexs(solvableFactorIndex, 0, containerBase, height,
					fillObjects, limits);
			maxTryTime--;
			if (solvableFactorIndex == null) {
				return null;
			}
		}
		return solvableFactorIndex;
	}

	private Long countStepMaxTryTime(FillParameter fillParam, List<List<FillUnit>> fillableUnits) {

		Long tryTime = new Double(Math.pow(2, fillableUnits.size())).longValue();
		if (fillParam.getMaxListableSize() != null && tryTime > fillParam.getMaxListableSize()) {
			tryTime = fillParam.getMaxListableSize();
		}
		return tryTime;
	}

	private List<List<Long>> solveEquation(List<List<FillUnit>> fillableUnits, List<Integer> solvableFactorIndex,
			Long result, Long[][] ranges, int solutionCount) {
		Long[] factors = new Long[solvableFactorIndex.size()];
		for (int i = 0; i < factors.length; i++) {
			factors[i] = fillableUnits.get(i).get(solvableFactorIndex.get(i)).getBase();
		}
		Map<DESolutionConstraintType, DESolutionConstraint> constraints = Maps.newHashMap();

		constraints.put(DESolutionConstraintType.SOLUTION_RANGE,
				new DESolutionConstraint(DESolutionConstraintType.SOLUTION_RANGE, new Object[] { ranges }));
		DESolutionConstraint.addSolutionSize(constraints, solutionCount);
		try {
			DiophantineResult equationResult = DiophantineEquation.getSolution(factors, result);
			equationResult.constrain(constraints);
			equationResult.validate();
			return (List<List<Long>>) (equationResult.getSolutions().size() > 0 ? equationResult.getSolutions()
					: Lists.newArrayList());
		} catch (Exception e) {
			e.printStackTrace();
			return Lists.newArrayList();
		}
	}

}
