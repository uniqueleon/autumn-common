package org.aztec.autumn.common.math.equations;

import java.util.HashMap;
import java.util.Map;

import org.aztec.autumn.common.algorithm.genetic.GeneGenerator;
import org.aztec.autumn.common.algorithm.genetic.GeneticAlgorithmRunner;
import org.aztec.autumn.common.algorithm.genetic.TranningConfig;
import org.aztec.autumn.common.algorithm.genetic.impl.SimpleTranningConfig;
import org.aztec.autumn.common.math.equations.impl.ga.DEEvaluator;
import org.aztec.autumn.common.math.equations.impl.ga.DEGeneGenerator;
import org.aztec.autumn.common.math.equations.impl.ga.DEIndividualGenerator;
import org.aztec.autumn.common.math.equations.impl.ga.DiophatineEquationIndividual;
import org.aztec.autumn.common.math.equations.impl.ga.IDE_Chaos;
import org.aztec.autumn.common.math.equations.impl.ga.IDE_GA_Constant;
import org.aztec.autumn.common.math.equations.impl.ga.IDE_GA_Constant.ConfigurationKeys;

public class IndefinteDiophantineEquation {

	public IndefinteDiophantineEquation() {
		// TODO Auto-generated constructor stub
	}
	
	public static IDEquationResult solve(Long[][] factors,Long[][] ranges,Long result,Map<String,Object> params) {
		
		//IDEquationResult result = new IDEquationResult(result, factors, solutions);
		//ChaosBuilder.builder();
		
		int population = (int) (params.get(ConfigurationKeys.GA_POPULATIONS) == null
				? IDE_GA_Constant.DEFAULT_POPULATION
				: params.get(ConfigurationKeys.GA_POPULATIONS));
		int maxGeneration = (int) (params.get(ConfigurationKeys.GA_MAX_GENERATION) == null
				? IDE_GA_Constant.DEFAULT_MAX_GENERATION
				: params.get(ConfigurationKeys.GA_MAX_GENERATION));
		double mutationRate = (double) (params.get(ConfigurationKeys.GA_MUTATION_RATIO) == null
				? IDE_GA_Constant.DEFAULT_MUTATION_RATIO
				: params.get(ConfigurationKeys.GA_MUTATION_RATIO));
		double exchangeRate = (double) (params.get(ConfigurationKeys.GA_EXCHANGE_RATIO) == null
				? IDE_GA_Constant.DEFAULT_EXCHANGE_RATIO
				: params.get(ConfigurationKeys.GA_EXCHANGE_RATIO));
		params.put(ConfigurationKeys.SOLUTION_RANGES, ranges);
		params.put(ConfigurationKeys.TARGET_RESULT, result);
		params.put(ConfigurationKeys.SOLVING_FACTORS, factors);
		TranningConfig config = new SimpleTranningConfig(population, maxGeneration, mutationRate, exchangeRate,
				factors.length, 1d,1d / result,params);
		GeneGenerator geneGenerator = new DEGeneGenerator(config);
		IDE_Chaos chaos = new IDE_Chaos(config, new DEIndividualGenerator(geneGenerator, config),
				new DEEvaluator(config));
		GeneticAlgorithmRunner runner = new GeneticAlgorithmRunner(
				chaos);
		runner.run();
		DiophatineEquationIndividual elite = (DiophatineEquationIndividual) chaos.getElite();
		if(elite == null)
			return null;
		return new IDEquationResult(elite.getResult(), elite.getFactors(), elite.getSolution());
	}
	
	
	public static class IDEquationResult {
		
		private Long result;
		private Long[] factors;
		private Long[] solutions;
		public Long getResult() {
			return result;
		}
		public void setResult(Long result) {
			this.result = result;
		}
		public Long[] getFactors() {
			return factors;
		}
		public void setFactors(Long[] factors) {
			this.factors = factors;
		}
		public Long[] getSolutions() {
			return solutions;
		}
		public void setSolutions(Long[] solutions) {
			this.solutions = solutions;
		}
		public IDEquationResult(Long result, Long[] factors, Long[] solutions) {
			super();
			this.result = result;
			this.factors = factors;
			this.solutions = solutions;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("IDEquationResult[");
			for(int i = 0;i < factors.length;i++) {
				if(i != 0) {
					builder.append(" + ");
				}
				builder.append(solutions[i] + " * " + factors[i]);
			}
			builder.append(" = " + result + "]");
			return builder.toString();
		}
		
	}
	
	public static void main(String[] args) {
		
		Map<String,Object> params = new HashMap<>();
		
		Long curTime = System.currentTimeMillis();
		/*params.put(ConfigurationKeys.FACTOR_LIMIT, new Long[] {120l,80l,90l});
		 * IDEquationResult result = IndefinteDiophantineEquation.solve(new Long[][] {
			{3l,7l,6l},{4l,8l,9l},{5l,3l,2l},{10l,11l,7l},{120l,70l,60l}
		}, new Long[][] {{0l,10l},{0l,10l},{0l,10l},{0l,10l},{0l,10l}}, 1293l, params);*/
		/*params.put(ConfigurationKeys.FACTOR_LIMIT, new Long[] {105l,190l,160l});
		IDEquationResult result = IndefinteDiophantineEquation.solve(new Long[][] {
			{60l,160l,160l},{39l,60l,105l}
		}, new Long[][] {{0l,10l},{0l,10l}}, 105l, params);*/
		params.put(ConfigurationKeys.FACTOR_LIMIT, new Long[] {130l,150l,160l});
		IDEquationResult result = IndefinteDiophantineEquation.solve(new Long[][] {
			{10l,51l,141l},{39l,60l,105l},{70l,110l,105l}
		}, new Long[][] {{0l,10l},{0l,10l},{0l,10l}}, 130l, params);
		Long elapseTime = System.currentTimeMillis() - curTime;
		System.out.println(result);
		System.out.println("use time : " + elapseTime);
	}

}
