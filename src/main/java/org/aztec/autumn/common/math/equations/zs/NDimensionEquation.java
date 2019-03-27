package org.aztec.autumn.common.math.equations.zs;

import java.util.List;

import org.aztec.autumn.common.math.MathException;

public interface NDimensionEquation {

	public Integer getDimension();
	public boolean accept(List<Long> coefficients);
	public void init(List<Long> conefficients);
	public List<Long[]> getAcceptableSolution(Long[][] ranges,List<List<Long>> excluded,int acceptableNum);
	public void validate(List<Long> coefficients,List<Long[]> solutions) throws MathException;
}
