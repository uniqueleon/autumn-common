package org.aztec.autumn.common.math.equations.impl.ga;

public interface IDE_GA_Constant {
	
	public static final int DEFAULT_POPULATION = 500;
	public static final int DEFAULT_MAX_GENERATION = 10;
	public static final double DEFAULT_MUTATION_RATIO = 0.01;
	public static final double DEFAULT_EXCHANGE_RATIO = 0.5;

	public static class ConfigurationKeys{
		public static final String SOLUTION_RANGES = "solutionRanges";
		public static final String SOLVING_FACTORS = "solvingFactors";
		public static final String FACTOR_LIMIT = "factorLimit";
		public static final String TARGET_RESULT = "targetResult";
		public static final String GA_POPULATIONS = "gaPopulation";
		public static final String GA_MAX_GENERATION = "maxGeneration";
		public static final String GA_MUTATION_RATIO = "mutationRatio";
		public static final String GA_EXCHANGE_RATIO = "exchangeRatio";
	}

}
