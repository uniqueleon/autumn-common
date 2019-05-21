package org.aztec.autumn.common.algorithm.genetic;

import org.aztec.autumn.common.algorithm.AlgorithmPhase;

public enum GeneticAlgorithmPhases {

  INITIALIZE("initialize"),
  SELECT("select"),
  COUPLE("couple"),
  MUTATION("mutation");

  AlgorithmPhase phase;

  private GeneticAlgorithmPhases(String name) {
    phase = new GAPhase(name);
  }
  
  public AlgorithmPhase getPhase(){
    return phase;
  }

  public static class GAPhase implements AlgorithmPhase{

    String name = "";
    
    public GAPhase(String name) {
      super();
      this.name = name;
    }
    
    @Override
    public String name() {
      return name;
    }
    
  }
}
