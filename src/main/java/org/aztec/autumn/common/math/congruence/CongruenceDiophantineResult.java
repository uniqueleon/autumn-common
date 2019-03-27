package org.aztec.autumn.common.math.congruence;

import java.util.List;

public class CongruenceDiophantineResult {
	
	CongruenceDiophantineEquation equation;
	private List<Long> solutions;

	public CongruenceDiophantineResult() {
		// TODO Auto-generated constructor stub
	}

	public CongruenceDiophantineEquation getEquation() {
		return equation;
	}

	public void setEquation(CongruenceDiophantineEquation equation) {
		this.equation = equation;
	}

	public List<Long> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Long> solutions) {
		this.solutions = solutions;
	}

}
