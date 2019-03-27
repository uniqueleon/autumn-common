package org.aztec.autumn.common.constant.unit;

public interface CalculableUnit {

	/**
	 * 将当前以amountUnit为单元的数，转化为以当前单位的数量
	 * @param amount 数量
	 * @param amountUnit 旧单位
	 * @return 当前单位为单位的新数量
	 */
	public Double convert(Double amount,CalculableUnit amountUnit);
	//将当前以amountUnit为单元的数，转化为以当前单位的数量
	/**
	 * 转换为当前单位所需要的转换数
	 * @param amount 数量
	 * @param amountUnit 旧单位
	 * @return 当前单位为单位的新数量
	 */
	public Double getConversion(CalculableUnit amountUnit);
	
}
