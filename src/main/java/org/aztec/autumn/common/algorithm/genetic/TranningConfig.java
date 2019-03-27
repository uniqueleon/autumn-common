package org.aztec.autumn.common.algorithm.genetic;

import java.util.Map;

public interface TranningConfig  {


	public double getMutationRatio();
	public double getCoupleRatio();
	public int getGenesNum();
	public double getExchangeRatio();
	public int getMaxGeneration();
	public int getPopulation();
	public Map<String,Object> getParameters();
	public double getAcceptableScore();
	public double getTargetScore();
}
