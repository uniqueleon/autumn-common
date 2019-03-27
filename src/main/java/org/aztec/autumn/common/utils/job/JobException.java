package org.aztec.autumn.common.utils.job;

/**
 * 用于封装所有与作业调度相关的异常
 * @author 黎明
 *
 */
public class JobException extends Exception {

  public JobException() {
    // TODO Auto-generated constructor stub
  }

  public JobException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  public JobException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  public JobException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  public JobException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // TODO Auto-generated constructor stub
  }

}
