package org.aztec.autumn.common.math.modeling.sales;

import java.util.List;

public class Customer {
	
	private List<Product> purchase;
	private List<Stock> stocks;
	private double earnning;
	private double deposit;
	private PurchasePassion passion;

	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public List<Product> getPurchase() {
		return purchase;
	}

	public void setPurchase(List<Product> purchase) {
		this.purchase = purchase;
	}

	public double getEarnning() {
		return earnning;
	}

	public void setEarnning(double earnning) {
		this.earnning = earnning;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public PurchasePassion getPassion() {
		return passion;
	}

	public void setPassion(PurchasePassion passion) {
		this.passion = passion;
	}

	
}
