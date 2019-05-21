package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.Collections;
import java.util.List;

import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.Location;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResultImpl.FillType;

import com.google.common.collect.Lists;

public class PackingUtils {

	public PackingUtils() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static long calculateUpperLimit(int minimunCombines, List<Item> remainItems){
		long upperLimit = 0l;
		for (Item item : remainItems) {
			upperLimit += item.getLongestEdge().longValue() * item.getNumber();
		}
		return upperLimit;
	}
	
	public static long calculateLowerLimit(List<List<Long>> edges){
		List<Long> sortEdges = Lists.newArrayList();
		for(List<Long> itemEdges : edges){
			sortEdges.addAll(itemEdges);
		}
		return sortEdges.get(0);
	}
	
	public static long calculateUpperLimit(List<List<Long>> edges,List<Item> items){
		long maxLength = 0;
		for(int i = 0;i < items.size();i++){
			Long number = items.get(i).getNumber();
			List<Long> sortEdges = Lists.newArrayList();
			sortEdges.addAll(edges.get(i));
			Collections.sort(sortEdges);
			maxLength += sortEdges.get(sortEdges.size() - 1) * number;
		}
		return maxLength;
	}
	

	public static long calculateLowerLimit(int minimunCombines, List<Item> remainItems) {
		long lowerLimit = Long.MAX_VALUE;
		for (Item item : remainItems) {
			if(lowerLimit >  item.getShortestEdge().longValue()){
				lowerLimit = item.getShortestEdge().longValue();
			}
		}
		return lowerLimit;
	}

	public static long calculateMinStep(List<Item> remainItems) {
		List<Long> edges = Lists.newArrayList();
		for (Item item : remainItems) {
			edges.add(item.getLength().longValue());
			edges.add(item.getHeight().longValue());
			edges.add(item.getWidthAsLong());
		}
		GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(edges.toArray(new Long[edges.size()]));
		return gcd.getGcd();
	}
	
	public static List<List<Long>> getFillableEdges(List<Item> remainItems,Box box){
		List<List<Long>> edges = Lists.newArrayList();
		for (Item item : remainItems) {
			edges.add(item.getFillableEdges(box));
		}
		return edges;
	}
	
	public static long calculateMinStepByEdge(List<List<Long>> edges) {
		List<Long> flatEdges = Lists.newArrayList();
		for(List<Long> itemEdges : edges){
			flatEdges.addAll(itemEdges);
		}
		GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(flatEdges.toArray(new Long[flatEdges.size()]));
		return gcd.getGcd();
	}
	

	public static List<Box> splitBox(Box box, FillResultImpl lastResult) {
		Box shrinkBox = (Box) lastResult.getStartPoint().getBelongObject();
		List<Box> newBoxes = Lists.newArrayList();
		Long lengthDis = box.getLengthAsLong() - shrinkBox.getLengthAsLong();
		Long widthDis = box.getWidthAsLong() - shrinkBox.getWidthAsLong();

		if (lengthDis != 0 ) {
			Location newLocation = box.getLocation().clone();
			newLocation.setX(box.getLocation().getX() + shrinkBox.getLength());
			Box newBox1 = new Box(box.getId(),
					new Long[] { lengthDis, box.getWidthAsLong(), box.getHeightAsLong() },
					box.getWeight().longValue(), box.getNumber());
			newBox1.setLocation(newLocation);
			newBox1.setSurfaceChoosed(0);
			newBoxes.add(newBox1);
		}
		if(widthDis != 0) {
			Location newLocation = box.getLocation().clone();
			Box newBox2 = new Box(box.getId(),
					new Long[] { box.getLengthAsLong() - lengthDis, widthDis, box.getHeightAsLong() },
					box.getWeight().longValue(), box.getNumber());
			newLocation.setY(box.getLocation().getY() + shrinkBox.getWidth());
			newBox2.setSurfaceChoosed(0);
			newBoxes.add(newBox2);
		}
		List<FillUnit> unfilledArea = lastResult.getUnfilledArea();
		for (FillUnit unfillUnit : unfilledArea) {
			Long width = new Double((box.getLocation().getY() + box.getWidth()) - unfillUnit.getLocation().getY())
					.longValue();
			Box newBox = new Box(box.getId(), new Long[] { unfillUnit.getLength(), width, box.getHeightAsLong() },
					box.getWeight().longValue(), box.getNumber());
			newBox.setLocation(unfillUnit.getLocation().clone());
			newBox.setSurfaceChoosed(0);
			newBoxes.add(newBox);
		}
		newBoxes.addAll(calculateBoxesOnTheTop(box,lastResult));
		return newBoxes;
	}

	private static List<Box> calculateBoxesOnTheTop(Box box,FillResultImpl fillResult){
		List<Box> boxes = Lists.newArrayList();
		List<FillUnit> fillUnits = fillResult.getAllFillUnits();
		for(FillUnit fillUnit : fillUnits) {
			Location location = fillUnit.getLocation().clone();
			Item originItem = (Item) fillUnit.getBelongObject();
			Long itemHeight = originItem.getHeight(fillUnit.getLength(), fillUnit.getWidth());
			Long boxHeight = (box.getLocation().getZ().longValue() + box.getHeight().longValue())
					- location.getZ().longValue() - itemHeight;
			Box newBox = new Box(box.getId(), new Long[] {fillUnit.getLength(),
					fillUnit.getWidth(),boxHeight}, box.getWeight().longValue(), 1l);
			location.setZ(location.getZ() + itemHeight);
			newBox.setLocation(location);
			boxes.add(newBox);
		}
		return boxes;
	}
	
	

	public static Long getSolutionScore(List<Long> solutions,Long[] factors,Long[] heights){
		Long score = 0l;
		Long tmpScore = Long.MAX_VALUE;
		//Long tmpScore = 0l;
		Long tmpHeight = 0l;
		//List<Long> volumes = Lists.newArrayList();
		List<Long> bases = Lists.newArrayList();
		for(int i = 0;i < solutions.size();i++){
			if(solutions.get(i) == 0)
				continue;
			Long testNum = solutions.get(i);
			if(tmpHeight == 0){
				tmpHeight = heights[i];
				tmpScore = testNum * factors[i];
				score = tmpScore;
			}
			else if (tmpHeight.equals(heights[i])){
				tmpScore += testNum * factors[i];
			}
			else{
				bases.add(tmpScore);
				if(tmpScore < score){
				//if(tmpScore > score){
					score = tmpScore;
				}
				tmpHeight = heights[i];
				tmpScore = testNum * factors[i];
			}
			if(i == solutions.size() - 1){
				bases.add(tmpScore);
				if(tmpScore < score){
				//if(tmpScore > score){
					score = tmpScore;
				}
			}
			
		}
		return score;
	}
	
	
	public static List<Integer> initSurfaceIndex(Box box,List<Item> items){
		List<Integer> surfaceIndexes = Lists.newArrayList();
		for(int i = 0;i < items.size();i++){
			//if()
		}
		return surfaceIndexes;
	}
	
	public static FillParameter getFillParams(Box box,List<Item> items,
			List<List<Long>> edges,
			int paramSize,
			boolean random){
		FillParameter fillParam = new FillParameter(box, items, FillType.SURFACE);
		int itemSize = items.size();
		List<Item> fillableItems = Lists.newArrayList();
		fillableItems.addAll(items);
		
		//return params;
		return null;
	}
	
	private static List<List<FillUnit>> getAllFillableUnits(Box box,List<Item> items){
		List<List<FillUnit>> allFillableUnits = Lists.newArrayList();
		for(int i = 0;i < items.size();i++){
			List<FillUnit> fillUnits = getFillableUnits(box, items.get(i));
			if(fillUnits.size() == 0)
				return null;
			allFillableUnits.add(fillUnits);
		}
		return allFillableUnits;
	}

	public static List<FillUnit> getFillableUnits(Box box, Item item) {
		List<FillUnit> fillUnits = Lists.newArrayList();
		if (item.getChoosedHeight() < box.getHeight()) {
			for (Double[] areaData : item.getChoosedSurfaceData()) {
				if (areaData[0] < box.getLength()
						&& areaData[1] < box.getWidth()) {
					FillUnit fillUnit = new FillUnit(item,
							areaData[0].longValue(),
							areaData[1].longValue());
					fillUnit.setLocation(box.getLocation());
					fillUnits.add(fillUnit);
				}
			}
		}
		return fillUnits;
	}
	

	public static List<Integer> reselectSurface(int index,List<Integer> indexes,List<Item> items,Box box) {
		List<Integer> newIndexes = Lists.newArrayList();
		newIndexes.addAll(indexes);
		Integer surfaceIndex = new Integer(newIndexes.get(index));
		Item item = items.get(index);
		while(!box.isFillable(item)) {
			if(surfaceIndex < 2) {
				surfaceIndex ++;
			}
			else {
				boolean overflow = false;
				boolean isChangable = false;
				if(index == indexes.size() - 1)
					return null;
				for(int i = index + 1;i < indexes.size();i++){
					Item testItem = items.get(i).clone();
					int oriIndex = indexes.get(i);
					int nextIndex = indexes.get(i);
					do{
						nextIndex ++;
						if(nextIndex > 2){
							nextIndex = 0;
						}
						testItem.setSurfaceChoosed(nextIndex);
					} while(!box.isFillable(testItem));
					if(oriIndex >= nextIndex){
						overflow = true;
					}
					else {
						overflow = false;
					}
					if(!overflow){
						surfaceIndex = 0;
						isChangable = true;
						break;
					}
					else if(i == indexes.size() - 1)
						return null;
				}
				if(!isChangable)
					return null;
			}
			item.setSurfaceChoosed(surfaceIndex);
		}
		newIndexes.set(index, surfaceIndex);
		return newIndexes;
	}
	
	
	
}
