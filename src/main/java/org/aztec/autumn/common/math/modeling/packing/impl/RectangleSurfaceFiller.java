package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;
import java.util.Map;

import static org.aztec.autumn.common.math.modeling.packing.BinPackingContext.ContextKeys.SORT_EDGES;

import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.math.modeling.packing.AlgorithmContextFactory.ContextKeys;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContext;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContextFactory;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.utils.MathUtils;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RectangleSurfaceFiller {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RectangleSurfaceFiller.class);
	

	public FillResultImpl fill(FillParameter fillParam) {

		if(fillParam.getItemFillUnits().size() < 0){
			return null;
		}
		FillResultImpl result = new FillResultImpl(fillParam.getStartPoint(), fillParam.getFillType(),
				fillParam.getStartPoint().getLocation());
		FillResultImpl bestResult = null;
		int maxTryTime = fillParam.getMaxTryTime();
		int failTime = 0;
		while (!result.isFull() && failTime < maxTryTime) {
			int lastStep = result.getCurrentStep();
			tryFill(result, fillParam);
			int newStep = result.getCurrentStep();
			if(result.isAllSearch())
				return result;
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
				failTime ++;
			}
		}
		return bestResult;
	}

	private List<Integer> findSolvableEquationFactorIndexs(List<Integer> lastPath, int chooseIndex, Long result,
			Long height, List<List<FillUnit>> fillableUnits,int[] limits) {
		int objectSize = fillableUnits.size();
		List<Integer> newPath = MathUtils.getNextPermutation(lastPath, objectSize, limits);
		//List<Integer> newPath = MathUtils.getRandomPermutation(lastPath, objectSize, limits);
		while (newPath != null 
				&& !DiophantineResult.hasSolution(result, calculateGCDs(newPath, fillableUnits))) {
			newPath = MathUtils.getNextPermutation(newPath, objectSize, limits);
			//newPath = MathUtils.getRandomPermutation(newPath, objectSize, limits);
		}
		return newPath;

	}

	private GreatestCommonDivisor calculateGCDs(List<Integer> indexes, List<List<FillUnit>> fillableUnits) {
		Long[] factors = new Long[indexes.size()];
		for (int i = 0; i < indexes.size(); i++) {
			factors[i] = fillableUnits.get(i).get(indexes.get(i)).getLength();
		}
		GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(factors);
		return gcd;
	}
	
	
	
	private boolean isSolutionExists(FillUnit startPoint,List<List<FillUnit>> fillObjects) {
		BinPackingContext context = BinPackingContextFactory.getContext();
		List<Long> sortEdges = context.get(SORT_EDGES);
		Long base = startPoint.getLength();
		if(base < sortEdges.get(0)) {
			return false;
		}
		Long lowestUnit = Long.MAX_VALUE;
		for(int i = 0;i < fillObjects.size();i++) {
			for(FillUnit unit : fillObjects.get(i)) {
				if(unit.getLength() < lowestUnit) {
					lowestUnit = unit.getLength();
				}
			}
		}
		if(base < lowestUnit) {
			return false;
		}
		return true;
	}
	
	private List<List<FillUnit>> getFillableUnits(FillParameter fillParam,Long[][] ranges,FillUnit startPoint){
		
		Long base = startPoint.getLength();
		Long height = startPoint.getWidth();
		List<List<FillUnit>> fillableUnits = Lists.newArrayList();
		List<List<FillUnit>> allUnits = fillParam.getItemFillUnits();
		for(int i = 0;i < allUnits.size();i++) {
			List<FillUnit> unit = allUnits.get(i);
			Long[] range = ranges[i];
			boolean isSolutionExists = range[1] > range[0] ? true : false;
			Long shortOne = new Double(Math.min(unit.get(0).getLength(), unit.get(1).getLength())).longValue();
			Long longOne = new Double(Math.max(unit.get(0).getLength(), unit.get(1).getLength())).longValue();
			Long[] compareArr1 = new Long[] {shortOne,longOne};
			Long shortOne1 = new Double(Math.min(base, height)).longValue();
			Long longOne1 = new Double(Math.max(base, height)).longValue();
			Long[] compareArr2 = new Long[] {shortOne1,longOne1};
			if(isSolutionExists && (compareArr2[1] >= compareArr1[1] && compareArr2[0] >= compareArr1[0])) {
				fillableUnits.add(unit);
			}
		}
		return fillableUnits;
	}
	
	private Long[][] recalculateRange(FillParameter fillParam,FillResultImpl tmpFillResult,
			List<List<FillUnit>> fillableUnits) {
		Long[][] ranges = new Long[fillableUnits.size()][2];
		for (int i = 0; i < fillableUnits.size(); i++) {
			FillUnit fillUnit = fillableUnits.get(i).get(0);
			ranges[i][0] = 0l;
			Item item = (Item)fillUnit.getBelongObject();
			Long objNum = item.getNumber();
			Long itemCount = tmpFillResult.getUsedCount().get(item.getId());
			ranges[i][1] = objNum - (itemCount == null ? 0 : itemCount);
		}
		return ranges;
	}
	
	private Long[][] getPossibleRanges(FillResultImpl tmpFillResult, FillParameter fillParam){
		
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
	
	private boolean isSolvable(FillUnit startPoint,List<Integer> factorIndex,List<List<FillUnit>> fillableUnit) {
		
		for(int i = 0;i < factorIndex.size();i++) {
			FillUnit targetUnit = fillableUnit.get(i).get(factorIndex.get(i));
			if(targetUnit.getWidth() > startPoint.getWidth()) {
				return false;
			}
		}
		
		return true;
	}
	
	
	
	private Object[] tryFindBestSolutions(List<List<FillUnit>> fillObjects,FillResultImpl result,FillUnit startPoint,FillParameter fillParam){

		Long containerBase = startPoint.getLength();
		if(containerBase == 0 || !isSolutionExists(startPoint,fillObjects))
			return null;
		Object[] retObjects = null;
		Long bestScore = 0l;
		long testCount = 0;
		long maxCount = getExhaustionCount(fillParam, fillObjects);
		Long[][] ranges = recalculateRange(fillParam,result,fillObjects);
		List<Integer> tempSolvableFactorIndex = findNewIndexes(fillParam, startPoint, fillObjects, null);
		do{
			if(tempSolvableFactorIndex == null)
				break;
			Long[] factors = new Long[tempSolvableFactorIndex.size()];
			Long[] heights = new Long[tempSolvableFactorIndex.size()];
			for (int i = 0; i < factors.length; i++) {
				factors[i] = fillObjects.get(i).get(tempSolvableFactorIndex.get(i)).getLength();
				heights[i] = fillObjects.get(i).get(tempSolvableFactorIndex.get(i)).getWidth();
			}
			List<List<Long>> tempSolutions = solveEquation(factors,
					containerBase,ranges, fillParam.getSolutionCount());
			for(List<Long> solution : tempSolutions){
				Long testScore = getSolutionScore(solution,factors,heights);
				if(testScore > bestScore){
					List<Integer> bestFactors = Lists.newArrayList();
					bestFactors.addAll(tempSolvableFactorIndex);
					retObjects = new Object[]{solution,bestFactors};
					bestScore = testScore;
				}
			}
			testCount ++;
			if(testCount >= maxCount){
				return retObjects;
			}
			tempSolvableFactorIndex = findNewIndexes(fillParam, startPoint, fillObjects, tempSolvableFactorIndex);
		}while(tempSolvableFactorIndex != null);
		return retObjects;
	}
	
	private FillResultImpl tryFill(FillResultImpl result, FillParameter fillParam) {

		if (result.isFull() || result.isSuccess()) {
			return result;
		}
		Long[][] ranges = getPossibleRanges(result, fillParam);
		
		List<FillUnit> fillTargets = Lists.newArrayList();
		fillTargets.addAll(result.getUnfilledArea());
		//FillResult bestResult = null;
		//List<List<Long>> solutions = Lists.newArrayList();
		for (int k = 0;k < fillTargets.size();k++) {
			FillUnit startPoint = fillTargets.get(k);

			//List<Integer> solvableFactorIndex = Lists.newArrayList();
			//List<List<FillUnit>> fillObjects = Lists.newArrayList();
			List<List<FillUnit>> fillObjects = getFillableUnits(fillParam, ranges,startPoint);
			Object[] bestSolution = tryFindBestSolutions(fillObjects, result, startPoint, fillParam);
			if(bestSolution == null){
				return result;
			}
			List<Integer> solvableFactorIndex = (List<Integer>) bestSolution[1];
			List<Long> solution = (List<Long>) bestSolution[0];
			if(solution == null){
				return result;
			}
			List<FillUnit> chooseUnits = Lists.newArrayList();
			for (int i = 0; i < solvableFactorIndex.size(); i++) {
				chooseUnits.add(fillObjects.get(i).get(solvableFactorIndex.get(i)));
			}
			try {
				result.forward(fillParam.getTotalCount(), startPoint, solvableFactorIndex , solution, chooseUnits);
				if(result.score() > 0){
					return result;
				}
				if(result.isFull())
					break;
			} catch (BinPackingException e) {
				LOG.debug(e.getMessage() + "---(errorCode:" + e.getErrorCode() + ")");	
			}
		}
		return result;
	}
	
	private List<Integer> findNewIndexes(FillParameter fillParam,FillUnit startPoint,List<List<FillUnit>> fillObjects,
			List<Integer> solvableFactorIndex){
		Integer objectSize = fillObjects.size();
		Long containerBase = startPoint.getLength();
		Long height = startPoint.getWidth();
		int[] limits = new int[objectSize];
		for (int i = 0; i < objectSize; i++) {
			limits[i] = 2;
		}
		solvableFactorIndex = findSolvableEquationFactorIndexs(solvableFactorIndex, 0, containerBase,
				height, fillObjects,limits);
		while (solvableFactorIndex != null && !isSolvable(startPoint, solvableFactorIndex, fillObjects)) {
			solvableFactorIndex = findSolvableEquationFactorIndexs(solvableFactorIndex, 0, containerBase,
					height, fillObjects,limits);
		}
		return solvableFactorIndex;
	}
	
	private Long getSolutionScore(List<Long> solutions,Long[] factors,Long[] heights){
		Long score = 0l;
		Long tmpScore = 0l;
		Long tmpHeight = 0l;
		for(int i = 0;i < solutions.size();i++){
			if(solutions.get(i) == 0)
				continue;
			Long testNum = solutions.get(i);
			if(tmpHeight == 0){
				tmpHeight = heights[i];
				tmpScore = testNum * factors[i];
				score = tmpScore;
			}
			else if (tmpHeight == heights[i]){
				tmpScore += testNum * factors[i];
			}
			else{
				if(tmpScore > score){
					score = tmpScore;
				}
				tmpHeight = heights[i];
				tmpScore = testNum * factors[i];
			}
			if(i == solutions.size() - 1){
				if(tmpScore > score){
					score = tmpScore;
				}
			}
			//score += testNum;
		}
		return score;
	}
	

	private Long getExhaustionCount(FillParameter fillParam,List<List<FillUnit>> fillableUnits) {
		
		Long tryTime = new Double(Math.pow(2,fillableUnits.size())).longValue();
		if(fillParam.getMaxExhaustionSize() != null && tryTime > fillParam.getMaxExhaustionSize()) {
			tryTime = fillParam.getMaxExhaustionSize();
		}
		return tryTime;
	}

	private List<List<Long>> solveEquation(Long[] factors, Long result,Long[][] ranges,int solutionCount) {
		
		Map<DESolutionConstraintType, DESolutionConstraint> constraints = Maps.newHashMap();
		
		List<Long> realFactors = Lists.newArrayList();
		for(int i = 0;i < factors.length;i++){
			if(factors[i] <= result){
				realFactors.add(factors[i]);
			}
		}
		if(realFactors.size() == 0)
			return Lists.newArrayList();
		GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(realFactors.toArray(new Long[realFactors.size()]));
		if(result < gcd.getGcd() || result % gcd.getGcd() != 0)
			return Lists.newArrayList();
		

		constraints.put(DESolutionConstraintType.SOLUTION_RANGE,
				new DESolutionConstraint(DESolutionConstraintType.SOLUTION_RANGE, new Object[] { ranges }));
		DESolutionConstraint.addSolutionSize(constraints, solutionCount );
		try {
			DiophantineResult equationResult = DiophantineEquation.getSolution(factors, result);
			if(equationResult == null || !equationResult.getHasSolution()){
				return Lists.newArrayList();
			}
			equationResult.constrain(constraints);
			equationResult.validate();
			return (List<List<Long>>) (equationResult.getSolutions().size() > 0 ? equationResult.getSolutions() : Lists.newArrayList());
		} catch (Exception e) {
			e.printStackTrace();
			return Lists.newArrayList();
		}
	}

}
