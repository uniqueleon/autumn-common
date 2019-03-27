package org.aztec.autumn.common.utils.job;

/**
 * 作业配置工具
 * @author 黎明
 *
 */
public class JobConfigUtils {

  private static final Long SECOND = 1000l;
  private static final Long MINUTE = 60 * SECOND;
  private static final Long HOUR = 60 * MINUTE;
  private static final Long DAY = 24 * HOUR;
  
  public JobConfigUtils() {
    // TODO Auto-generated constructor stub
  }
  
  private Long getSecond(long second){
    return second * SECOND;
  }
  
  private Long getMinute(long minute){
    return minute * MINUTE;
  }
  
  private Long getHour(long hour){
    return hour * HOUR;
  }
  
  private Long getDay(long day){
    return day * DAY;
  }

}
