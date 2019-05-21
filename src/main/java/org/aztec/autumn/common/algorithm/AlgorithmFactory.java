package org.aztec.autumn.common.algorithm;

public interface AlgorithmFactory {

  public AlgorithmRunner getRunner(String type,Object... initParams);

  public AlgorithmRunner getRunnerInOSGi(String type,Object... initParams);
  
  public Thread getRunnerThread(String type,Object... initParams);
}
