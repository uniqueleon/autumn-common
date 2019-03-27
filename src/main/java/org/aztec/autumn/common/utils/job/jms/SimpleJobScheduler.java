package org.aztec.autumn.common.utils.job.jms;

import java.util.Date;

import org.aztec.autumn.common.utils.job.Job;
import org.aztec.autumn.common.utils.job.JobCondition;
import org.aztec.autumn.common.utils.job.JobException;
import org.aztec.autumn.common.utils.job.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJobScheduler implements JobScheduler{

  private static Logger LOG = LoggerFactory.getLogger(JobScheduler.class);
  
  public SimpleJobScheduler() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, Date endTime, Long period)
      throws JobException {
    try {
      Job job =  new JMSJob(new JMSJobInfo(beginTime, endTime, null, period, null, task, null));
      job.start();
      return job;
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(e.getMessage(),e);
      throw new JobException(e.getMessage(),e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, int count, Long period)
      throws JobException {
    try {
      Job job = new JMSJob(new JMSJobInfo(beginTime, null, null, period, count, task, null));
      job.start();
      return job;
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(e.getMessage(),e);
      throw new JobException(e.getMessage(),e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, Date endTime, String cronjob)
      throws JobException {
    try {
      Job job = new JMSJob(new JMSJobInfo(beginTime, endTime, cronjob, null, null, task, null));
      job.start();
      return job;
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(e.getMessage(),e);
      throw new JobException(e.getMessage(),e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, int count, String cronjob)
      throws JobException {
    try {
      Job job = new JMSJob(new JMSJobInfo(beginTime, null, cronjob, null, count, task, null));
      job.start();
      return job;
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(e.getMessage(),e);
      throw new JobException(e.getMessage(),e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, Long period,
      JobCondition condition) throws JobException {
    try {
      Job job = new JMSJob(new JMSJobInfo(beginTime, null, null, period, null, task, condition.getClass().getName()));
      job.start();
      return job;
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(e.getMessage(),e);
      throw new JobException(e.getMessage(),e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, String cronjob,
      JobCondition condition) throws JobException {
    try {
      Job job = new JMSJob(new JMSJobInfo(beginTime, null, cronjob, null, null, task, condition.getClass().getName()));
      job.start();
      return job;
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(e.getMessage(),e);
      throw new JobException(e.getMessage(),e);
    }
  }

}
