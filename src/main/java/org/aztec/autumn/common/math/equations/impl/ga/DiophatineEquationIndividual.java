package org.aztec.autumn.common.math.equations.impl.ga;

import java.util.Map;

import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.IndividualGenerator;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;
import org.aztec.autumn.common.algorithm.genetic.impl.BaseIndividual;

public class DiophatineEquationIndividual extends BaseIndividual implements Individual {
	

	public DiophatineEquationIndividual(IndividualGenerator generator,Gene[] genes, TranningConfig config) {
		super(generator,genes, config);
		
	}

	@Override
	public Map<String, Object> getAsMap() {

		Long[] factors = new Long[genes.length];
		
		return null;
	}
	
	public Long getResult() {
		Long result = 0l;
		for(int i = 0;i < genes.length;i++) {
			IDEGene gene = (IDEGene) genes[i];
			result += gene.getFactor() * gene.getSolution();
		}
		return result;
	}
	
	public Long[] getFactors() {
		Long[] factors = new Long[genes.length];
		for(int i = 0;i < genes.length;i++) {

			IDEGene gene = (IDEGene) genes[i];
			factors[i] = gene.getFactor();
		}
		return factors;
	}
	
	public Long[] getSolution() {

		Long[] solutions = new Long[genes.length];
		for(int i = 0;i < genes.length;i++) {
			IDEGene gene = (IDEGene) genes[i];
			solutions[i] = gene.getSolution();
		}
		return solutions;
	}

}
