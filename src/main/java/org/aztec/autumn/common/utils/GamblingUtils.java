package org.aztec.autumn.common.utils;

import org.apache.commons.lang.math.RandomUtils;

public class GamblingUtils {

	public GamblingUtils() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static double successiveGambling(int base,Double winRatio,Double totalBet,Double lowerlimit,Double upperLimit) {
		
		Double score = new Double(totalBet);
		Double testValue = RandomUtils.nextDouble();
		Double bet = totalBet >= lowerlimit ? lowerlimit : 0;
		int times = 0;
		while(bet > 0) {
			if(testValue < winRatio) {
				score += bet;
				if(score > totalBet) {
					return score;
				}
			}
			else {
				score -= bet;
			}
			times ++;
			Double nextBet = lowerlimit * Math.pow(base, times);
			if(nextBet > upperLimit) {
				nextBet = upperLimit;
			}
			bet = score > nextBet ? nextBet : score;
			testValue = RandomUtils.nextDouble();
		}
		return score;
	}
	
	public static double getSuccessiveGamblingRatio(int base,Double winRatio,Double totalBet,Double lowerlimit,Double upperLimit) {
		
		Double finalBet = totalBet >= upperLimit ? upperLimit : totalBet;
		int times = 0;
		Double tmpBet = lowerlimit;
		while(tmpBet <= finalBet) {
			times ++;
			tmpBet *= base;
		}
		Double ratio = 1d;
		for(int i =0 ;i < times;i++) {
			ratio *= (1 - winRatio);
		}
		return (1 - ratio);
	}

	public static void main(String[] args) {
		for(int i = 0;i < 100;i++) {
			System.out.println(successiveGambling(3,0.5,20d,1d,100d));
		}
		System.out.println(getSuccessiveGamblingRatio(3, 0.5, 20d, 1d, 100d));
	}
}
