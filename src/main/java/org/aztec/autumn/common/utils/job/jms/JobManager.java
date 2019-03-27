package org.aztec.autumn.common.utils.job.jms;

import static org.aztec.autumn.common.utils.job.JobFrameworkConstant.JOB_EXECUTOR_PUNISHMENT;
import static org.aztec.autumn.common.utils.job.JobFrameworkConstant.JOB_EXECUTOR_SLEEP_INTERVAL;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;

import org.aztec.autumn.common.utils.jms.DestinationType;
import org.aztec.autumn.common.utils.jms.IMessageReceiver;
import org.aztec.autumn.common.utils.jms.IMessageSender;
import org.aztec.autumn.common.utils.jms.IMessageSubscriber;
import org.aztec.autumn.common.utils.jms.IMessengerFactory;
import org.aztec.autumn.common.utils.job.Job;
import org.aztec.autumn.common.utils.job.JobException;
import org.aztec.autumn.common.utils.job.JobFrameworkConstant;
import org.aztec.autumn.common.utils.job.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobManager {

  private Map<String, IMessageSubscriber> jobStatusSubscriber;
  private IMessageReceiver jobReceiver;
  IMessageSender publisher;
  IMessageSender sender;
  private JobScheduler scheduler;
  private String url;
  private static final Logger LOG = LoggerFactory.getLogger(JobManager.class);
  private static final Map<String, Job> RUNNING_JOBS = new ConcurrentHashMap<String, Job>();
  private static final Map<String, JobManager> executorPool = new ConcurrentHashMap<String, JobManager>();

  public static JobManager startExecutor(IMessengerFactory factory,
      JobScheduler scheduler, String url) throws JMSException, JobException {
    if (!executorPool.containsKey(url)) {
      JobManager executor = new JobManager(factory, scheduler, url);
      executor.start();
      executorPool.put(url, executor);
      return executor;
    } else {
      throw new JobException("The job message protocol [" + url
          + "] has been use, and the executor has been started!");
    }
  }

  private JobManager(IMessengerFactory factory, JobScheduler scheduler,
      String url) throws JMSException {
    // TODO Auto-generated constructor stub
    this.url = url;
    jobReceiver = factory.getReceiver(url, "QUEUE");
    publisher = factory.getSender(url,
        DestinationType.TOPIC.name());
    sender = factory.getSender(url, DestinationType.QUEUE.name()); 
    this.scheduler = scheduler;
  }

  private void start() {
    Thread fetcherThread = new Thread(new JobFetcher());
    fetcherThread.start();
    Thread newThread = new Thread(new JobCommandExecutor());
    newThread.start();
    newThread = new Thread(new JobStatusPublisher());
    newThread.start();
  }

  class JobFetcher implements Runnable {

    private boolean runnable = true;

    @Override
    public void run() {
      while (runnable) {
        try {
          LOG.debug("Fetching job for JMS Broker Server!");
          JMSJobInfo info = jobReceiver.receiveObject(
              JobFrameworkConstant.SUBMIT_JOB_MESSAGE_NAME, JMSJobInfo.class);
          Date now = new Date();
          if (info.getStartTime().getTime() < now.getTime()) {
            LOG.debug("Found a new job to start!");
            JMSJob newJob = new JMSJob(info);
            newJob.start();
            RUNNING_JOBS.put(info.getId(), newJob);
            Thread.currentThread().sleep(JOB_EXECUTOR_SLEEP_INTERVAL);
          } else {
            ((JMSJobScheduler) scheduler).sumbit(info);
            Thread.currentThread().sleep(
                JOB_EXECUTOR_SLEEP_INTERVAL * JOB_EXECUTOR_PUNISHMENT);
          }
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          LOG.error(e.getMessage(), e);
        }
      }
    }

  }

  class JobCommandExecutor implements Runnable {

    private boolean runnable = true;

    @Override
    public void run() {
      while (runnable) {
        try {
          LOG.debug("Fetching job command for JMS Broker Server!");
          JobCommand info = jobReceiver.receiveObject(
              JobFrameworkConstant.JOB_COMMAND_MESSAGE_NAME, JobCommand.class);
          Date now = new Date();
          if (RUNNING_JOBS.containsKey(info.getJobInfo().getId())) {
            Job job = RUNNING_JOBS.get(info.getJobInfo().getId());
            if (info.getCommand().equals(JobCommand.START)) {
              job.start();
            } else if (info.getCommand().equals(JobCommand.STOP)) {
              job.interupt();
            } else if (info.getCommand().equals(JobCommand.RESTART)) {
              job.restart();
            }
            Thread.currentThread().sleep(JOB_EXECUTOR_SLEEP_INTERVAL);
          } else {
            sender.sendObject(JobFrameworkConstant.JOB_COMMAND_MESSAGE_NAME,
                info);
            Thread.currentThread().sleep(
                JOB_EXECUTOR_SLEEP_INTERVAL * JOB_EXECUTOR_PUNISHMENT);
          }
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          LOG.error(e.getMessage(), e);
        }
      }
    }

  }

  class JobStatusPublisher implements Runnable {

    private boolean runnable = true;

    @Override
    public void run() {
      while (runnable) {
        try {
          LOG.debug("Broadcasting job status to JMS Broker Server!");
          for (Job job : RUNNING_JOBS.values()) {
            publisher.sendObject(JobFrameworkConstant.JOB_STATUS_TOPIC,
                job.getStatus());
          }
          Thread.currentThread().sleep(JOB_EXECUTOR_SLEEP_INTERVAL);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          LOG.error(e.getMessage(), e);
        }
      }
    }

  }
}
