package org.aztec.autumn.common.utils.job;
/**
 * 调度器工厂，用于配装作业调度器及相关接口实现
 * @author 黎明
 *
 */
public interface SchedulerFactory {

  /**
   * 获取分布式作业调度器
   * @param url JMS 代理服务器连接url，默认为ActiveMQ Broker的连接URL，如tcp://10.10.1.107:61616/
   * @return 作业配度器
   * @throws JobException
   */
  public JobScheduler getDistributedScheduler(String url) throws JobException;
  

  /**
   * 获取本地的简单作业调度器
   * @return 作业配度器
   * @throws JobException
   */
  public JobScheduler getSimpleScheduler() throws JobException;
}
