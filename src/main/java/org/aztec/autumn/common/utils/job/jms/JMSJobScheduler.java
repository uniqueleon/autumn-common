package org.aztec.autumn.common.utils.job.jms;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;

import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.jms.DestinationType;
import org.aztec.autumn.common.utils.jms.IMessageSender;
import org.aztec.autumn.common.utils.jms.IMessengerFactory;
import org.aztec.autumn.common.utils.job.Job;
import org.aztec.autumn.common.utils.job.JobCondition;
import org.aztec.autumn.common.utils.job.JobException;
import org.aztec.autumn.common.utils.job.JobScheduler;
import org.aztec.autumn.common.utils.job.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSJobScheduler implements JobScheduler {

  private IMessageSender sender;
  private String url;
  private IMessengerFactory factory;
  private static final String IDENTICAL_PREFIX = "JOB_FRAMEWORK_SUBMIT_JOBS";
  private static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);
  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

  public JMSJobScheduler(String url, IMessengerFactory factory)
      throws JMSException {
    this.url = url;
    this.factory = factory;
    sender = factory.getSender(url, DestinationType.QUEUE.name());
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, Date endTime, Long period)
      throws JobException {
    try {
      JMSJobInfo info = new JMSJobInfo(
          beginTime, endTime, null, period, null, task,
          null);
      sender.sendObject(generateMsgLocation(beginTime), info);
      return new JobHandler(info.getId(), url, factory);
    } catch (JMSException e) {
      LOG.error(e.getMessage(), e);
      throw new JobException(e.getMessage(), e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, int count, Long period)
      throws JobException {
    try {
      JMSJobInfo info = 
          new JMSJobInfo(beginTime, null, null, period, count, task, null);
      sender.sendObject(generateMsgLocation(beginTime),info);
      return new JobHandler(info.getId(), url, factory);
    } catch (JMSException e) {
      LOG.error(e.getMessage(), e);
      throw new JobException(e.getMessage(), e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, String fixedTime,
      JobCondition condition) throws JobException {
    try {
      JMSJobInfo info = new JMSJobInfo(
          beginTime, null, fixedTime, null, null, task,
          JobCondition.class.getName());
      sender.sendObject(generateMsgLocation(beginTime), info);
      return new JobHandler(info.getId(), url, factory);
    } catch (JMSException e) {
      LOG.error(e.getMessage(), e);
      throw new JobException(e.getMessage(), e);
    }
  }

  private String generateMsgLocation(Date beginTime) {
    String destination = IDENTICAL_PREFIX;
    //destination += formatter.format(beginTime);
    return destination;
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, Date endTime, String fixedTime)
      throws JobException {
    try {
      JMSJobInfo info = new JMSJobInfo(
          beginTime, endTime, fixedTime, null, null, task,
          null);
      sender.sendObject(generateMsgLocation(beginTime), info);
      return new JobHandler(info.getId(), url, factory);
    } catch (JMSException e) {
      LOG.error(e.getMessage(), e);
      throw new JobException(e.getMessage(), e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, int count, String fixedTime)
      throws JobException {
    try {
      JMSJobInfo info = new JMSJobInfo(
          beginTime, null, fixedTime, null, count, task,
          null);
      sender.sendObject(generateMsgLocation(beginTime), info);
      return new JobHandler(info.getId(), url, factory);
    } catch (JMSException e) {
      LOG.error(e.getMessage(), e);
      throw new JobException(e.getMessage(), e);
    }
  }

  @Override
  public Job sumbit(Runnable task, Date beginTime, Long period,
      JobCondition condition) throws JobException {
    try {
      JMSJobInfo info = new JMSJobInfo(
          beginTime, null, null, period, null, task,
          condition.getClass().getName());
      sender.sendObject(generateMsgLocation(beginTime), info);
      return new JobHandler(info.getId(), url, factory);
    } catch (JMSException e) {
      LOG.error(e.getMessage(), e);
      throw new JobException(e.getMessage(), e);
    }
  }
  
  public JobStatus sumbit(JMSJobInfo info) throws JobException {
    try {
      sender.sendObject(generateMsgLocation(info.getStartTime()), new JMSJobInfo(
          info.getStartTime(), info.getEndTime(), info.getScheme(), info.getPeriod(), info.getCount(), (Runnable) UtilsFactory.getInstance().getReflectUtil().newInstance(Class.forName(info.getTaskName())),
          info.getConditionName()));
      return null;
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new JobException(e.getMessage(), e);
    }
  }
  
  public static void main(String[] args) {
    
  }
  

}
