package org.aztec.autumn.common.utils.job;

/**
 * 任务执行条件，用于提供更灵活的任务执行控制。
 * 
 * 
 * @author 黎明
 *
 */
public interface JobCondition {

  /**
   * 判断当前作业是否已经结束
   * @return true 已结束 反之，false
   */
  public boolean isEnd();
  /**
   * 判断当前作业是否可以跳过
   * @return true，可跳过，反之，false
   */
  public boolean isSkip();
  /**
   * 判断当前作业是否应当中断
   * @return true，中断，反之，false
   */
  public boolean isInterupt();
  /**
   * 判断当前作业是否应当重启
   * 
   * @return true，重启,反之，false
   */
  public boolean restart();
}
