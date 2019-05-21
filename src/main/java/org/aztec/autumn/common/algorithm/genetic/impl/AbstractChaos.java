package org.aztec.autumn.common.algorithm.genetic.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.aztec.autumn.common.algorithm.genetic.Chaos;
import org.aztec.autumn.common.algorithm.genetic.Evaluator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.IndividualGenerator;
import org.aztec.autumn.common.algorithm.genetic.Selector;
import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;
import org.aztec.autumn.common.math.equations.SortableNumber;
import org.aztec.autumn.common.math.equations.SortableNumber.Ordering;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public abstract class AbstractChaos implements Chaos {

	protected Selector selector;
	protected Classifier classifier;
	protected Evaluator evaluator;
	protected IndividualGenerator individualGenerator;
	protected int populations;
	protected int maxGeneration;
	protected int currentGeneration;
	protected int retryTime = 3;
	protected int candidateSize = 10;
	protected Individual elite;
	protected Individual[] elites;
	protected boolean finished = false;
	private Map<Integer,Individual> pool = Maps.newConcurrentMap();
	private Queue<Individual> elitesQueue = Queues.newArrayBlockingQueue(candidateSize);

	public AbstractChaos(IndividualGenerator generator, Evaluator evaluator,
			Selector selector) {
		this.selector = selector;
		this.individualGenerator = generator;
		this.evaluator = evaluator;
	}

	public AbstractChaos(IndividualGenerator generator, Evaluator evaluator,
			Selector selector, int maxGeneration, int populations) {
		this.selector = selector;
		this.individualGenerator = generator;
		this.evaluator = evaluator;
		this.maxGeneration = maxGeneration;
		this.populations = populations;
	}

	@Override
	public int getMaximumGeneration() {
		return maxGeneration;
	}

	@Override
	public int getPropulation() {
		return populations;
	}

	@Override
	public Individual[] generate() {
		elites = new Individual[populations];
		for (int i = 0; i < populations; i++) {
			elites[i] = generateNewOne();
			if (elites[i] == null) {
				finished = true;
				break;
			}
			if (evaluator.isSatisfied(elites[i])) {
				this.elite = elites[i];
				break;
			}
		}
		return elites;
	}
	
	private Individual generateNewOne(){
		Individual newOne = individualGenerator.generate();
		int tmpTry = retryTime;
		while(pool.containsKey(newOne.hashCode())){
			newOne = individualGenerator.generate();
			tmpTry -- ;
			if(tmpTry < 0){
				return getEliteFromPool();
			}
		}
		pool.put(newOne.hash(), newOne);
		return newOne;
	}
	
	private Individual getEliteFromPool(){
		if(elitesQueue.size() == 0){
			List<SortableNumber> scores = Lists.newArrayList();
			for(Entry<Integer,Individual> ind : pool.entrySet()){
				scores.add(new SortableNumber(evaluator.evaluateAsDouble(ind.getValue()), ind.getKey()));
			}
			SortableNumber.sort(scores, Ordering.DESC);
			int pickupSize = candidateSize > scores.size() ? scores.size() : candidateSize;
			for(int i = 0;i < pickupSize;i++){
				if(pool.get(scores.get(i).getIndex()) != null){
					elitesQueue.add(pool.get(scores.get(i).getIndex()));
				}
			}
		}
		return elitesQueue.size() > 0 ? elitesQueue.poll() : null;
	}
	

	@Override
	public Individual[] generateNext(Individual[] individuals) {

		elites = new Individual[populations];
		for (int i = 0; i < populations; i++) {
			if (i < individuals.length) {
				elites[i] = individuals[i];
			} else {
				elites[i] = generateNewOne();
				if(elites[i] == null){
					finished = true;
					break;
				}
			}
			if (evaluator.isSatisfied(elites[i])) {
				elite = elites[i];
				break;
			}
		}
		return elites;
	}

	@Override
	public Selector getSelector() {
		return selector;
	}

	@Override
	public Evaluator getEvaluator() {
		return evaluator;
	}

	@Override
	public Classifier getClassifier() {
		return classifier;
	}

	@Override
	public boolean isFinished(Individual[] indiviuduals) {
		if(elite != null && evaluator.isSatisfied(elite)){
			return true;
		}
		for (Individual ind : indiviuduals) {
			if (evaluator.isSatisfied(ind)) {
				elite = ind;
				return true;
			}
		}
		return currentGeneration > maxGeneration;
	}

	@Override
	public Individual[] getElites() {
		return elites;
		// return selector.;
	}

	@Override
	public Individual getElite() {
		if (elite == null) {
			double maxScore = -Double.MAX_VALUE;
			int eliteIndex = -1;
			for (int i = 0; i < elites.length; i++) {
				Individual individual = elites[i];
				if(individual == null){
					continue;
				}
				double score = evaluator.evaluateAsDouble(individual);
				if (maxScore < evaluator.evaluateAsDouble(individual)) {
					maxScore = score;
					eliteIndex = i;
				}
			}
			elite = elites[eliteIndex];
		}
		return elite;
	}

	@Override
	public void setElites(Individual[] indiviuduals) {
		this.elites = indiviuduals;
	}

}
