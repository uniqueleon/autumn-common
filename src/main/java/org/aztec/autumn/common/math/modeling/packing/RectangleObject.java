package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;
import java.util.Random;

import org.aztec.autumn.common.constant.unit.LengthUnits;
import org.aztec.autumn.common.constant.unit.WeightUnits;

import com.google.common.collect.Lists;

public class RectangleObject extends BaseObject {

	protected Double height = 0d;
	protected Double width = 0d;
	protected Double length = 0d;
	protected Integer surfaceChoosed;
	protected Integer edgeChoosed;
	protected Integer shape = -1;
	protected final static LengthUnits minLengthUnit = LengthUnits.MM;
	protected final static Random random = new Random();
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
	
	
	public Double getLongestEdge(){
		return length > width ? (width > height ? length : (height > length ? height : length) )
				: (length > height ? width : (height > width ? height : width));
	}
	
	public Double getShortestEdge(){
		return length < width ? (width < height ? length : (height < length ? height : length) )
				: (length < height ? width : (height < width ? height : width));
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
	
	
	public Integer getShape() {
		return shape;
	}

	public void setShape(Integer shape) {
		this.shape = shape;
	}
	
	public int randomSelectFillableShape(RectangleObject container){
		int tryTime = 6;
		List<Integer> tried = Lists.newArrayList();
		while (tryTime > 0) {
			int chooseIndex = random.nextInt(6);
			while (tried.contains(chooseIndex)) {
				chooseIndex = random.nextInt(6);
			}
			int shape = chooseIndex;
			Double[] edges = getShapeData(shape);
			if (container.getLength() >= edges[0]
					&& container.getWidth() >= edges[1]
					&& container.getHeight() >= edges[2]) {
				return shape;
			}
		}
		return -1;
	}
	
	public int getFillableShapeNum(RectangleObject container){
		int fillableCount = 0;
		for(int i = 0;i < 6;i ++){
			Double[] edges = getShapeData(i);
			if(edges[0] <= container.getLength() 
					&& edges[1] <= container.getWidth()
					&& edges[2] <= container.getHeight()){
				fillableCount++;
			}
		}
		return fillableCount;
	}
	
	public Double[] getShapeData(int shape){
		switch(shape){
		case 0 :
			// l,w,h
			// 0 = 0(mod3), 0 (mod3) + 0(mod2) + 1, 3 - 1
			return new Double[]{length,width,height};
		case 1:
			// 1 = 1(mod3), 1(mod3) + 1(mod2) + 1 = 0(mod3),3-1
			return new Double[]{width,length,height};
		case 2:
			// 2 = 2(mod3), 2(mod3) + 0(mod2) + 1 = 0(mod3),3 - 2  
			return new Double[]{height,length,width};
		case 3:
			// 3 = 0(mod 3), 0 + 1 + 1 = 2,3 - 2
			return new Double[]{length,height,width};
		case 4:
			// 4 = 1(mod 3), 1 + 0 + 1 = 2,3 - 3
			return new Double[]{width,height,length};
		case 5:
			// 5 = 2(mod 3), 2 + 1 + 1 = 1, 3 - 3
			return new Double[]{height,width,length};
		default :
			return null;
		}
	}
	
	public Double getVolume(){
		return length * width * height;
	}
	
	public boolean isExist(){
		if(length == 0 || width == 0 || height == 0)
			return false;
		return true;
	}

	public static void main(String[] args) {
		Double douValue = 10.4d;
		System.out.println(douValue.longValue());
		
		// System.out.println(Math.pow(3, 20));
	}
}
