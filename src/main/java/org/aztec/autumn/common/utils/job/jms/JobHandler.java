package org.aztec.autumn.common.utils.job.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;

import org.aztec.autumn.common.utils.jms.DestinationType;
import org.aztec.autumn.common.utils.jms.IMessageSender;
import org.aztec.autumn.common.utils.jms.IMessageSubscriber;
import org.aztec.autumn.common.utils.jms.IMessengerFactory;
import org.aztec.autumn.common.utils.job.Job;
import org.aztec.autumn.common.utils.job.JobException;
import org.aztec.autumn.common.utils.job.JobFrameworkConstant;
import org.aztec.autumn.common.utils.job.JobStatus;

public class JobHandler implements Job {

  private JobStatus status;
  private String jobId;
  private transient IMessageSubscriber statusSubscriber;
  private transient IMessageSender sender;
  private static final List<JobHandler> ALL_JOBS = new ArrayList<JobHandler>();

  public JobHandler(String jobId, String url, IMessengerFactory factory)
      throws JMSException {
    statusSubscriber = factory.getSubscriber(url,
        JobFrameworkConstant.JOB_STATUS_TOPIC, DestinationType.TOPIC.name());
    statusSubscriber.start();
    sender = factory.getSender(url, DestinationType.QUEUE.name());
    this.jobId = jobId;
    ALL_JOBS.add(this);
    Thread checkerThread = new Thread(new JobStatusChecker());
    checkerThread.start();
  }

  class JobStatusChecker implements Runnable {

    @Override
    public void run() {
      while (true) {

        try {
          JobStatus fetchStatus = (JobStatus) statusSubscriber.getObject();
          if (fetchStatus != null && fetchStatus.getBasicInfo().getId().equals(jobId)){
            status = fetchStatus;
          }
          Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

  }

  @Override
  public JobStatus getStatus() {
    return status;
  }

  @Override
  public JobStatus interupt() throws JobException {
    try {
      sender.sendObject(JobFrameworkConstant.JOB_COMMAND_MESSAGE_NAME,
          new JobCommand(status.getBasicInfo(), JobCommand.STOP));
    } catch (JMSException e) {
      throw new JobException("send interrupt message fail!", e);
    }
    return status;
  }

  @Override
  public JobStatus restart() throws JobException {
    try {
      sender.sendObject(JobFrameworkConstant.JOB_COMMAND_MESSAGE_NAME,
          new JobCommand(status.getBasicInfo(), JobCommand.RESTART));
    } catch (JMSException e) {
      throw new JobException("send restart message fail!", e);
    }
    return status;
  }

  @Override
  public JobStatus start() throws JobException {
    try {
      sender.sendObject(JobFrameworkConstant.JOB_COMMAND_MESSAGE_NAME,
          new JobCommand(status.getBasicInfo(), JobCommand.START));
    } catch (JMSException e) {
      throw new JobException("send start message fail!", e);
    }
    return status;
  }

}
