package org.aztec.autumn.common.math.modeling.sales;

import java.util.List;

public class Company {
	
	/** 销售额  */
	private double sales;
	/** 分红 */
	private double bonus;
	/** 可分红股权 */
	private List<Stock> sellableStocks;
	/**
	 * 产品
	 */
	private List<Product> products;

	public Company() {
		// TODO Auto-generated constructor stub
	}

}
