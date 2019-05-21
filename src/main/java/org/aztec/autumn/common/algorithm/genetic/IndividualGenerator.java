package org.aztec.autumn.common.algorithm.genetic;

public interface IndividualGenerator {

	public Individual generate(Gene[] genes);
	public void setGeneGenerator(GeneGenerator generator);
	public Individual generate();
}
