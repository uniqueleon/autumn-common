package org.aztec.autumn.common.math.modeling.sales;

/**
 * 产品
 * @author 10064513
 *
 */
public class Product {
	
	
	private String id;
	
	/** 成本  */
	private double cost;
	/** 价格  */
	private double price;
	/** 折扣  */
	private double discount;
	/**
	 * 生产企业
	 */
	private Company company;

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Product(double cost, double price, double discount) {
		super();
		this.cost = cost;
		this.price = price;
		this.discount = discount;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
