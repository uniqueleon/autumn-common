package org.aztec.autumn.common.math;

import java.math.BigInteger;

public class PrimeTestResult {
	
	private BigInteger testNum;
	private BigInteger modulus;
	private BigInteger remainder;
	private BigInteger round;
	private boolean success = false;

	public PrimeTestResult(BigInteger testNum,BigInteger modulus, BigInteger remainder, BigInteger round) {
		super();
		this.modulus = modulus;
		this.remainder = remainder;
		this.round = round;
		this.testNum = testNum;
		this.success = true;
	}

	public PrimeTestResult(BigInteger testNum, BigInteger round,BigInteger modulus,boolean success) {
		super();
		this.testNum = testNum;
		this.success = false;
		this.modulus = modulus;
		this.round = round;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public BigInteger getModulus() {
		return modulus;
	}

	public void setModulus(BigInteger modulus) {
		this.modulus = modulus;
	}

	public BigInteger getRemainder() {
		return remainder;
	}

	public void setRemainder(BigInteger remainder) {
		this.remainder = remainder;
	}

	public BigInteger getRound() {
		return round;
	}

	public void setRound(BigInteger round) {
		this.round = round;
	}
	

	public BigInteger getTestNum() {
		return testNum;
	}

	public void setTestNum(BigInteger testNum) {
		this.testNum = testNum;
	}

	@Override
	public String toString() {
		return success ? "PrimeTestResult [raw=" + testNum + " ,modulus=" + modulus + ", remainder=" + remainder + ", round=" + round + "]"
				: "No prime found while raw = " + testNum + ",round=" + round +",modulus=" + modulus + " !";
	}

}
