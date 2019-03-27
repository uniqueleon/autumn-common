package org.aztec.autumn.common.math.modeling.sales;

public interface PurchasePassion {

	/**
	 * 会再复购
	 * @return
	 */
	public boolean willPurchase();
	/**
	 * 会帮忙推广
	 * @return
	 */
	public boolean willPromote();
}
