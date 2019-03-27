package org.aztec.autumn.common.math.modeling.packing;

import org.aztec.autumn.common.constant.unit.LengthUnits;
import org.aztec.autumn.common.constant.unit.WeightUnits;

public class RectangleObject extends BaseObject {

	protected Double height = 0d;
	protected Double width = 0d;
	protected Double length = 0d;
	protected Integer surfaceChoosed;
	protected Integer edgeChoosed;
	protected final static LengthUnits minLengthUnit = LengthUnits.MM;
	protected boolean fixed = false;

	public RectangleObject(Double length, Double width, Double height, LengthUnits unit, Double weight,
			WeightUnits wUnit) {
		super(new Location(), weight, wUnit);
		this.height = minLengthUnit.convert(height, unit);
		this.width = minLengthUnit.convert(width, unit);
		this.length = minLengthUnit.convert(length, unit);
	}

	public RectangleObject(Location location, Double length, Double width, Double height, LengthUnits unit,
			Double weight, WeightUnits wUnit) {
		super(location, weight, wUnit);
		this.height = minLengthUnit.convert(height, unit);
		this.width = minLengthUnit.convert(width, unit);
		this.length = minLengthUnit.convert(length, unit);
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Long getHeightAsLong() {
		return height.longValue();
	}

	public Long getWidthAsLong() {
		return width.longValue();
	}

	public Long getLengthAsLong() {
		return length.longValue();
	}

	public Double[][] getAreas() {
		Double[][] areas = new Double[][] { {length * width,height}, {length * height,width}, {width * height,length} };
		return areas;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public Integer getSurfaceChoosed() {
		return surfaceChoosed;
	}

	/**
	 * 0 长 * 宽 1 长 * 高 2 宽 * 高
	 * @param surfaceChoosed
	 */
	public void setSurfaceChoosed(Integer surfaceChoosed) {
		this.surfaceChoosed = surfaceChoosed;
	}
	
	public Double getChoosedHeight() {
		if (surfaceChoosed != null) {
			switch (surfaceChoosed) {
			case 0:
				return height;
			case 1:
				return width;
			case 2:
				return length;
			default:
				return null;
			}
		}
		else {
			return null;
		}
	}

	public Double[][] getChoosedSurfaceData() {
		if (surfaceChoosed != null) {
			switch (surfaceChoosed) {
			case 0:
				return new Double[][] { {length, width},{width,length} };
			case 1:
				return new Double[][] { {length, height},{height,length} };
			case 2:
				return new Double[][] { {width, height},{height,width} };
			default:
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public Integer getEdgeChoosed() {
		return edgeChoosed;
	}

	public void setEdgeChoosed(Integer edgeChoosed) {
		this.edgeChoosed = edgeChoosed;
	}

	public boolean isInside(Location testLoc) {
		
		if((testLoc.getX() > location.getX() && testLoc.getX() < location.getX() + length) 
				&& (testLoc.getY() > location.getY() && testLoc.getY() < location.getY() + width) 
				&& (testLoc.getZ() > location.getZ() && testLoc.getZ() < location.getZ() + height)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "RectangleObject [height=" + height + ", width=" + width + ", length=" + length + ", surfaceChoosed="
				+ surfaceChoosed + ", edgeChoosed=" + edgeChoosed + ", fixed=" + fixed + "]";
	}

	public static void main(String[] args) {
		Double douValue = 10.4d;
		System.out.println(douValue.longValue());
		
		// System.out.println(Math.pow(3, 20));
	}
}
