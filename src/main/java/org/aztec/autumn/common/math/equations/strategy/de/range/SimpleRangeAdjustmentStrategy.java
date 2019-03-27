package org.aztec.autumn.common.math.equations.strategy.de.range;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.MathException;
import org.aztec.autumn.common.math.equations.AdjustmentStrategy;
import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentResult;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentStep;
import org.aztec.autumn.common.math.equations.strategy.BaseAdjustmentStrategy;
import org.aztec.autumn.common.math.equations.zs.NDimensionEquation;
import org.aztec.autumn.common.math.equations.zs._3DEquation;
import org.aztec.autumn.common.utils.MathUtils;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;


public class SimpleRangeAdjustmentStrategy extends BaseAdjustmentStrategy implements AdjustmentStrategy{
	
	private static final Map<Integer,NDimensionEquation> equations = Maps.newHashMap();
	
	static {
		equations.put(3,new _3DEquation());
	}

	public SimpleRangeAdjustmentStrategy() {
		searchedIgnore = true;
	}

	@Override
	public AdjustmentResult makeAdjustment(Map<DESolutionConstraintType, DESolutionConstraint> constraints, Long[] solutions,
			List<Long> coefficients,Map<Integer,AdjustmentResult> searchedHistory,Object... otherParams) {
		init(searchedHistory);
		Long[][] ranges = (Long[][]) constraints.get(DESolutionConstraintType.SOLUTION_RANGE).getParams()[0];
		//Long[] tmpSolutions = solutions;
		Long[] tmpSolutions = Arrays.copyOf(solutions, solutions.length);
		List<List<GreatestCommonDivisor>> allGcds = Lists.newArrayList();
		if(otherParams != null && otherParams.length > 0) {
			allGcds = (List<List<GreatestCommonDivisor>>)otherParams[0];
		}
		else {
			allGcds = GreatestCommonDivisor.getAllGCD(coefficients.toArray(new Long[coefficients.size()]));
		}
		for(int i = 0;i < tmpSolutions.length;i++) {
			Long lastFound = null;
			Long findSolution = null;
			do{
				if(findSolution != null) {
					lastFound = findSolution;
				}
				findSolution = doAdjust(i, coefficients, ranges, solutions, tmpSolutions, allGcds);
				
			}while(findSolution != null && !findSolution.equals(lastFound) 
					&& (findSolution < ranges[i][0] || findSolution > ranges[i][1]) ) ;
		}
		return currentSteps.get().isEmpty() ? null : currentSteps.get().pop();
	}
	
	private Long doAdjust(int i,List<Long> coefficients,Long[][] ranges,Long[] solutions,Long[] tmpSolutions,List<List<GreatestCommonDivisor>> allGcds) {
		
		Long findSolution = adjustIn2D(i, coefficients, ranges, solutions, tmpSolutions, allGcds);
		if(findSolution > ranges[i][1] || findSolution < ranges[i][0]) {
			findSolution = adjustInND(i, coefficients, ranges, solutions, tmpSolutions, allGcds);
		}
		return findSolution;
	}
	
	
	private Long adjustIn2D(int i,List<Long> coefficients,Long[][] ranges,Long[] solutions,Long[] tmpSolutions,List<List<GreatestCommonDivisor>> allGcds) {
		long tmpSolution = tmpSolutions[i];
		if(tmpSolution > ranges[i][1] || tmpSolution < ranges[i][0]) {
			boolean direction = tmpSolution < ranges[i][0] ? true : false;
			AdjustmentStep step = super.findTwoFactorPartner(direction, tmpSolutions, ranges, i,0, allGcds, coefficients);
			while(step != null) {
				addStep(solutions, tmpSolutions, step);
				tmpSolutions[i] = tmpSolutions[i] + step.getMovableStep().get(0);
				int otherIndex = step.getIndexes().get(1);
				tmpSolutions[otherIndex] = tmpSolutions[otherIndex] + step.getMovableStep().get(1);
				tmpSolution = tmpSolutions[i];
				if(tmpSolution <= ranges[i][1] && tmpSolution >= ranges[i][0] )
					break;
				step = super.findTwoFactorPartner(direction, tmpSolutions, ranges, i,0, allGcds, coefficients);
			}
		}
		return tmpSolution;
	}
	
	private Long adjustInND(int index,List<Long> coefficients,Long[][] ranges,Long[] solutions,
			Long[] tmpSolutions,List<List<GreatestCommonDivisor>> allGcds) {
		Integer beginDimemsion = 3;
		NDimensionEquation ndEquation = null;
		int maxDimension =  3 + equations.size();
		Integer currentDimension = beginDimemsion;
		long tmpSolution = tmpSolutions[index];
		List<List<Integer>> searchHistories = Lists.newArrayList();
		boolean success = false;
		do {
			ndEquation = equations.get(currentDimension);

			if(ndEquation == null)
				throw new MathException();
			int indexSeqNo = 0;
			int maxCombinationCount = MathUtils.getCombinationNumber(coefficients.size() - 1, currentDimension - 1).intValue();
			while(indexSeqNo < maxCombinationCount) {
				indexSeqNo ++;
				adjustInZeroSumEquation(indexSeqNo,index, ndEquation, coefficients, solutions, tmpSolutions, ranges, searchHistories);
				tmpSolution = tmpSolutions[index];
				if(tmpSolution >= ranges[index][0] && tmpSolution <= ranges[index][1]) {
					return tmpSolution;
				}
			}
			if(success)
				break;
			currentDimension ++;
		} while (!success && ndEquation != null && currentDimension < maxDimension);
		return tmpSolution;
	}
	
	private boolean adjustInZeroSumEquation(
			Integer indexSeqNo,Integer index,NDimensionEquation ndEquation,List<Long> coefficients,
			Long[] solutions,Long[] tmpSolutions,Long[][] ranges,List<List<Integer>> searchHistories) {
		Integer currentDimension = ndEquation.getDimension();

		List<Integer> indexes = Lists.newArrayList();
		indexes.add(index);
		List<Integer> selectableIndexes = Lists.newArrayList();
		for(int i = 0;i < coefficients.size();i++) {
			if(i == index)
				continue;
			selectableIndexes.add(i);
		}
		List<Integer> pickupIndexes = pickupIndexes(indexSeqNo,selectableIndexes, 
				currentDimension - 1, indexes);
		searchHistories.add(pickupIndexes);
		if(pickupIndexes == null)
			return false;
		long rangeSeq = 0l;
		long totalRangeSeq = new Double(Math.pow(2, indexes.size())).longValue();
		List<Long> pkCoefficients = pickupCoefficients(coefficients, pickupIndexes);
		ndEquation.init(pkCoefficients);
		while(rangeSeq < totalRangeSeq ) {
			Long[][] newRanges = pickUpRanges(rangeSeq,pickupIndexes, tmpSolutions, ranges);
			List<Long[]> acceptableSolution = ndEquation.getAcceptableSolution( newRanges, Lists.newArrayList(), currentDimension);
			if(acceptableSolution.size() > 0) {
				Long[] findSolution = findBestSolution(acceptableSolution);
				if(findSolution != null) {
					List<Long> movableStep = Lists.newArrayList(findSolution);
					AdjustmentStep step = new AdjustmentStep(tmpSolutions, pickupIndexes, movableStep);
					move(tmpSolutions, step);
					return true;
				}
			}
			rangeSeq ++;
		}
		return false;
	}
	
	public Long[] findBestSolution(List<Long[]> solutions) {
		Long[] bestSolution = null;
		Long maxStep = 0l;
		if(solutions != null && solutions.size() > 0) {
			for(int i = 0;i < solutions.size();i++) {
				Long[] tmpSolution = solutions.get(i);
				Long thisChange = Math.abs(tmpSolution[0]);
				if(thisChange > 0 && (bestSolution == null || thisChange > maxStep)) {
					maxStep = thisChange;
					bestSolution = tmpSolution;
				}
			}
		}
		return bestSolution;
	}
	
	
	public static List<Integer> pickupIndexes(Integer sequenceNo,List<Integer> selectableIndexes,int count,List<Integer> indexes){
		List<Integer> combinations = MathUtils.getNextCombination(Lists.newArrayList(),count , 1, selectableIndexes.size() - 1);
		for(int i = 0;i < sequenceNo ;i ++) {
			combinations = MathUtils.getNextCombination(combinations, count, 1, selectableIndexes.size() - 1);
			if(combinations == null)
				return null;
		}
		for(int i = 0 ;i < combinations.size();i++) {
			indexes.add(selectableIndexes.get(combinations.get(i)));
		}
		return indexes;
	}
	
	
	
	public List<Long> pickupCoefficients(List<Long> coefficients,List<Integer> indexes){
		List<Long> newCoefficients = Lists.newArrayList();
		for(int i = 0;i < indexes.size();i++) {
			newCoefficients.add(coefficients.get(indexes.get(i)));
		}
		return newCoefficients;
	}
	
	public Long[][] pickUpRanges(long sequenceNo,List<Integer> indexes,Long[] tmpSolutions,Long[][] ranges){
		Long[][] newRanges = new Long[indexes.size()][2];
		
		BitSet bs = BitSet.valueOf(new long[] {sequenceNo});
		for(int i = 0;i < indexes.size();i++) {
			Integer choosenIndex = indexes.get(i);
			Long tmpS = tmpSolutions[choosenIndex];
			Long minR = ranges[choosenIndex][0];
			Long maxR = ranges[choosenIndex][1];
			boolean isUpperHalf = bs.get(i);
			long distance = minR - tmpS;
			if(isUpperHalf) {
				distance = maxR - tmpS;
			}
			newRanges[i][0] = distance > 0 ? 0l : distance;
			newRanges[i][1] = distance > 0 ? distance : 0l;
		}
		return newRanges;
	}
}
