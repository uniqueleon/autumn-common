package org.aztec.autumn.common.utils.compress;

import java.io.PrintStream;

public interface CodingProgress {

	public CodingPhase getCurrentPhase();
	public void setCurrentPhase(CodingPhase phase);
	public Float getTotalRate();
	public Float getCurrentRate();
	public void setCurrentRate(Float newRate);
	public Long getTotalWorkLoad();
	public Long getTotalFinishedWork();
	public void setTotalFinishedWork(Long workCount); 
	public Long getWorkLoad();
	public void setWorkLoad(Long workLoad);
	public Long getFinishedWork();
	public void setFinishedWork(Long workCount);
	public Long getRemainTime();
	public Long getElapseTime();
	public void setElapseTime(Long elapseTime);
	public void print();
	public void print(PrintStream printer);
}
