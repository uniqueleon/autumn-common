package org.aztec.autumn.common.utils.job;

/**
 * 作业接口
 * @author 黎明
 *
 */
public interface Job {

  /**
   * 获取最新的作业状态信息 
   * @return 最新的作业状态信息
   */
  public JobStatus getStatus();
  /**
   * 中断作业。
   * @return 最新的作业信息
   * @throws JobException
   */
  public JobStatus interupt() throws JobException;
  /**
   * 重启作业。中断作业，并重新启动
   * @return 最新的作业状态信息
   * @throws JobException
   */
  public JobStatus restart() throws JobException;
  /**
   * 启动作业。默认情况下，作业提交到调度器，只要达到作业的执行条件，就会自动启动。所以一般情况下，不需要调用接口，只有当作业被用户主动中断后，才需要调用此接口重新开启。
   * @return 最新的作业状态信息
   * @throws JobException
   */
  public JobStatus start() throws JobException;
  
}
