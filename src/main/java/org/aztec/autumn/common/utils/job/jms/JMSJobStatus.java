package org.aztec.autumn.common.utils.job.jms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aztec.autumn.common.utils.job.JobException;
import org.aztec.autumn.common.utils.job.JobInfo;
import org.aztec.autumn.common.utils.job.JobStatus;

public class JMSJobStatus implements JobStatus {

  private String jobId;
  private String taskClass;
  private int totalCount;
  private int failCount;
  private int successCount;
  private JobInfo info;
  private List<Exception> exceptions = new ArrayList<Exception>();
  private boolean interrupted;
  private boolean started;
  private boolean finished;
  private Date lastUpdateTime;
  
  public JMSJobStatus(JobInfo info) {
    // TODO Auto-generated constructor stub
    this.info = info;
  }


  @Override
  public <T> T adatp(Class<T> compatibleCls) throws JobException {
    if(compatibleCls.equals(JMSJobStatus.class))
      return (T) this;
    return null;
  }

  @Override
  public List<Exception> getExceptions() throws JobException {
    return exceptions;
  }

  @Override
  public int getSuccessCount() {
    return successCount;
  }

  @Override
  public int getFailCount() {
    return failCount;
  }
  
  public void taskSuccess(){
    successCount++;
    totalCount++;
  }
  
  public void taskFail(Exception execException){
    exceptions.add(execException);
    failCount++;
    totalCount++;
  }


  @Override
  public String toString() {
    String msg = "{\"executedTask:\"" + taskClass + ",\"success\":"+successCount+",\"failCount\":"+failCount+",\"totalCount\":"+ totalCount;
    msg += ",\"exception\":[";
    for(Exception exception : exceptions)
      msg += "\"" + exception.getMessage() + "\",";
    msg = msg.substring(0,msg.length() - 1);
    msg += "]}";
    return msg;
  }


  @Override
  public JobInfo getBasicInfo() {
    return info;
  }

  public void setInterrupted(boolean interrupted) {
    this.interrupted = interrupted;
  }
  

  @Override
  public boolean isInterrupted() {
    return interrupted;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  @Override
  public boolean isStarted() {
    return started;
  }


  @Override
  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }
  
  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }


  @Override
  public int getTotalCount() {
    return totalCount;
  }
  
  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  @Override
  public boolean isFinished() {
    return finished;
  }

}
