package org.aztec.autumn.common.algorithm.genetic.impl;

import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.IndividualGenerator;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;

public abstract class BaseIndividual implements Individual {

	protected Gene[] genes;
	protected TranningConfig config;
	private IndividualGenerator generator;
	private static Random random = new Random();

	public BaseIndividual(IndividualGenerator generator,Gene[] genes,TranningConfig config) {
		// TODO Auto-generated constructor stub
		this.genes = genes;
		this.config = config;
		this.generator = generator;
	}

	public Object get() {
		return this;
	}

	@Override
	public Gene[] getGenes() {
		return genes;
	}

	@Override
	public void mutate() {
		for (int i = 0; i < genes.length; i++) {
			double randomNum = RandomUtils.nextDouble();
			Gene gene = genes[i];
			if (randomNum < gene.getMutationRate()) {
				genes[i] = gene.mutate();
			}
		}
	}

	@Override
	public Individual couple(Individual individual) {
		double testNum = random.nextDouble();
		if(testNum < config.getCoupleRatio()) {
			return null;
		}
		Gene[] newGenes = new Gene[this.genes.length];
		try {
			if (genes.length == 1) {
				int randomNum = RandomUtils.nextInt(2);
				if(randomNum > 0) {
					newGenes[0] = this.genes[0].clone();
				}
				else {
					newGenes[0] = individual.getGenes()[0].clone();
				}
			} else {
				for (int i = 0; i < genes.length; i ++) {
					Gene newGene = null;
					if(i % 2 == 0) {
						newGene = individual.getGenes()[i].clone();
					}
					else {
						newGene = genes[i].clone();
					}
					newGenes[i] = newGene;
				}
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generator.generate(newGenes);
	}

	@Override
	public <T> T adapt(Class<T> targetCls) {
		if(targetCls.isAssignableFrom(this.getClass())){
			return (T) this;
		}
		return null;
	}
	
}
