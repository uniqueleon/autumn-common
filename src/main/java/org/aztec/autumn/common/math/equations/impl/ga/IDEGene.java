package org.aztec.autumn.common.math.equations.impl.ga;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;

public class IDEGene implements Gene {
	
	private Long factor;
	private Long solution;
	private Long[] selectableFactors;
	private Long[] solutionRange;
	private TranningConfig config;

	public IDEGene(Long factor, Long solution,Long[] selectableFactors,Long[] solutionRange,
			TranningConfig config) {
		super();
		this.factor = factor;
		this.solution = solution;
		this.selectableFactors = selectableFactors;
		this.solutionRange = solutionRange;
		this.config = config;
	}


	@Override
	public double getMutationRate() {
		return config.getMutationRatio();
	}

	public Long getFactor() {
		return factor;
	}


	public void setFactor(Long factor) {
		this.factor = factor;
	}


	public Long getSolution() {
		return solution;
	}


	public void setSolution(Long solution) {
		this.solution = solution;
	}


	@Override
	public Gene mutate() {
		double testNum = RandomUtils.nextDouble();
		if(testNum < config.getMutationRatio()) {
			return this;
		}
		int randomInt = RandomUtils.nextInt(selectableFactors.length);
		while(selectableFactors[randomInt].equals(factor)) {
			randomInt = RandomUtils.nextInt(selectableFactors.length);
		}
		Long newFactor = selectableFactors[randomInt];
		Long distance = solutionRange[1] - solutionRange[0] + 1;
		randomInt = RandomUtils.nextInt(distance.intValue());
		Long newSolution = solutionRange[0] + randomInt;
		while(newSolution.equals(solution)) {
			randomInt = RandomUtils.nextInt(distance.intValue());
			newSolution = solutionRange[0] + randomInt;
		}
		return new IDEGene(newFactor, newSolution, selectableFactors, solutionRange,config);
	}

	@Override
	public <T> T get() {
		// TODO Auto-generated method stub
		return (T) this;
	}

	@Override
	public IDEGene clone() throws CloneNotSupportedException {
		return new IDEGene(new Long(factor), new Long(solution),
				selectableFactors,solutionRange,config);
	}

	
}
