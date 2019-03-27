package org.aztec.autumn.common.math.modeling.packing;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.aztec.autumn.common.constant.unit.LengthUnits;
import org.aztec.autumn.common.constant.unit.WeightUnits;
import org.aztec.autumn.common.math.equations.SortableNumber;

import com.beust.jcommander.internal.Lists;

public class Item extends RectangleObject {

	public Item(Double length,Double width,Double height, LengthUnits unit,
			Double weight, WeightUnits gUnit,Long number) {
		
		super(length ,width,height,unit,weight, gUnit);
		super.number = number;
		super.id = "ITEM_" + UUID.randomUUID().toString();
	}
	
	public Item(String name,Long[] shapeData,Long weight,Long number) {
		super(shapeData[0].doubleValue(),shapeData[1].doubleValue(),shapeData[2].doubleValue(),
				LengthUnits.MM,weight.doubleValue(),WeightUnits.G);
		super.number = number;
		super.id = name;
	}
	
	@Override
	protected String getIDPrefix() {
		return "ITEM_";
	}

	public Item clone() {
		Item newItem = new Item(new Double(length), new Double(width), new Double(height), Item.minLengthUnit,
				new Double(weight), Item.minWeightUnit, new Long(number));
		newItem.setLocation(location);
		newItem.setId(id);
		return newItem;
	}
	
	public Long getHeight(Long length,Long width) {
		List<SortableNumber> sortNumber = Lists.newArrayList();
		sortNumber.add(new SortableNumber(this.length.longValue(), 0));
		sortNumber.add(new SortableNumber(this.width.longValue(), 1));
		sortNumber.add(new SortableNumber(this.height.longValue(), 2));
		sortNumber.sort(new Comparator<SortableNumber>() {

			@Override
			public int compare(SortableNumber o1, SortableNumber o2) {
				return o1.getNumber() - o2.getNumber() > 0 ? 1 : -1;
			}
		
		});
		Long[] compareNumber = new Long[] {length <= width ? length : width,length <= width ? width : length};
		for(int i = 0;i < sortNumber.size();i++) {
			SortableNumber sortNum = sortNumber.get(i);
			if(sortNum.getNumber().equals(compareNumber[0])) {
				
				SortableNumber nextNum = sortNumber.get(i + 1);
				if(nextNum.getNumber().equals(compareNumber[1])) {
					if(i == 0) {
						return sortNumber.get(2).getNumber();
					}
					else {
						return sortNumber.get(0).getNumber();
					}
				}
				else if(i == 0 && sortNumber.get(i + 2).getNumber().equals(compareNumber[1])){
					return sortNumber.get(1).getNumber();
				}
			}
		}
		return null;
	}
	
	public void setSurface(Box box,Long chooseEdge) {
		int edgeFlag = length.longValue() == chooseEdge ? 0 : width.longValue() == chooseEdge ? 1 : 
			height.longValue() == chooseEdge ? 2 : -1 ;
		switch(edgeFlag) {
		case 0:
			if(width < box.getWidth() && height < box.getHeight()) {
				surfaceChoosed = 0;
			}
			else if(height < box.getWidth() && width < box.getHeight()){
				surfaceChoosed = 1;
			}
			break;
		case 1:
			if(length < box.getWidth() && height < box.getHeight()) {
				surfaceChoosed = 0;
			}
			else if(height < box.getWidth() && length < box.getHeight()){
				surfaceChoosed = 2;
			}
			break;
		case 2:
			if(length < box.getWidth() && width < box.getHeight()) {
				surfaceChoosed = 1;
			}
			else if(width < box.getWidth() && length < box.getHeight()){
				surfaceChoosed = 2;
			}
			break;
		case -1:
		break;
		}
		
	}

	@Override
	public String toString() {
		return "\nItem [id=" + id + ",number=" + number + ",height=" + height + ", width=" + width + ", length=" + length + ", location=" + location
				+ ", weight=" + weight + ", ]";
	}
	
	
}
