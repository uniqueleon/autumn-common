package org.aztec.autumn.common.utils.job.jms;

import org.aztec.autumn.common.utils.jms.IMessengerFactory;
import org.aztec.autumn.common.utils.jms.MessengerFactory;
import org.aztec.autumn.common.utils.job.JobException;
import org.aztec.autumn.common.utils.job.JobScheduler;
import org.aztec.autumn.common.utils.job.SchedulerFactory;

/**
 * 调度器工厂实现
 * @author 黎明
 *
 */
public class JobSchedulerFactory implements SchedulerFactory{

  public JobSchedulerFactory() {
    // TODO Auto-generated constructor stub
  }

  public JobScheduler getDistributedScheduler(String url) throws JobException{
    try {
      IMessengerFactory factory = new MessengerFactory();
      JobScheduler scheduler = new JMSJobScheduler(url, factory);
      JobManager.startExecutor(factory, scheduler, url);
      return scheduler;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new JobException(e.getMessage(),e);
    }
  }

  @Override
  public JobScheduler getSimpleScheduler() throws JobException {
    return new SimpleJobScheduler();
  }
}
