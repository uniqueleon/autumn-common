package org.aztec.autumn.common.utils.job.impl;

import org.aztec.autumn.common.utils.job.JobCondition;

public class NonStopCondition implements JobCondition {

	public NonStopCondition() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isEnd() {
		return false;
	}

	@Override
	public boolean isSkip() {
		return false;
	}

	@Override
	public boolean isInterupt() {
		return false;
	}

	@Override
	public boolean restart() {
		return false;
	}

}
