package org.aztec.autumn.common.utils.job.jms;

import java.util.Date;
import java.util.UUID;

import org.aztec.autumn.common.utils.job.JobInfo;

public class JMSJobInfo implements JobInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = UUID.randomUUID().toString();
	private Date startTime;
	private Date endTime;
	private String fixedTime;
	private Long period;
	private Integer count;
	private String taskName;
	private String conditionName;
	private transient Runnable runnableTask;
	private String localhost;

	public JMSJobInfo() {
		// TODO Auto-generated constructor stub
	}

	public JMSJobInfo(Date startTime, Date endTime, String fixedTime, Long period, Integer count, Runnable task,
			String conditionName) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.fixedTime = fixedTime;
		this.period = period;
		this.count = count;
		this.runnableTask = task;
		if(task != null){
			this.taskName = task.getClass().getName();
		}
		this.conditionName = conditionName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScheme() {
		return fixedTime;
	}

	public void setFixedTime(String fixedTime) {
		this.fixedTime = fixedTime;
	}

	public String getLocalhost() {
		return localhost;
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}

	public Runnable getRunnableTask() {
		return runnableTask;
	}

	public void setRunnableTask(Runnable runnableTask) {
		this.runnableTask = runnableTask;
	}

}
