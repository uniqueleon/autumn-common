package org.aztec.autumn.common.math.modeling.packing;

import java.util.Collections;
import java.util.List;

import org.aztec.autumn.common.constant.unit.LengthUnits;
import org.aztec.autumn.common.constant.unit.WeightUnits;

import com.google.common.collect.Lists;


public class Box extends RectangleObject {
	private Double bearing;

	public Box(Double length, Double width, Double height, LengthUnits unit, Double weight, Double bearing,
			WeightUnits gUnit, Long number) {
		super(length, width, height, unit, weight, gUnit);
		bearing = minWeightUnit.convert(bearing, gUnit);
		super.number = number;
	}

	public Box(String name, Long[] shapeData, Long weight, Long number) {
		super(shapeData[0].doubleValue(), shapeData[1].doubleValue(), shapeData[2].doubleValue(), LengthUnits.MM,
				weight.doubleValue(), WeightUnits.G);
		super.number = number;
		setBearing(0d);
		super.id = name;
	}

	@Override
	protected String getIDPrefix() {
		return "BOX_";
	}

	public Double getBearing() {
		return bearing;
	}

	public void setBearing(Double bearing) {
		this.bearing = bearing;
	}

	public Double getBottomArea() {
		return length * width;
	}

	public Box clone() {
		Box newBox = new Box(new Double(getLength()), new Double(getWidth()), new Double(getHeight()), minLengthUnit,
				getWeight() != null ? new Double(getWeight()) : 0l, 
						getBearing() != null ? new Double(getBearing()) : 0l, minWeightUnit, 
								getNumber() != null ? new Long(getNumber()):null);
		newBox.setSurfaceChoosed(surfaceChoosed);
		newBox.setLocation(location.clone());
		newBox.setId(getId());
		return newBox;
	}
	
	public boolean isShrinkable(double step, double base, boolean onlyBottom) {
		if (step < 0 || base < 0) {
			return false;
		}
		if (onlyBottom) {
			if (length > width ) {
				if(base > length - step) {
					return false;
				}
			} else if (base > width - step) {
					return false;
			}
		} else {
			if (length > width && length > height) {
				if(base > length - step)
					return false;
			} else if (width > height) {
				if( base > width - step)
					return false;
			} else if (base > height - step) {
					return false;
			}
		}
		return true;
	}
	
	public void resize(long step,boolean onlyBottom) {
		length = new Double(step * (length.longValue() / step));
		width = new Double(step * (width.longValue() / step));
		if(!onlyBottom) {
			height = new Double(step * (height.longValue() / step));
		}
	}

	public void shrink(double step, double base, boolean onlyBottom) {
		if (step < 0 || base < 0) {
			throw new IllegalArgumentException("Can't shrink a box[" + length + "," + width + "," + height + "] with step[" + step + "] and base[" + base + "]");
		}
		if(!isShrinkable(step, base, onlyBottom)) {
			throw new IllegalArgumentException("Can't shrink a box[" + length + "," + width + "," + height + "] with step[" + step + "] and base[" + base
					+ "] " + " onlyBottom[" + onlyBottom + "]");
		}
		if (onlyBottom) {
			if (getLength() > getWidth()) {
				length -= step;
			} else {
				width -= step;
			}
		} else {
			if (getLength() > getWidth() && getLength() > getHeight()) {
				length -= step;
			} else if (getWidth() > getHeight()) {
				width -= step;
			} else {
				height -= step;
			}
		}
	}
	
	
	public static List<Box> mergeAll(List<Box> targetBoxes){
		List<Box> mergeBox = Lists.newArrayList();
		List<Integer> mergeIndexes =  Lists.newArrayList();
		for(int i = 0;i < targetBoxes.size();i++) {
			if(mergeIndexes.contains(i))
				continue;
			Box tmpBox = targetBoxes.get(i).clone();
			for(int j = i + 1;j < targetBoxes.size();j++) {
				if(mergeIndexes.contains(j))
					continue;
				Box otherBox = targetBoxes.get(j);
				if(tmpBox.isMergable(otherBox)) {
					otherBox = tmpBox.merge(otherBox);
					mergeBox.add(otherBox);
					mergeIndexes.add(i);
					mergeIndexes.add(j);
					break;
				}
			}
		}
		if(mergeBox.size() == 0) {
			mergeBox.addAll(targetBoxes);
		}
		else {
			for(int i = 0;i < targetBoxes.size();i++) {
				if(!mergeIndexes.contains(i)) {
					mergeBox.add(targetBoxes.get(i));
				}
			}
			mergeBox = mergeAll(mergeBox);
		}
		return mergeBox;
	}
	
	public Box merge(Box otherBox) {
		if(!isMergable(otherBox)) {
			return null;
		}
		boolean horizontal = location.isLinkedOnHorizontal(otherBox.getLocation(), length.longValue());
		if(horizontal) {
			Long startPoint = Math.min(location.getX().longValue(), otherBox.getLocation().getX().longValue());
			Long endPoint = Math.max(location.getX().longValue() + length.longValue(),
					otherBox.getLocation().getX().longValue() + otherBox.length.longValue());
			Location newLocation = location.clone();
			newLocation.setX(startPoint.doubleValue());
			Box newBox = new Box(otherBox.getId(),
					new Long[] { endPoint - startPoint, width.longValue(), height.longValue() },
					otherBox.getWeight().longValue(), otherBox.getNumber());
			newBox.setLocation(newLocation);
			return newBox;
		}
		else {
			Long startPoint = Math.min(location.getY().longValue(), otherBox.getLocation().getY().longValue());
			Long endPoint = Math.max(location.getY().longValue() + width.longValue(),
					otherBox.getLocation().getY().longValue() + otherBox.width.longValue());
			Location newLocation = location.clone();
			newLocation.setY(startPoint.doubleValue());
			Box newBox = new Box(otherBox.getId(),
					new Long[] { length.longValue(), endPoint - startPoint, height.longValue() },
					otherBox.getWeight().longValue(), otherBox.getNumber());
			newBox.setLocation(newLocation);
			return newBox;
		}
	}
	
	public boolean isMergable(Box otherBox) {
		Location otherLoc = otherBox.getLocation();
		if(((location.isLinkedOnHorizontal(otherLoc, length.longValue()) && getWidth().equals(otherBox.getWidth())) ||
				(location.isLinkedOnVertical(otherLoc, length.longValue()) && getLength().equals(otherBox.getLength())))
				&& this.height.equals(otherBox.getHeight())) {
			return true;
		}
		return false;
	}
	
	
	public boolean isFillable(Item item) {
		List<Long> sortEdges = Lists.newArrayList();
		sortEdges.add(item.getWidthAsLong());
		sortEdges.add(item.getHeightAsLong());
		sortEdges.add(item.getLengthAsLong());
		Collections.sort(sortEdges);
		Long[] itemEdges = sortEdges.toArray(new Long[3]);
		sortEdges = Lists.newArrayList();

		sortEdges.add(getWidthAsLong());
		sortEdges.add(getHeightAsLong());
		sortEdges.add(getLengthAsLong());
		Collections.sort(sortEdges);
		Long[] boxEgdes = sortEdges.toArray(new Long[3]);
		boolean fillable = true;
		for(int i = 0 ;i < boxEgdes.length;i++) {
			if(boxEgdes[i] < itemEdges[i]) {
				return false;
			}
		}
		return fillable;
	}

	@Override
	public String toString() {
		return "Box [id=" + id + ", number=" + number + ", length=" + length + ", width="
				+ width + ", height=" + height + ", location=" + location + ", weight=" + weight + ",bearing=" + bearing + " ]";
	}
	
	
}
