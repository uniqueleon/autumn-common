package org.aztec.autumn.common.utils.job;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务信息
 * @author 黎明
 *
 */
public interface JobInfo extends Serializable{

  /**
   * 获取启动时间，如未启动，则返回空
   * @return 启动时间
   */
  public Date getStartTime();
  /**
   * 获取终止时间，如果未终止，则返回空
   * @return 终止时间
   */
  public Date getEndTime();
  /**
   * 获取任务执行周期
   * @return 两次任务间的时间间隔，毫秒为单位
   */
  public Long getPeriod();
  /**
   * 获取任务总共需要执行的次数
   * @return 执行次数
   */
  public Integer getCount();
  /**
   * 获取实际执行的任务名称。通常情况下，为实现了Runnable接口的类名。定义为类名，而不是类对象，是为了方便实现序列化和跨服务器调用。
   * @return 任务名称
   */
  public String getTaskName();
  /**
   * 获取任务执行条件名称。通常情况下，为实现了JobCondition接口的类名。
   * @return 条件名称
   */
  public String getConditionName();
  /**
   * 作业的唯一ID
   * @return 作业id
   */
  public String getId();
  /**
   * 作业调度方案。此字段的实际内容由各个实现逻辑提供，默认的分布式作业调度实现为Cronjob表达式
   * @return 作业调度方案
   */
  public String getScheme();
}
