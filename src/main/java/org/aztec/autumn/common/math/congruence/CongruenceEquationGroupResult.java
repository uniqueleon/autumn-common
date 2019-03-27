package org.aztec.autumn.common.math.congruence;

public class CongruenceEquationGroupResult {
	
	
	private Long base;
	private Long[] factors;
	private boolean solved = false;

	public CongruenceEquationGroupResult() {
		// TODO Auto-generated constructor stub
	}


	public Long getBase() {
		return base;
	}

	public void setBase(Long base) {
		this.base = base;
	}

	public Long[] getFactors() {
		return factors;
	}

	public void setFactors(Long[] factors) {
		this.factors = factors;
	}


	@Override
	public String toString() {
		if(!solved) {
			return "The congruence equation group is not solved yet!";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("The solution is :\nx=" + base + " + " );
		for(Long factor : factors ) {
			builder.append(factor + " * ");
		}
		builder.append("t");
		return builder.toString();
	}

	
	
}
