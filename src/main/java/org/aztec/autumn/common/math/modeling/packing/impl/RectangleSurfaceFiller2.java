package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException;

import com.google.common.collect.Lists;

public class RectangleSurfaceFiller2 {

	public FillResultImpl fill(FillParameter fillParam) throws BinPackingException {
		FillResultImpl result = new FillResultImpl(fillParam.getStartPoint(),
				fillParam.getFillType(), fillParam.getStartPoint()
						.getLocation());
		// result.forward(fillParam, result.getStartPoint(), chooseIndex,
		// solutions, units);
		if(fillParam.getItemFillUnits().size() > 0){
			tryFill(fillParam, result, fillParam.getItemFillUnits());
		}
		return result;
	}

	private void tryFill(FillParameter fillParam, FillResultImpl result,
			List<List<FillUnit>> fillUnits) throws BinPackingException {
		EquationMetaData factors = new EquationMetaData(fillUnits);
		List<FillUnit> fillObjects = Lists.newArrayList();
		fillObjects.addAll(result.getUnfilledArea());
		for (FillUnit startPoint : fillObjects) {

			SolutionData sData = findSolution(factors, startPoint.getLength(),
					fillParam.getSolutionCount(), fillUnits);
			if(sData != null){
				result.forward(fillParam.getTotalCount(), startPoint, sData.getSolution(), sData.getFillUnit());
			}
		}

	}

	private SolutionData findSolution(EquationMetaData metaData, Long base,
			int solutionNum, List<List<FillUnit>> fillUnits) {
		List<List<Long>> solutions = DiophantineEquation
				.getConstraintedSolutions(metaData.getFactors(), base,
						metaData.getRanges(), solutionNum, new int[] { 1, 2 },
						metaData.getGroupDatas());
		if (solutions != null && solutions.size() > 0) {
			SolutionData bestSolution = findBestSolution(toSolutionData(solutions, 
					metaData.getGroupDatas(), fillUnits));
			return bestSolution;
		}
		return null;
	}
	
	private List<SolutionData> toSolutionData(List<List<Long>> solutions,int[] groupDatas,
			List<List<FillUnit>> units){
		List<SolutionData> sDatas = Lists.newArrayList();
		for(List<Long> solution : solutions){
			sDatas.add(new SolutionData(solution, groupDatas, units));
		}
		return sDatas;
	}
	

	public SolutionData findBestSolution(List<SolutionData> sDatas) {
		double bestScore = 0d;
		int bestIndex = -1;
		if(sDatas.size() == 1){
			return sDatas.get(0);
		}
		for (int i = 0; i < sDatas.size(); i++) {
			SolutionData sData = sDatas.get(i);
			double thisScore = PackingUtils
					.getSolutionScore(sData.getSolution(), sData.getFactors(),
							sData.getHeights());
			if (bestScore < thisScore) {
				bestScore = thisScore;
				bestIndex = i;
			}
		}
		return bestIndex != -1 ? sDatas.get(bestIndex) : null;
	}


	public class SolutionData {
		private List<Long> solution;
		private Long[] factors;
		private Long[] heights;
		private List<FillUnit> fillUnit;

		public List<Long> getSolution() {
			return solution;
		}

		public void setSolution(List<Long> solution) {
			this.solution = solution;
		}


		public SolutionData(List<Long> solution,int[] groupData,
				List<List<FillUnit>> units) {
			super();
			this.fillUnit = Lists.newArrayList();
			this.solution = solution;
			this.factors = new Long[solution.size()];
			this.heights = new Long[solution.size()];
			int[] seperators = new int[groupData.length];
			for(int i = 0;i < groupData.length;i++){
				if(i == 0){
					seperators[i] = groupData[i];
				}
				else{
					seperators[i] = seperators[i - 1] + groupData[i];
				}
			}
			int itemNo = 0;
			for (int i = 0; i < solution.size(); i++) {
				if(seperators[itemNo] <= i){
					itemNo ++;
				}
				Integer unitIndex = itemNo == 0 ? i : (i - seperators[itemNo - 1]);
				FillUnit unit =  units.get(itemNo).get(unitIndex);
				factors[i] = unit.getLength();
				heights[i] = unit.getWidth();
				this.fillUnit.add(unit);
			}
		}

		public Long[] getFactors() {
			return factors;
		}

		public void setFactors(Long[] factors) {
			this.factors = factors;
		}

		public Long[] getHeights() {
			return heights;
		}

		public void setHeights(Long[] heights) {
			this.heights = heights;
		}

		public List<FillUnit> getFillUnit() {
			return fillUnit;
		}

		public void setFillUnit(List<FillUnit> fillUnit) {
			this.fillUnit = fillUnit;
		}

	}

	public class EquationMetaData {
		private Long[] factors;
		private int[] groupDatas;
		private Long[][] ranges;

		public EquationMetaData(List<List<FillUnit>> funits) {
			init(funits);
		}

		private void init(List<List<FillUnit>> funits) {
			List<List<FillUnit>> fUnits = funits;
			List<Long> edges = Lists.newArrayList();
			List<Long> edgeRanges = Lists.newArrayList();
			groupDatas = new int[fUnits.size()];
			for (int i = 0; i < fUnits.size(); i++) {
				List<FillUnit> funit = fUnits.get(i);
				for (FillUnit unit : funit) {
					edges.add(unit.getLength());
					edgeRanges.add(unit.getBelongObject().getNumber());
				}

				groupDatas[i] = funit.size();
			}
			factors = new Long[edges.size()];
			factors = edges.toArray(factors);
			ranges = new Long[edges.size()][2];
			for(int i = 0;i < edgeRanges.size();i++){
				ranges[i][0] = 0l;
				ranges[i][1] = edgeRanges.get(i);
			}
		}

		public Long[] getFactors() {
			return factors;
		}

		public void setFactors(Long[] factors) {
			this.factors = factors;
		}

		public int[] getGroupDatas() {
			return groupDatas;
		}

		public void setGroupDatas(int[] groupDatas) {
			this.groupDatas = groupDatas;
		}

		public Long[][] getRanges() {
			return ranges;
		}

		public void setRanges(Long[][] ranges) {
			this.ranges = ranges;
		}

	}

}
