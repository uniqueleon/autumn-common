package org.aztec.autumn.common.math.modeling.ga;

import org.aztec.autumn.common.algorithm.genetic.Gene;

public class PackingStep implements Gene {
	
	private int no;
	private String itemID;
	private int num;
	private double x;
	private double y;
	private double z;
	private double length;
	private double width;
	private double height;

	public PackingStep() {
		super();
	}

	public PackingStep(int no,String itemID, double x, double y, double z,
			double length, double width, double height ,int num) {
		super();
		this.itemID = itemID;
		this.no = no;
		this.x = x;
		this.y = y;
		this.z = z;
		this.length = length;
		this.width = width;
		this.height = height;
		this.num = num;
	}

	@Override
	public double getMutationRate() {
		AlgorithmConfig config = AlgorithmConfig.getInstance();
		return config.getMutationRatio();
	}

	@Override
	public Gene mutate() {
		return null;
	}

	@Override
	public <T> T get() {
		return (T) this;
	}

	@Override
	public PackingStep clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		PackingStep newStep = new PackingStep();
		newStep.itemID = this.itemID;
		newStep.x = this.x;
		newStep.y = this.y;
		newStep.z = this.z;
		newStep.length = this.length;
		newStep.width = this.width;
		newStep.height = this.height;
		newStep.no = this.no;
		return newStep;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "{no=" + no + ", itemID=" + itemID + ", num=" + num + ", loc=(" + x
				+ "," + y + "," + z + "), shape=(" + length + ","
				+ width + "," + height + ")}\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((itemID == null) ? 0 : itemID.hashCode());
		temp = Double.doubleToLongBits(length);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + no;
		result = prime * result + num;
		temp = Double.doubleToLongBits(width);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackingStep other = (PackingStep) obj;
		if (Double.doubleToLongBits(height) != Double
				.doubleToLongBits(other.height))
			return false;
		if (itemID == null) {
			if (other.itemID != null)
				return false;
		} else if (!itemID.equals(other.itemID))
			return false;
		if (Double.doubleToLongBits(length) != Double
				.doubleToLongBits(other.length))
			return false;
		if (no != other.no)
			return false;
		if (num != other.num)
			return false;
		if (Double.doubleToLongBits(width) != Double
				.doubleToLongBits(other.width))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	
}
