package org.aztec.autumn.common.utils.compress.base;

import java.io.PrintStream;

import org.aztec.autumn.common.utils.compress.CodingPhase;
import org.aztec.autumn.common.utils.compress.CodingProgress;

public class BaseCodingProgress implements CodingProgress {
	
	private CodingPhase currentPhase;
	private Float totalRate = 0f;
	private Float currentRate = 0f;
	private Long totalWorkLoad;
	private Long workLoad;
	private Long totalFinishedWork = 0l;
	private Long finishedWork = 0l;
	private Long remainTime;
	private Long elapseTime = 0l;
	private Long startTime = System.currentTimeMillis();

	@Override
	public CodingPhase getCurrentPhase() {
		return currentPhase;
	}

	@Override
	public void setCurrentPhase(CodingPhase phase) {
		currentPhase = phase;
	}

	@Override
	public Float getTotalRate() {
		// TODO Auto-generated method stub
		return totalRate;
	}

	@Override
	public Float getCurrentRate() {
		// TODO Auto-generated method stub
		return currentRate;
	}

	@Override
	public void setCurrentRate(Float newRate) {
		// TODO Auto-generated method stub
		this.currentRate = newRate;
	}

	@Override
	public Long getTotalWorkLoad() {
		// TODO Auto-generated method stub
		return totalWorkLoad;
	}

	@Override
	public Long getTotalFinishedWork() {
		// TODO Auto-generated method stub
		return totalFinishedWork;
	}

	@Override
	public void setTotalFinishedWork(Long workCount) {
		// TODO Auto-generated method stub
		this.totalFinishedWork = workCount;
	}

	@Override
	public Long getWorkLoad() {
		return workLoad;
	}

	@Override
	public void setWorkLoad(Long workLoad) {
		this.workLoad = workLoad;
	}

	@Override
	public Long getFinishedWork() {
		return finishedWork;
	}

	@Override
	public void setFinishedWork(Long workCount) {

		this.totalFinishedWork = this.totalFinishedWork - finishedWork;
		this.finishedWork = workCount;
		totalFinishedWork = totalFinishedWork + finishedWork;
		if(totalWorkLoad != 0) {
			totalRate = totalFinishedWork * 1f / totalWorkLoad;
		}
		if(workLoad != 0) {
			currentRate = finishedWork * 1f / workLoad;
		}
		elapseTime = System.currentTimeMillis() - startTime;
	}

	@Override
	public Long getRemainTime() {
		return remainTime;
	}

	@Override
	public Long getElapseTime() {
		return elapseTime;
	}

	@Override
	public void setElapseTime(Long elapseTime) {
		this.elapseTime = elapseTime;
	}

	@Override
	public void print() {
		System.out.println(toString());
	}

	@Override
	public String toString() {
		return "{\"currentPhase\"=" + currentPhase.getName() + ", \"totalRate\"=" + totalRate + ", \"currentRate=\""
				+ currentRate + ", \"totalWorkLoad\"=" + totalWorkLoad + ", \"workLoad\"=" + workLoad + ", \"totalFinishedWork\"="
				+ totalFinishedWork + ", \"finishedWork\"=" + finishedWork + ", \"remainTime\"=" + remainTime + ", \"elapseTime\"="
				+ elapseTime + "}";
	}

	@Override
	public void print(PrintStream printer) {
		printer.print(toString());
	}

	public BaseCodingProgress(CodingPhase currentPhase, Float totalRate, 
			Long totalWorkLoad, Long remainTime
			) {
		super();
		this.currentPhase = currentPhase;
		this.totalRate = totalRate;
		this.totalWorkLoad = totalWorkLoad;
		this.remainTime = remainTime;
	}
	
	

}
