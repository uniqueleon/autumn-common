package org.aztec.autumn.common.algorithm;

public enum AlgorithmType {

  GA("genetic_algorithm",new String[]{"GA"}),
  NUMERIC_GA("numeric_ga",new String[]{"NUMERIC_GA"}),
  NEURAL_NETWORK("neural_network",new String[]{"NN"});
  
  private String name;
  private String[] alias;

  private AlgorithmType(String name,String[] alias) {
    this.name = name;
    this.alias = alias;
  }
  
}
