package org.aztec.autumn.common.utils.job;

/**
 * 作业调度框架常用常量定义
 * @author 黎明
 *
 */
public interface JobFrameworkConstant {

  public static final String SUBMIT_JOB_MESSAGE_NAME = "JOB_FRAMEWORK_SUBMIT_JOBS";
  public static final String JOB_COMMAND_MESSAGE_NAME = "JOB_FRAMEWORK_JOB_COMMANDS";
  public static final String JOB_MESSAGE_NAME_PREFIX = "JOB_FRAMEWORK_JOB_";
  public static final String JOB_STATUS_MESSAGE_NAME_SUFFIX = "_STUTAS";

  public static String JOB_STATUS_TOPIC = "JOB_FRAMEWORK_JOB_STATUS";
  public static String JOB_ACTIVE_PATTERN = "/$JOB_INFO:{\"id\":[]}^/";
  

  public static final Long JOB_EXECUTOR_SLEEP_INTERVAL = 1000l;
  public static final int JOB_EXECUTOR_PUNISHMENT = 5;
  
  
  public static final String JOB_COMMAND_START = "start";
  public static final String JOB_COMMAND_STOP = "stop";
  public static final String JOB_COMMAND_RESTART = "restart";
}
