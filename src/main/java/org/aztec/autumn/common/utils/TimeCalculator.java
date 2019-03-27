package org.aztec.autumn.common.utils;

public class TimeCalculator {

	private Long totalUseTime = 0l;
	private Double avgUseTime = 0d;
	private Long maxUseTime = 0l;
	private Long minUseTime = Long.MAX_VALUE;
	private Integer sampleCount = 0;
	private Integer errorCount = 0;
	private Integer successCount = 0;
	private Double successRate = 0d;
	private static ThreadLocal<Long> timetiker = new ThreadLocal<Long>();
	public Long getTotalUseTime() {
		return totalUseTime;
	}
	public void setTotalUseTime(Long totalUseTime) {
		this.totalUseTime = totalUseTime;
	}
	public Double getAvgUseTime() {
		return avgUseTime;
	}
	public void setAvgUseTime(Double avgUseTime) {
		this.avgUseTime = avgUseTime;
	}
	public Long getMaxUseTime() {
		return maxUseTime;
	}
	public void setMaxUseTime(Long maxUseTime) {
		this.maxUseTime = maxUseTime;
	}
	public Long getMinUseTime() {
		return minUseTime;
	}
	public void setMinUseTime(Long minUseTime) {
		this.minUseTime = minUseTime;
	}
	public TimeCalculator(Long totalUseTime, Double avgUseTime, Long maxUseTime,
			Long minUseTime) {
		super();
		this.totalUseTime = totalUseTime;
		this.avgUseTime = avgUseTime;
		this.maxUseTime = maxUseTime;
		this.minUseTime = minUseTime;
	}
	public TimeCalculator() {
		super();
	}

	public void addSample(Long elapseTime){
		totalUseTime += elapseTime;
		if(maxUseTime < elapseTime){
			maxUseTime = elapseTime;
		}else if (minUseTime > elapseTime){
			minUseTime = elapseTime;
		}
		sampleCount ++;
	}
	
	public void calculate(){
		avgUseTime = totalUseTime * 1d / sampleCount;
		successRate = successCount * 100d / sampleCount;
	}
	
	public void start(){
		timetiker.set(System.currentTimeMillis());
	}
	
	public void stop(boolean result) throws Exception{
		Long startTime = timetiker.get();
		if(startTime == null)
			throw new Exception("time ticker has not been start!");
		Long elapseTime = System.currentTimeMillis() - startTime;
		if(result){
			successCount ++;
		} else {
			errorCount ++;
		}
		addSample(elapseTime);
	}
	public TimeCalculator(Long totalUseTime, Double avgUseTime, Long maxUseTime,
			Long minUseTime, Integer sampleCount, Integer errorCount,
			Integer successCount, Double successRate) {
		super();
		this.totalUseTime = totalUseTime;
		this.avgUseTime = avgUseTime;
		this.maxUseTime = maxUseTime;
		this.minUseTime = minUseTime;
		this.sampleCount = sampleCount;
		this.errorCount = errorCount;
		this.successCount = successCount;
		this.successRate = successRate;
	}
	@Override
	public String toString() {
		return "TimeCalculator [totalUseTime=" + totalUseTime + ", avgUseTime="
				+ avgUseTime + ", maxUseTime=" + maxUseTime + ", minUseTime="
				+ minUseTime + ", sampleCount=" + sampleCount + ", errorCount="
				+ errorCount + ", successCount=" + successCount
				+ ", successRate=" + successRate + "]";
	}
	
}
