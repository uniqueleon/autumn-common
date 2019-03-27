package org.aztec.autumn.common.algorithm.genetic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.algorithm.genetic.Constants;
import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.Individual;

public class NumericVectorIndividual implements Individual {

  private Gene[] genes;
  public static final double DEFAULT_EXCHANGE_RATIO = 0.5;
  
  public NumericVectorIndividual(int size,long[] range) {
	  
  }
  
  public NumericVectorIndividual(int size,long seed){
    genes = new Gene[size];
    for(int i = 0;i < size;i++){
      genes[i] = new NumericGene<Double>(seed,Double.class);
    }
  }
  
  public NumericVectorIndividual(int size,long seed,Class geneCls){
    genes = new Gene[size];
    for(int i = 0;i < size;i++){
      genes[i] = new NumericGene<Double>(seed,geneCls);
    }
  }
  
  public NumericVectorIndividual(double[] values) {
    genes = new Gene[values.length];
    for(int i = 0;i < genes.length;i++){
      genes[i] = new NumericGene<Double>(values[i],Double.class);
    }
  }

  @Override
  public Gene[] getGenes() {
    return genes;
  }

  @Override
  public void mutate() {
    for(int i = 0;i < genes.length;i++){
      double randomNum = RandomUtils.nextDouble();
      Gene gene = genes[i];
      if(randomNum < gene.getMutationRate()){
        genes[i] = gene.mutate();
      }
    }
  }

  @Override
  public Individual couple(Individual individual) {
    double[] newIndividualGenes = new double[genes.length];
    if(genes.length == 1){
      newIndividualGenes = new double[]{(new Double(genes[0].get().toString()) + (double)individual.getGenes()[0].get()) /2 };
    }
    else{
      for(int i = 0;i < genes.length;i++){
        double randomNum = RandomUtils.nextDouble();
        if(randomNum > DEFAULT_EXCHANGE_RATIO)
          newIndividualGenes[i] = (double)genes[i].get();
        else
          newIndividualGenes[i] = (double)individual.getGenes()[i].get();
      }
    }
    return new NumericVectorIndividual(newIndividualGenes);
  }

  public static void main(String[] args) {
  }

  @Override
  public Object get() {
    return this;
  }

  @Override
  public Map<String, Object> getAsMap() {
    List geneList = new ArrayList();
    for(Gene gene : genes){
      geneList.add(gene);
    }
    Map<String,Object> result = new HashMap<>();
    result.put(Constants.RESULT_GENES_KEY, geneList);
    return result;
  }

  @Override
  public <T> T adapt(Class<T> targetCls) {
	if(targetCls.isAssignableFrom(NumericVectorIndividual.class))
		return (T) this;
	return null;
  }
}
