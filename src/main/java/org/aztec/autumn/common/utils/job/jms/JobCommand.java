package org.aztec.autumn.common.utils.job.jms;

import java.io.Serializable;

import org.aztec.autumn.common.utils.job.JobInfo;

public class JobCommand implements Serializable{
  
  private JobInfo jobInfo;
  private String command;
  
  public static final String STOP = "stop";
  public static final String START = "start";
  public static final String RESTART = "restart";

  public JobCommand(JobInfo jobInfo,String command) {
    this.jobInfo = jobInfo;
    this.command = command;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public JobInfo getJobInfo() {
    return jobInfo;
  }

  public void setJobInfo(JobInfo jobInfo) {
    this.jobInfo = jobInfo;
  }

}
