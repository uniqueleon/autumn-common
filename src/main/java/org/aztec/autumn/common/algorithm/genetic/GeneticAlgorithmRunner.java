package org.aztec.autumn.common.algorithm.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.algorithm.AlgorithmException;
import org.aztec.autumn.common.algorithm.AlgorithmMonitor;
import org.aztec.autumn.common.algorithm.AlgorithmPhase;
import org.aztec.autumn.common.algorithm.AlgorithmRunner;
import org.aztec.autumn.common.algorithm.Progress;
import org.aztec.autumn.common.algorithm.Result;
import org.aztec.autumn.common.algorithm.genetic.advance.Classifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneticAlgorithmRunner implements AlgorithmRunner,AlgorithmMonitor,Progress{

  private Chaos chaos;
  private int currentGeneration;
  private GeneticAlgorithmPhases currentPhase;
  private static Logger LOG = LoggerFactory.getLogger(GeneticAlgorithmRunner.class);
  
  public GeneticAlgorithmRunner(Chaos chaos) {
    this.chaos = chaos;
  }
  

  @Override
  public void run() {
    try {
      execute();
    } catch (GeneticAlgorithmException e) {
      LOG.error(e.getMessage(),e);
    }
  }

  @Override
  public void execute() throws GeneticAlgorithmException {
    
    Individual[] generation = init();
    for(int currentGeneration = 0;currentGeneration < chaos.getMaximumGeneration();currentGeneration++){
      LOG.info("Generating " + currentGeneration + "th population!");
      if(chaos.isFinished(generation))
    	  break;
      generation = nextGeneration(generation);
      LOG.info("Generate finished!!");
    }
  }
  
  private Individual[] init(){
    currentPhase = GeneticAlgorithmPhases.INITIALIZE;
    Individual[] firstGeneration = chaos.generate();
    Classifier classifier = chaos.getClassifier();
    LOG.info("Generation first generation!");
    if(classifier != null){
      while(classifier.getGeneDistribution(firstGeneration) < classifier.getMinimumDistribution()){
        firstGeneration = chaos.generate();
      }
    }
    return firstGeneration;
  }
  
  private Individual[] nextGeneration(Individual[] population) throws GeneticAlgorithmException{
	Long curTime = System.currentTimeMillis();
    population = select(population);
    LOG.debug("select use:" + (System.currentTimeMillis() - curTime));
	curTime = System.currentTimeMillis();
    population = couple(population);
    LOG.debug("couple use:" + (System.currentTimeMillis() - curTime));
	curTime = System.currentTimeMillis();
    population = rebuildPopulation(population);
    LOG.debug("rebuild use:" + (System.currentTimeMillis() - curTime));
	curTime = System.currentTimeMillis();
    return population;
  }
  
  private Individual[] couple(Individual[] population){
    currentPhase = GeneticAlgorithmPhases.COUPLE;
    List<Individual> newIndividuals = new ArrayList<Individual>();
    Map<Integer,Individual> selectedIndividuals = new HashMap<Integer,Individual>();
    for(int i = 0;i < population.length;i++){
      if(selectedIndividuals.containsKey(i))
        continue;
      //闂冨弶顒涘璇叉儕閻滐拷
      if(population.length % 2 == 1 
          && selectedIndividuals.size() != 1 
          && population.length - selectedIndividuals.size() == 1){
        newIndividuals.add(population[i]);
        break;
      }
      selectedIndividuals.put(i, population[i]);
      Integer randomIndex = RandomUtils.nextInt(population.length);
      while(selectedIndividuals.containsKey(randomIndex))
        randomIndex = RandomUtils.nextInt(population.length);
      selectedIndividuals.put(randomIndex, population[randomIndex]);
      Individual individual = population[i];
      Individual otherIndividual = population[randomIndex];
      newIndividuals.add(individual);
      newIndividuals.add(otherIndividual);
      Individual child = individual.couple(otherIndividual);
      if(child != null) {
          child.mutate();
          newIndividuals.add(child);
      }
    }
    return newIndividuals.toArray(new Individual[newIndividuals.size()]);
  }
  
  private Individual[] select(Individual[] population) throws GeneticAlgorithmException{

    currentPhase = GeneticAlgorithmPhases.SELECT;
    Selector selector = chaos.getSelector();
    return selector.select(population, chaos.getEvaluator());
  }

  private Individual[] rebuildPopulation(Individual[] population){
    Classifier classifier = chaos.getClassifier();
    Individual[] newPopulation = population.clone();
    if(classifier != null){
      while(classifier.getGeneDistribution(newPopulation) < classifier.getMinimumDistribution()){
        newPopulation = chaos.generateNext(newPopulation);
      }
    }
    else if(newPopulation.length < chaos.getPropulation()){
      newPopulation = chaos.generateNext(newPopulation);
    }
    return newPopulation;
  }


  @Override
  public Result getResult() throws AlgorithmException {
    return chaos.getElite();
  }


  @Override
  public boolean hasSolved() throws AlgorithmException {
    
    return currentGeneration < chaos.getMaximumGeneration();
  }


  @Override
  public AlgorithmMonitor getMonitor() throws AlgorithmException {
    return this;
  }


  @Override
  public double getRate() {
    double rate = 0;
    if(!currentPhase.equals(GeneticAlgorithmPhases.INITIALIZE))
      rate += 0.2;
    return rate += (currentGeneration / chaos.getMaximumGeneration()) * 0.8;
  }


  @Override
  public Map<String, Object> getAsMap() {
    Map<String,Object> progress = new HashMap<String,Object>();
    progress.put(Constants.PROGRESS_RATIO_KEY, getRate());
    progress.put(Constants.PROGRESS_PHASE_KEY, currentPhase.getPhase().name());
    return progress;
  }


  @Override
  public AlgorithmPhase[] getPhases() {
    List<AlgorithmPhase> phaseList = new ArrayList<AlgorithmPhase>();
    for(GeneticAlgorithmPhases phase : GeneticAlgorithmPhases.values()){
      phaseList.add(phase.getPhase());
    }
    return phaseList.toArray(new AlgorithmPhase[phaseList.size()]);
  }


  @Override
  public AlgorithmPhase getCurrentPhase() {
    return currentPhase.getPhase();
  }


  @Override
  public Progress getProgress() {
    return this;
  }

  
}
