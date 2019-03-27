package org.aztec.autumn.common.math.equations.impl.ga;

import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.GeneGenerator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.IndividualGenerator;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;

public class DEIndividualGenerator implements IndividualGenerator {
	
	private TranningConfig config;
	private GeneGenerator generator;
	
	
	public DEIndividualGenerator( GeneGenerator generator,TranningConfig config) {
		super();
		this.config = config;
		this.generator = generator;
	}

	@Override
	public Individual generate() {
		Long[][] factors = (Long[][]) config.getParameters().get(IDE_GA_Constant.ConfigurationKeys.SOLVING_FACTORS);
		Gene[] genes = new Gene[factors.length];
		for(int i = 0;i < factors.length;i++) {
			genes[i] = generator.generate(i,config);
		}
		return new DiophatineEquationIndividual(this, genes, config);
	}

	@Override
	public Individual generate(Gene[] genes) {
		return new DiophatineEquationIndividual(this, genes, config);
	}

	@Override
	public void setGeneGenerator(GeneGenerator generator) {
		this.generator = generator;
	}


}
