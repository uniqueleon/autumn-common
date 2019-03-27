package org.aztec.autumn.common.math.equations.adjustment;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public  class AdjustmentStep{

	private Integer id;
	private List<Integer> indexes = Lists.newArrayList();
	private List<Long> movableStep = Lists.newArrayList();
	public List<Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Integer> indexes) {
		this.indexes = indexes;
	}
	public List<Long> getMovableStep() {
		return movableStep;
	}
	public void setMovableStep(List<Long> movableStep) {
		this.movableStep = movableStep;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public AdjustmentStep(Long[] solution,List<Integer> indexes, List<Long> movableStep) {
		super();
		id = Arrays.hashCode(solution);
		this.indexes = indexes;
		this.movableStep = movableStep;
	}
	public AdjustmentStep() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((indexes == null) ? 0 : indexes.hashCode());
		result = prime * result + ((movableStep == null) ? 0 : movableStep.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdjustmentStep other = (AdjustmentStep) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (indexes == null) {
			if (other.indexes != null)
				return false;
		} else if (!indexes.equals(other.indexes))
			return false;
		if (movableStep == null) {
			if (other.movableStep != null)
				return false;
		} else if (!movableStep.equals(other.movableStep))
			return false;
		return true;
	}
	
}
