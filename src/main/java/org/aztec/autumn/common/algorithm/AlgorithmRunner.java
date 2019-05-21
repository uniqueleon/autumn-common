package org.aztec.autumn.common.algorithm;

public interface AlgorithmRunner extends Runnable{

  public void execute() throws AlgorithmException;
  public boolean hasSolved() throws AlgorithmException;
  public AlgorithmMonitor getMonitor() throws AlgorithmException;
  public Result getResult() throws AlgorithmException;
}
