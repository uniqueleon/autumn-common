package org.aztec.autumn.common.algorithm.genetic;

import org.aztec.autumn.common.algorithm.Result;

public interface Individual extends Result{

  public Gene[] getGenes();
  public void mutate();
  public Individual couple(Individual individual);
  public <T> T adapt(Class<T> targetCls);
  public Integer hash();
}
