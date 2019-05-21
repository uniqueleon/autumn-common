package org.aztec.autumn.common.algorithm.genetic.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.Selector;
import org.aztec.autumn.common.algorithm.genetic.SelectorException;

import com.google.common.collect.Lists;

public class RouletteDoubleSelector implements Selector {

	public RouletteDoubleSelector() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Individual[] select(Individual[] individuals, Evaluator evaluators)
			throws SelectorException {
		List<Score> scores = init(individuals, evaluators);
		changeToPositive(scores);
		caculateRatio(scores);
		List<Individual> elites = rouletteGamble(scores);
		return elites.toArray(new Individual[elites.size()]);
	}

	public List<Score> init(Individual[] individuals, Evaluator evaluators)
			throws SelectorException {
		List<Score> scores = new ArrayList<Score>();
		for (Individual individual : individuals) {
			double score = evaluators.evaluateAsDouble(individual);
			scores.add(new Score(score, individual));
		}
		Collections.sort(scores);
		return scores;
	}

	public void changeToPositive(List<Score> scores) {
		double minumValue = scores.get(0).get();
		if (minumValue < 0) {
			for (int i = 0; i < scores.size(); i++) {
				scores.get(i).set(scores.get(i).get() - minumValue);
			}
		}
	}

	public void caculateRatio(List<Score> scores) {
		double totalScore = 0;
		List<Score> tmpScores = Lists.newArrayList();
		for (Score score : scores) {
			totalScore += score.get();
		}
		double tmpScore = 0;
		for (int i = 0; i < scores.size(); i++) {
			double ratio = scores.get(i).get() / totalScore;
			tmpScore += ratio; 
			tmpScores.add(new Score(tmpScore, scores.get(i).getIndividual()));
			//score.set(score.get() / totalScore);
		}
		scores.clear();
		scores.addAll(tmpScores);
	}

	public List<Individual> rouletteGamble(List<Score> scores) {
		List<Individual> elites = new ArrayList<Individual>();
		Random random = new Random();
		for (int i = 0; i < scores.size(); i++) {
			double randomNum = random.nextDouble();
			int position = searchPosition(randomNum, scores);
			Score currentScore = scores.get(position);
			if (!elites.contains(currentScore.getIndividual())){
				elites.add(currentScore.getIndividual());
			}
			/*for (int j = 0; j < scores.size(); j++) {
				Score currentScore = scores.get(j);
				if (currentScore.get() < randomNum) {
					if (j == scores.size() - 1) {
						if (!elites.contains(currentScore.getIndividual()))
							elites.add(currentScore.getIndividual());
					} else {
						if (randomNum < scores.get(j + 1).get()) {
							if (!elites.contains(currentScore.getIndividual()))
								elites.add(currentScore.getIndividual());
						}
					}
				}
			}*/
		}
		return elites;
	}
	
	private static int searchPosition(double random,List<Score> scores){
		int beginIndex = 0;
		int endIndex = scores.size();
		while(beginIndex !=  endIndex - 1){
			int tmpEnd = (endIndex - beginIndex) / 2;
			if(scores.get((beginIndex + tmpEnd)).get() >= random){
				endIndex -= tmpEnd;
			}
			else{
				beginIndex += tmpEnd;
			}
		}
		//System.out.println("RANDOM : " + random + " - [" + scores.get(beginIndex).get() + "," + scores.get(beginIndex + 1).get() + "]");
		return beginIndex;
	}

	private static class Score implements Comparable<Score> {
		double score;
		Individual individual;

		public Score(double index, Individual individual) {
			this.score = index;
			this.individual = individual;
		}

		public void set(double score) {
			this.score = score;
		}

		public double get() {
			return score;
		}

		public Individual getIndividual() {
			return individual;
		}

		@Override
		public int compareTo(Score o) {
			if (score - o.get() == 0)
				return 0;
			return score - o.get() > 0 ? 1 : -1;
		}

	}

}
