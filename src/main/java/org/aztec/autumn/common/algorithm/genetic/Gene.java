package org.aztec.autumn.common.algorithm.genetic;

public interface Gene {

  public double getMutationRate();
  public Gene mutate();
  public <T> T get();
  public Gene clone() throws CloneNotSupportedException;
}
