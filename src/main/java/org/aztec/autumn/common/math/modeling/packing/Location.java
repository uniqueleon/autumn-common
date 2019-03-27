package org.aztec.autumn.common.math.modeling.packing;

public class Location {

	protected Double x = 0d;
	protected Double y = 0d;
	protected Double z = 0d;
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public Double getZ() {
		return z;
	}
	public void setZ(Double z) {
		this.z = z;
	}
	public Location(Double x, Double y, Double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Location() {
		super();
	}
	public Location clone() {
		Location newLocation = new Location();
		newLocation.setX(new Double(x));
		newLocation.setY(new Double(y));
		newLocation.setZ(new Double(z));
		return newLocation;
	}
	
	public boolean isLinkedOnHorizontal(Location location2,Long distance) {
		if(this.getY().equals(location2.getY()) && 
				(Math.abs(location2.getX() - getX())) >= distance) {
			return true;
		}
		return false;
	}
	
	public boolean isLinkedOnVertical(Location location2,Long distance) {
		if(this.getX().equals(location2.getX()) && 
				(Math.abs(location2.getY() - getY())) >= distance) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
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
		Location other = (Location) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		if (z == null) {
			if (other.z != null)
				return false;
		} else if (!z.equals(other.z))
			return false;
		return true;
	}
	
	
}
