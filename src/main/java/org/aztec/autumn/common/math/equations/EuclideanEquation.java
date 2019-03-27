package org.aztec.autumn.common.math.equations;


public class EuclideanEquation{
	private Long dividend;
	private Long multiple;
	private Long divisor;
	private Long remainder;
	
	public Long getMultiple() {
		return multiple;
	}
	public void setMultiple(Long multiple) {
		this.multiple = multiple;
	}
	
	public Long getDividend() {
		return dividend;
	}
	public void setDividend(Long dividend) {
		this.dividend = dividend;
	}
	public Long getDivisor() {
		return divisor;
	}
	public void setDivisor(Long divisor) {
		this.divisor = divisor;
	}
	public Long getRemainder() {
		return remainder;
	}
	public void setRemainder(Long remainder) {
		this.remainder = remainder;
	}
	public EuclideanEquation(Long result, Long multiple, Long base, Long remainder) {
		super();
		this.dividend = result;
		this.multiple = multiple;
		this.divisor = base;
		this.remainder = remainder;
	}
	

	public String toString() {
		return remainder != 0 ? remainder + " = " + dividend + " - " + multiple + " * " + divisor : multiple + " = " + dividend + " / " + divisor;
	}
	
	
}