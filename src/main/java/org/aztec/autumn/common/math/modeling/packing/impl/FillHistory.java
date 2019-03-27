package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

public class FillHistory {

	private List<Integer> chooseIndexes;
	private int depth;
	private int lastSolutionIndex;
	private List<List<Long>> candidateSolutions;
	public List<Integer> getChooseIndexes() {
		return chooseIndexes;
	}
	public void setChooseIndexes(List<Integer> chooseIndexes) {
		this.chooseIndexes = chooseIndexes;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public List<List<Long>> getCandidateSolutions() {
		return candidateSolutions;
	}
	public void setCandidateSolutions(List<List<Long>> candidateSolutions) {
		this.candidateSolutions = candidateSolutions;
	}
	
	public int getLastSolutionIndex() {
		return lastSolutionIndex;
	}
	public void setLastSolutionIndex(int lastSolutionIndex) {
		this.lastSolutionIndex = lastSolutionIndex;
	}
	public FillHistory(List<Integer> chooseIndexes, int stepNo, int lastSelectIndex,
			List<List<Long>> candidateSolutions) {
		super();
		this.chooseIndexes = chooseIndexes;
		this.depth = stepNo;
		this.lastSolutionIndex = lastSelectIndex;
		this.candidateSolutions = candidateSolutions;
	}
	
	public FillHistory(int stepNo, 
			List<List<Long>> candidateSolutions) {
		super();
		this.depth = stepNo;
		this.candidateSolutions = candidateSolutions;
	}
	@Override
	public String toString() {
		return "FillHistory [chooseIndexes=" + chooseIndexes + ", depth="
				+ depth + ", lastSolutionIndex=" + lastSolutionIndex
				+ ", candidateSolutions=" + candidateSolutions + "]";
	}
	
	
}
