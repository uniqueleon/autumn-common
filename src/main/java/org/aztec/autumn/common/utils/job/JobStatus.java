package org.aztec.autumn.common.utils.job;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务状态，用于查看任务总体的执行情况
 * @author 黎明
 *
 */
public interface JobStatus extends Serializable{

  /**
   * 适配其它接口
   * @param compatibleCls 可适配的接口类
   * @return 非空，表示可适配传入的接口，空，表示无法适配
   * @throws JobException 适配过程中的所有异常
   */
  public <T> T adatp(Class<T> compatibleCls) throws JobException;
  /**
   * 获取任务执行过程中出现的所有异常
   * @return 异常列表，按照异常出现的顺序排列
   * @throws JobException 获取过程中的所有异常
   */
  public List<Exception> getExceptions() throws JobException;
  /**
   * 获取任务总共执行的次数
   * @return 任务执行次数
   */
  public int getTotalCount();
  /**
   * 获取任务成功执行的次数
   * @return 任务成功执行的次数
   */
  public int getSuccessCount();
  /**
   * 获取任务失败次数
   * @return 任务失败次数
   */
  public int getFailCount();
  /**
   * 获知当前调度任务是否已经中断
   * @return true，已中断,反之false
   */
  public boolean isInterrupted();
  /**
   * 获知当前调度任务是否已经启动
   * @return true,已启动,反之false
   */
  public boolean isStarted();
  /**
   * 获知当前调度任务是否已经结束
   * @return true，已经结束，反之false
   */
  public boolean isFinished();
  /**
   * 获取基本的作业任务信息
   * @return 基本的作业信息
   */
  public JobInfo getBasicInfo();
  /**
   * 获取最新更新时间
   * @return 最新更新时间
   */
  public Date getLastUpdateTime();
}
