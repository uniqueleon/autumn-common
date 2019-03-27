package org.aztec.autumn.common.math.congruence;

import java.util.List;

import org.aztec.autumn.common.utils.MathUtils;

import com.beust.jcommander.internal.Lists;

public class CongruenceEquationResult{

	private Long modular;
	private Long coefficient;
	private Long remainder;
	private Long leastRemainder;
	private boolean solved = false;
	private List<Long> results;
	public Long getModular() {
		return modular;
	}
	public void setModular(Long modular) {
		this.modular = modular;
	}
	public Long getRemainder() {
		return remainder;
	}
	public void setRemainder(Long remainder) {
		this.remainder = remainder;
	}
	public boolean isSolved() {
		return solved;
	}
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	public List<Long> getResults() {
		return results;
	}
	public void setResults(List<Long> results) {
		this.results = results;
	}
	public CongruenceEquationResult(Long coefficient,Long modular, Long remainder) {
		super();
		this.coefficient = coefficient;
		this.modular = modular;
		this.remainder = remainder;
		this.leastRemainder = MathUtils.getLeastRemainder(remainder, modular);
		this.results = Lists.newArrayList();
	}
	
	
	
	@Override
	public String toString() {
		return String.format("%2$d * X = %4$d (mod %3$d) , X = %1$s (mod %3$d)",new Object[] {results.toString(),coefficient,modular,leastRemainder});
	}
	
	public void validate() {
		if(solved) {
			for(Long result : results) {
				Long testNum = coefficient * result;
				Long r1 = MathUtils.getLeastRemainder(testNum, modular);
				Long r2 = MathUtils.getLeastRemainder(remainder, modular);
				if(!r1.equals(r2)) {
					throw new ArithmeticException(String.format("Result[%1$d] dose not match %2$d * X = %3$d (mod %4$d) ",new Object[] {result,coefficient,modular,remainder}));
				}
			}
		}
	}
	
	public List<Long> findSolutions(Long[] range){
		List<Long> solutions = Lists.newArrayList();
		for(Long result : results) {
			List<Long> numbers = MathUtils.findPossibleNumbers(range, modular, result);
			if(numbers != null && numbers.size() > 0) {
				solutions.addAll(numbers);
			}
		}
		return solutions;
	}
	
}