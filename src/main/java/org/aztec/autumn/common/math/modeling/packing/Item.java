package org.aztec.autumn.common.math.modeling.packing;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.aztec.autumn.common.constant.unit.LengthUnits;
import org.aztec.autumn.common.constant.unit.WeightUnits;
import org.aztec.autumn.common.math.equations.SortableNumber;
import org.aztec.autumn.common.math.modeling.packing.impl.FillUnit;

import com.google.common.collect.Lists;

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
	
	public List<Long> getFillableEdges(Box box) {
		List<Long> edges = Lists.newArrayList();
		if ((getLength() <= box.getLength())
				&& ((getWidth() <= box.getWidth() && getHeight() <= box
						.getHeight()) || (getWidth() <= box.height && getHeight() <= box
						.getWidth()))) {
			edges.add(getLength().longValue());
		}
		if ((getWidth() <= box.getLength())
				&& ((getLength() <= box.getWidth() && getHeight() <= box
						.getHeight()) || (getLength() <= box.height && getHeight() <= box
						.getWidth()))) {
			edges.add(getWidth().longValue());
		}
		if ((getHeight() <= box.getLength())
				&& ((getLength() <= box.getWidth() && getWidth() <= box
						.getHeight()) || (getLength() <= box.height && getWidth() <= box
						.getWidth()))) {
			edges.add(getHeight().longValue());
		}
		return edges;
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
		Collections.sort(sortNumber,new Comparator<SortableNumber>() {

			@Override
			public int compare(SortableNumber o1, SortableNumber o2) {
				return o1.getNumber().longValue() - o2.getNumber().longValue() > 0 ? 1 : -1;
			}
		
		});
		Long[] compareNumber = new Long[] {length <= width ? length : width,length <= width ? width : length};
		for(int i = 0;i < sortNumber.size();i++) {
			SortableNumber sortNum = sortNumber.get(i);
			if(sortNum.getNumber().equals(compareNumber[0])) {
				
				SortableNumber nextNum = sortNumber.get(i + 1);
				if(nextNum.getNumber().equals(compareNumber[1])) {
					if(i == 0) {
						return sortNumber.get(2).getNumber().longValue();
					}
					else {
						return sortNumber.get(0).getNumber().longValue();
					}
				}
				else if(i == 0 && sortNumber.get(i + 2).getNumber().equals(compareNumber[1])){
					return sortNumber.get(1).getNumber().longValue();
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
			if(width <= box.getWidth() && height <= box.getHeight()) {
				surfaceChoosed = 0;
			}
			else if(height <= box.getWidth() && width <= box.getHeight()){
				surfaceChoosed = 1;
			}
			break;
		case 1:
			if(length <= box.getWidth() && height <= box.getHeight()) {
				surfaceChoosed = 0;
			}
			else if(height <= box.getWidth() && length <= box.getHeight()){
				surfaceChoosed = 2;
			}
			break;
		case 2:
			if(length <= box.getWidth() && width <= box.getHeight()) {
				surfaceChoosed = 1;
			}
			else if(width <= box.getWidth() && length <= box.getHeight()){
				surfaceChoosed = 2;
			}
			break;
		case -1:
		break;
		}
		
	}
	
	private List<FillUnit> getSurfaceFillableUnit(Double[][] surfaceDatas,Box box
			,List<FillUnit> units){
		List<FillUnit> fillUnits = Lists.newArrayList();
		List<FillUnit> testUnits = Lists.newArrayList();
		testUnits.addAll(units);
		for(Double[] sData : surfaceDatas ){
			if(sData[0] <= box.getLength() && 
					sData[1] <= box.getWidth()){
				boolean hasAdded = false;
				for(FillUnit addedUnit : testUnits){
					if(addedUnit.getLength() == sData[0].longValue()
							&& addedUnit.getWidth() == sData[1].longValue()){
						hasAdded = true;
						break;
					}
				}
				if(hasAdded)
					continue;
				FillUnit unit = new FillUnit(this,sData[0].longValue(),sData[1].longValue());
				unit.setHeight(getChoosedHeight().longValue());
				unit.setLocation(box.getLocation());
				fillUnits.add(unit);
				testUnits.add(unit);
			}
		}
		return fillUnits;
	}
	
	public List<FillUnit> getFillableUnits(Box box) {
		List<FillUnit> fillUnits = Lists.newArrayList();
		if (surfaceChoosed != null) {
			Double[][] surfaceDatas = getChoosedSurfaceData();
			fillUnits.addAll(getSurfaceFillableUnit(surfaceDatas, box,
					fillUnits));
		} else {
			for (int i = 0; i < 3; i++) {
				surfaceChoosed = i;
				if (getChoosedHeight() <= box.getHeight()) {
					fillUnits.addAll(getSurfaceFillableUnit(
							getChoosedSurfaceData(), box, fillUnits));
				}
				surfaceChoosed = null;
			}
		}
		return fillUnits;
	}
	
	
	
	public FillUnit getEdgeMatchFillUnit(Box box){
		List<FillUnit> candidateUnits = getFillableUnits(box);
		FillUnit bestUnit = null;
		Integer bestScore = 0;
		for(int i = 0;i < candidateUnits.size();i++){
			FillUnit unit = candidateUnits.get(i);
			Integer score = 0;
			if(box.getLengthAsLong() % unit.getLength() == 0){
				score ++;
			}
			if(box.getWidthAsLong() % unit.getWidth() == 0){
				score ++;
			}
			if(box.getHeightAsLong() % unit.getHeight() == 0){
				score ++;
			}
			if(score > bestScore){
				bestScore = score;
				bestUnit = unit; 
			}
		}
		return bestUnit;
	}
	

	@Override
	public String toString() {
		return "\nItem [id=" + id + ",number=" + number + ", shape=[" + length +"," + width +"," + height + "] location=" + location
				+ ", weight=" + weight + ", ]";
	}
	
	public static void main(String[] args) {
		Item ia = new Item("itemA",new Long[]{200l,200l,150l},1l,1l);
		Box box = new Box("Box1",new Long[]{400l,300l,200l},1l,1l);
		System.out.println(ia.getEdgeMatchFillUnit(box));
	}
}
