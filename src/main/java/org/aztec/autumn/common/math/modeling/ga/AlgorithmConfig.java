package org.aztec.autumn.common.math.modeling.ga;

public class AlgorithmConfig {

	private double mutationRatio = 0.1;
	private double coupleRatio = 0.5;
	private static final AlgorithmConfig singleton = new AlgorithmConfig();
	
	private AlgorithmConfig(){
		
	}
	
	public static AlgorithmConfig getInstance(){
		return singleton;
	}

	public double getMutationRatio() {
		return mutationRatio;
	}

	public void setMutationRatio(double mutationRatio) {
		this.mutationRatio = mutationRatio;
	}

	public double getCoupleRatio() {
		return coupleRatio;
	}

	public void setCoupleRatio(double coupleRatio) {
		this.coupleRatio = coupleRatio;
	}
	
}
