package org.aztec.autumn.common.utils.job.jms;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.aztec.autumn.common.utils.jms.IMessengerFactory;
import org.aztec.autumn.common.utils.job.Job;
import org.aztec.autumn.common.utils.job.JobCondition;
import org.aztec.autumn.common.utils.job.JobException;
import org.aztec.autumn.common.utils.job.JobStatus;
import org.aztec.autumn.common.utils.job.JobTaskLoader;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSJob implements Job {

  private JMSJobInfo info;
  private Object task;
  private ScheduledFuture future;
  private ScheduledExecutorService executorService = Executors
      .newScheduledThreadPool(1);
  private JobStatus status;
  private IMessengerFactory factory;
  private Runnable jobService;
  private JobCondition exitCondition;
  private static final Logger LOG = LoggerFactory.getLogger(JMSJob.class);
  private AtomicInteger groupCount = new AtomicInteger(0);
  private Scheduler quartzScheduler;

  public JMSJob(JMSJobInfo info)
      throws ClassNotFoundException, InstantiationException,
      IllegalAccessException {
    this.info = info;
    Class jobClass = new JobTaskLoader(JMSJob.class.getClassLoader())
        .loadClass(info.getTaskName());
    if (info.getConditionName() != null) {
      Class conditionClass = new JobTaskLoader(JMSJob.class.getClassLoader())
          .loadClass(info.getConditionName());
      exitCondition = (JobCondition) conditionClass.newInstance();
    }
    if(info.getRunnableTask() != null){
    	jobService = info.getRunnableTask();
    }
    else{
    	jobService = (Runnable) jobClass.newInstance();
    }
    status = new JMSJobStatus(info);
  }

  @Override
  public JobStatus getStatus() {
    return status;
  }

  @Override
  public JobStatus interupt() throws JobException {

    try {
      if (!status.isStarted())
        throw new JobException("Job[" + status.getBasicInfo().getId()
            + "] is not started!");
      if(future != null)
        future.cancel(true);
      executorService.shutdown();
      if(quartzScheduler != null)quartzScheduler.shutdown();
      status.adatp(JMSJobStatus.class).setLastUpdateTime(new Date());
      status.adatp(JMSJobStatus.class).setStarted(false);
      status.adatp(JMSJobStatus.class).setInterrupted(true);
      return status;
    } catch (SchedulerException e) {
      e.printStackTrace();
      throw new JobException(e.getMessage(),e);
    }
  }

  @Override
  public JobStatus restart() throws JobException {

    if (!status.isInterrupted())
      throw new JobException("Job[" + status.getBasicInfo().getId()
          + "] is not interrupted!");
    interupt();
    start();
    status.adatp(JMSJobStatus.class).setLastUpdateTime(new Date());
    status.adatp(JMSJobStatus.class).setStarted(true);
    status.adatp(JMSJobStatus.class).setInterrupted(false);
    return status;
  }

  @Override
  public JobStatus start() throws JobException {

    try {
      if (status.isStarted())
        throw new JobException("Job[" + status.getBasicInfo().getId()
            + "] is started!");
      if (status.isInterrupted())
        throw new JobException("Job[" + status.getBasicInfo().getId()
            + "] is interrupted!");
      startJob();
      status.adatp(JMSJobStatus.class).setLastUpdateTime(new Date());
      status.adatp(JMSJobStatus.class).setStarted(true);
      status.adatp(JMSJobStatus.class).setInterrupted(false);
    } catch (SchedulerException e) {
      throw new JobException("Executing quartz cron job failed!", e);
    }

    return status;
  }

  private void startJob() throws SchedulerException {
    if (info.getPeriod() != null) {
      executorService.scheduleAtFixedRate(new TaskExecutor(this), getDelay(),
          info.getPeriod(), TimeUnit.MILLISECONDS);
    } else if (info.getScheme() != null) {
      startCronJob();
    }
  }

  private void startCronJob() throws SchedulerException {
    SchedulerFactory factory = new StdSchedulerFactory();
    quartzScheduler = factory.getScheduler();

    int groupId = groupCount.incrementAndGet();
    JobDetail job = JobBuilder.newJob(TaskExecutor.class)
        .withIdentity(info.getId(), "group@" + groupId).build();
    CronTrigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("trigger@" + info.getId(), "group" + groupId)
        .withSchedule(CronScheduleBuilder.cronSchedule(info.getScheme()))
        .build();
    quartzScheduler.scheduleJob(job, trigger);
    cronJobMapping.put(job, this);
    quartzScheduler.start();
  }

  public long getDelay() {
    Date now = new Date();
    return info.getStartTime().getTime() - now.getTime();
  }

  private void finish() throws SchedulerException, JobException {
    if (executorService != null)
      executorService.shutdown();
    if (quartzScheduler != null)
      quartzScheduler.shutdown();
    status.adatp(JMSJobStatus.class).setFinished(true);
    status.adatp(JMSJobStatus.class).setLastUpdateTime(new Date());
  }
  
  private static Map<JobDetail, JMSJob> cronJobMapping = new ConcurrentHashMap<JobDetail, JMSJob>();
  

  public static class TaskExecutor implements Runnable, org.quartz.Job {

    
    private JMSJob job;
    
    public TaskExecutor() {
      // TODO Auto-generated constructor stub
    }
    

    public TaskExecutor(JMSJob job) {
      this.job = job;
    }
    
    @Override
    public void run() {

      try {
        JMSJobStatus jmsStatus = job.status.adatp(JMSJobStatus.class);
        try {
          if (isEnd()) {
            LOG.debug("JOB[" + job.status.getBasicInfo().getTaskName() + "@"
                + job.status.getBasicInfo().getId() + "] is finished!");
            job.finish();
            return;
          }
          else if(job.exitCondition == null || !job.exitCondition.isSkip()){
            LOG.info("JOB[" + job.status.getBasicInfo().getTaskName() + "@"
                + job.status.getBasicInfo().getId() + "] is started!");
            job.jobService.run();
            jmsStatus.taskSuccess();
            jmsStatus.setLastUpdateTime(new Date());
            LOG.info("JOB[" + job.status.getBasicInfo().getTaskName() + "@"
                + job.status.getBasicInfo().getId() + "] executed success!");
          }
        } catch (Exception e) {
          jmsStatus.taskFail(e);
          jmsStatus.setLastUpdateTime(new Date());
          LOG.warn("JOB[" + job.status.getBasicInfo().getTaskName() + "@"
              + job.status.getBasicInfo().getId() + "] executed fail!");
        }
      } catch (JobException e) {
        LOG.error(e.getMessage(), e);
      }
    }

    private boolean isEnd() {
      if (job.exitCondition != null)
        return job.exitCondition.isEnd();
      if (job.info.getCount() != null)
        return job.status.getTotalCount() > job.info.getCount();
      if (job.info.getEndTime() != null)
        return new Date().after(job.info.getEndTime());
      return false;
    }

    @Override
    public void execute(JobExecutionContext paramJobExecutionContext)
        throws JobExecutionException {
      this.job = cronJobMapping.get(paramJobExecutionContext.getJobDetail());
      this.run();
    }

  }

}
