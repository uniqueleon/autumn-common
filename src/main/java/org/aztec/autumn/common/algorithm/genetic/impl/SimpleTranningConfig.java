package org.aztec.autumn.common.algorithm.genetic.impl;

import java.util.Map;

import org.aztec.autumn.common.algorithm.genetic.TranningConfig;

public class SimpleTranningConfig implements TranningConfig{

	protected int populations;
	protected int maxGeneration;
	protected double mutationRate;
	protected double exchangeRate;
	protected int genesNum = 1;
	protected Map<String,Object> parameters;
	protected double targetScore;
	protected double error;


	public SimpleTranningConfig(int populations, int maxGeneration, double mutationRate,
			double exchangeRate,int genesNum, double targetScore,double error,Map<String, Object> parameters) {
		super();
		this.populations = populations;
		this.maxGeneration = maxGeneration;
		this.mutationRate = mutationRate;
		this.exchangeRate = exchangeRate;
		this.genesNum = genesNum;
		this.parameters = parameters;
		this.targetScore = targetScore;
		this.error = error;
	}

	public int getPopulations() {
		return populations;
	}

	public void setPopulations(int populations) {
		this.populations = populations;
	}


	public double getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public void setMaxGeneration(int maxGeneration) {
		this.maxGeneration = maxGeneration;
	}

	public void setGenesNum(int genesNum) {
		this.genesNum = genesNum;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	@Override
	public double getMutationRatio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGenesNum() {
		// TODO Auto-generated method stub
		return genesNum;
	}

	@Override
	public double getExchangeRatio() {
		// TODO Auto-generated method stub
		return exchangeRate;
	}

	@Override
	public int getMaxGeneration() {
		// TODO Auto-generated method stub
		return maxGeneration;
	}

	@Override
	public int getPopulation() {
		// TODO Auto-generated method stub
		return populations;
	}

	@Override
	public Map<String, Object> getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	@Override
	public double getAcceptableScore() {
		return error;
	}

	@Override
	public double getTargetScore() {
		// TODO Auto-generated method stub
		return targetScore;
	}

	@Override
	public double getCoupleRatio() {
		return exchangeRate;
	}

}
