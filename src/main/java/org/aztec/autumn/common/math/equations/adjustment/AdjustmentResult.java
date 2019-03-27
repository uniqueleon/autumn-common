package org.aztec.autumn.common.math.equations.adjustment;

import java.util.Arrays;
import java.util.Map;

import com.beust.jcommander.internal.Maps;

public class AdjustmentResult {
	
	private Integer id;
	private Long[] solution;
	private boolean searched = false;
	private boolean blocked = false;
	private boolean end = false;
	private Map<AdjustmentStep,AdjustmentResult> neighborhood = Maps.newHashMap();

	public AdjustmentResult() {
		// TODO Auto-generated constructor stub
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long[] getSolution() {
		return solution;
	}

	public void setSolution(Long[] solution) {
		this.solution = solution;
	}

	public boolean isSearched() {
		return searched;
	}

	public void setSearched(boolean searched) {
		this.searched = searched;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Map<AdjustmentStep, AdjustmentResult> getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(Map<AdjustmentStep, AdjustmentResult> neighborhood) {
		this.neighborhood = neighborhood;
	}

	public AdjustmentResult(Long[] solution) {
		super();
		this.solution = solution;
		this.id = Arrays.hashCode(solution);
	}
	
	

}
