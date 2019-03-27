package org.aztec.autumn.common.algorithm;

import org.aztec.autumn.common.algorithm.genetic.Chaos;
import org.aztec.autumn.common.algorithm.genetic.GeneticAlgorithmRunner;
import org.aztec.autumn.common.algorithm.genetic.impl.NumericChaos;
import org.aztec.autumn.common.algorithm.genetic.impl.NumericChaosConfiguration;

public class DefaultFactory implements AlgorithmFactory {

  public DefaultFactory() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public AlgorithmRunner getRunner(String type, Object... initParams) {
    AlgorithmType aType = AlgorithmType.valueOf(type);
    switch(aType){
      case GA: 
        break;
      case NUMERIC_GA:
        Chaos chaos = new NumericChaos(new NumericChaosConfiguration(initParams));
        return new GeneticAlgorithmRunner(chaos);
      case NEURAL_NETWORK:
        break;
      default:
        return null;
    }
    return null;
  }

  @Override
  public AlgorithmRunner getRunnerInOSGi(String type,
      Object... initParams) {
    AlgorithmType aType = AlgorithmType.valueOf(type);
    switch(aType){
      case GA: 
        break;
      case NUMERIC_GA:
        Chaos chaos = new NumericChaos(new NumericChaosConfiguration(initParams));
        return new GeneticAlgorithmRunner(chaos);
      case NEURAL_NETWORK:
        break;
      default:
        return null;
    }
    return null;
  }

  @Override
  public Thread getRunnerThread(String type, Object... initParams) {
    return new Thread(getRunner(type, initParams));
  }

}
