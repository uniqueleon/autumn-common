package org.aztec.autumn.common.math.modeling.ga.evaluator;

import java.util.List;

import org.aztec.autumn.common.math.modeling.ga.SolutionEvaluator;

import com.google.common.collect.Lists;

public class SolutionEvaluatorFactory {
	
	private static final SolutionEvaluatorFactory factory = new SolutionEvaluatorFactory();
	
	private List<SolutionEvaluator> registedEvaluators = Lists.newArrayList();
	
	private SolutionEvaluatorFactory(){
		registedEvaluators.add(new FillRatioEvaluator());
	}
	
	public static SolutionEvaluatorFactory getInstance(){
		return factory;
	}
	
	public void regist(SolutionEvaluator solutionEvaluator){
		registedEvaluators.add(solutionEvaluator);
	}
	
	public List<SolutionEvaluator> getEvaluators(){
		return registedEvaluators;
	}
	
}
