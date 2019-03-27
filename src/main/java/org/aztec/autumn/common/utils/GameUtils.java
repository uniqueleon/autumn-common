package org.aztec.autumn.common.utils;

import java.util.List;

import org.aztec.autumn.common.utils.security.rsa.RSAEncrypt;

import com.google.common.collect.Lists;

public class GameUtils {
	
	public static double getRankProfit(int n,int rank,double power,double ratio) {

		double bonus = n * (1d / Math.pow(rank, power)) * ratio;
		return bonus;
	}

	public static double sumOfHarmonicProgression(int n, double power,double ratio,double bet) {
		double result = 0;
		List<Double> profits = getBonus(n, ratio, power);
		for(int i = 0;i < profits.size() ;i ++) {
			result += profits.get(i);
		}
		return result;
	}
	
	public static double calculateHarmonicGameProfit(int n, double power,double ratio,double bet) {
		double sumOfHar = sumOfHarmonicProgression(n,power,ratio,bet);
		return (bet * n) - sumOfHar;
	}
	
	public static int winCount(int n, double bet, double ratio,double power) {
		for(int i = 1;i <= n;i++) {
			double bonus = getRankProfit(n, i, power, ratio);
			if(bonus < bet) {
				return i;
			}
		}
		return -1;
	}
	
	public static List<Double> getBonus(int n, double ratio,double power){
		List<Double> bonuses = Lists.newArrayList();
		for(int i = 1;i <= n;i++) {
			double bonus = getRankProfit(n, i, power, ratio);
			bonuses.add(bonus);
		}
		return bonuses;
	}
	
	public static double calculateScore(long refTime,long dealTime,long useTime,double[] efficients) {
		double base = efficients[0] * (refTime - dealTime);
		
		base *= efficients[1] * (1d / useTime);
		return base;
		
	}

	public GameUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		
		Long curTime = System.currentTimeMillis();
		double[] effectors = new double[] {1 / 1e3 * 60 * 60,1};
		
		System.out.println(effectors[0] + " == " + effectors[1]);
		System.out.println(calculateScore(curTime, (curTime - 10000), 10000,effectors));
		System.out.println(calculateScore(curTime, (curTime - 60 * 1000), 2000,effectors));
		System.out.println(calculateScore(curTime, (curTime - 24 * 60 * 60 * 1000), 10000,effectors));

		System.out.println(calculateScore(curTime, (curTime - 12 * 60 * 60 * 1000), 2000,effectors));
		System.out.println(calculateScore(curTime, (curTime - 12 * 60 * 60 * 1000), 5000,effectors));

		System.out.println(calculateScore(curTime, (curTime - 24 * 60 * 59 * 1000), 10000,effectors));
		int num = 100;
		double ratio = 1d;
		double power = 0.6;
		double bet = 1.0;
		double sumOfHar = GameUtils.sumOfHarmonicProgression(num,power,ratio,bet);
		// 14元
		System.out.println(sumOfHar);
		System.out.println(GameUtils.calculateHarmonicGameProfit(num, power, ratio, bet));
		System.out.println(GameUtils.winCount(num, bet,ratio, power));
		System.out.println(GameUtils.getBonus(num, ratio, power));
		for(int i = 1;i <= 100;i++) {
			System.out.println(">>>>>>>>>>ORDER NUM[" + i + "]<<<<<<<<<<<<<<");
			System.out.println("1st:" + getRankProfit(i, 1, power, ratio));
			System.out.println("2nd:" + getRankProfit(i, 2, power, ratio));
			System.out.println("3rd:" + getRankProfit(i, 3, power, ratio));

			System.out.println(">>>>>>>>>>ORDER NUM[" + i + "]<<<<<<<<<<<<<<");
		}
	}
}
