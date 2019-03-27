package org.aztec.autumn.common.algorithm;

import java.util.Map;

public interface Progress {

  public double getRate();
  public Map<String,Object> getAsMap();
  public AlgorithmPhase[] getPhases();
  public AlgorithmPhase getCurrentPhase();
}
