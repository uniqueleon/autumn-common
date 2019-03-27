package org.aztec.autumn.common.utils.job;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * 作业调度器，用于进行作业调度。作业的提交，调度时间的设置，主要通过这接口
 * 
 * @author 黎明
 *
 */
public interface JobScheduler {

  /**
   * 提交作业
   * 
   * @param task 实际执行的任务
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @param period 固定时间间隔，以毫秒为单位
   * @return
   * @throws JobException 执行过程中抛出的任何异常，都会用这异常进行封装
   */
  public Job sumbit(Runnable task,Date beginTime,Date endTime,Long period) throws JobException;
  /**
   * 提交作业
   * 
   * @param task 实际执行的任务
   * @param beginTime 开始时间
   * @param count 任务执行次数
   * @param period 固定时间间隔，以毫秒为单位
   * @return
   * @throws JobException 执行过程中抛出的任何异常，都会用这异常进行封装
   */
  public Job sumbit(Runnable task,Date beginTime,int count,Long period) throws JobException;
  /**
   * 提交作业
   * 
   * @param task 实际执行的任务
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @param cronjob 用linux的cronjob 表达式来设置任务执行时间
   * @return
   * @throws JobException 执行过程中抛出的任何异常，都会用这异常进行封装
   */
  public Job sumbit(Runnable task,Date beginTime,Date endTime,String cronjob) throws JobException;
  /**
   * 提交作业
   * 
   * @param task 实际执行的任务
   * @param beginTime 开始时间
   * @param count 任务执行次数
   * @param cronjob 用linux的cronjob 表达式来设置任务执行时间
   * @return
   * @throws JobException 执行过程中抛出的任何异常，都会用这异常进行封装
   */
  public Job sumbit(Runnable task,Date beginTime,int count,String cronjob) throws JobException;
  /**
   * 提交作业
   * 
   * @param task 实际执行的任务
   * @param beginTime 开始时间
   * @param period 固定时间间隔，以毫秒为单位
   * @param condition 任务执行条件，可用于作业调度的跳出，跳过，中断，重启
   * @return
   * @throws JobException 执行过程中抛出的任何异常，都会用这异常进行封装
   */
  public Job sumbit(Runnable task,Date beginTime,Long period,JobCondition condition) throws JobException;
  /**
   * 提交作业
   * 
   * @param task 实际执行的任务
   * @param beginTime 开始时间
   * @param cronjob 用linux的cronjob 表达式来设置任务执行时间
   * @param condition 任务执行条件，可用于作业调度的跳出，跳过，中断，重启
   * @return
   * @throws JobException 执行过程中抛出的任何异常，都会用这异常进行封装
   */
  public Job sumbit(Runnable task,Date beginTime,String cronjob,JobCondition condition) throws JobException;
}
