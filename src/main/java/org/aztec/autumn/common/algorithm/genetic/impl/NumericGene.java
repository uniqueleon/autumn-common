package org.aztec.autumn.common.algorithm.genetic.impl;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.algorithm.genetic.Gene;

public class NumericGene<T> implements Gene {
  
  private double gene;
  public static double DEFAULT_MUTATION_RATE = 0.5;
  private long seed = 1;
  Class<T> geneCls;
  //private Random random = new Random();

  public NumericGene(long seed,Class<T> geneCls) {
    this.geneCls = geneCls;
    this.seed = seed;
    this.gene = seed * RandomUtils.nextDouble();
    regenerate();
  }
  
  public NumericGene(Class<T> geneCls) {
    this.geneCls = geneCls;
    this.gene = RandomUtils.nextDouble();
    regenerate();
  }

  public NumericGene(double geneValue,Class<T> geneCls) {
    this.geneCls = geneCls;
    this.gene = geneValue;
    regenerate();
  }
  
  private void regenerate(){

    if(geneCls.equals(int.class) || geneCls.equals(Integer.class)){
      if(Math.abs(gene) < Integer.MAX_VALUE){
        gene = new Integer(Math.round(gene) + "");
      }
      else {
        gene = RandomUtils.nextInt();
      }
    }
    else if(geneCls.equals(long.class) || geneCls.equals(Long.class)){
      gene = Math.round(gene);
    }
    else if(geneCls.equals(float.class) || geneCls.equals(Float.class)){
      if(Math.abs(gene) > Float.MAX_VALUE){
        gene = RandomUtils.nextFloat();
      }
    }
  }

  @Override
  public double getMutationRate() {
    return 0.5;
  }

  @Override
  public Gene mutate() {
    return new NumericGene<T>(seed * RandomUtils.nextDouble(),geneCls);
  }

  @Override
  public T get() {
    if(geneCls.equals(int.class) || geneCls.equals(Integer.class))
      return (T) new Integer("" + Math.round(gene));
    else if(geneCls.equals(long.class) || geneCls.equals(Long.class))
      return (T) new Long("" + Math.round(gene));
    else if(geneCls.equals(double.class) || geneCls.equals(Double.class))
      return (T) new Double("" + gene);
    else if(geneCls.equals(float.class) || geneCls.equals(Float.class))
      return (T) new Float("" + gene);
    return null;
  }

  @Override
  public String toString() {
    return "NumericGene [gene=" + gene + ", seed=" + seed + "]";
  }

	@Override
	public Gene clone() throws CloneNotSupportedException {
		return null;
	}

}
