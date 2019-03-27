package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.Location;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PackingUtils {

	public PackingUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static Long caculateMaxPossibleLength(List<Item> remainItems,BinPackingConfig config,Long lengthLimit){
		Long maxLength = 0l;
		Map<String,List<Long>> itemEdges = Maps.newConcurrentMap();
		
		for(Item item : remainItems) {
			List<Long> sortEdge = Lists.newArrayList();
			sortEdge.add(item.getWidthAsLong());
			sortEdge.add(item.getLengthAsLong());
			sortEdge.add(item.getHeightAsLong());
			sortEdge.sort(new Comparator<Long>() {

				@Override
				public int compare(Long o1, Long o2) {
					return o1 - o2 > 0 ? 1 : (o1 == o2 ? 0 : -1);
				}
				
			});
		}
		//edges.add();
		return maxLength;
	}

	public  static List<Long> quickSolve(List<Item> remainItems,BinPackingConfig config,Long maxLength){
		List<Long> retSolutions = Lists.newArrayList();
		Long consumeTime = 1l;
		for(int i = 0;i < remainItems.size();i++) {
			consumeTime *= remainItems.get(i).getNumber() * 3;
		}
		if(consumeTime > config.getMaxExhaustionTolerant()) {
			return null;
		}
		int itemSize = remainItems.size();
		int[] itemNumLimits = new int[itemSize];
		int[] edgeSelectLimits = new int[itemSize];
		for(int i = 0;i < itemSize;i++) {
			itemNumLimits[i] = remainItems.get(i).getNumber().intValue() + 1;
			edgeSelectLimits[i] = 3;
		}
		List<Integer> edgeSelectSelector = MathUtils.getNextPermutation(null, itemSize, edgeSelectLimits);
		while(edgeSelectSelector != null) {

			List<Integer> itemNumSelector = MathUtils.getNextPermutation(null, itemSize, itemNumLimits);
			itemNumSelector = MathUtils.getNextPermutation(itemNumSelector, itemSize, itemNumLimits);
			while(itemNumSelector != null) {
				Long edgeTotalLength = 0l;
				for(int i = 0;i < edgeSelectSelector.size();i++) {
					int selectIndex = edgeSelectSelector.get(i);
					Item item = remainItems.get(i);
					long edge = selectIndex == 0 ? item.getLengthAsLong() : 
						selectIndex == 1 ? item.getWidthAsLong() : item.getHeightAsLong();
					edgeTotalLength += edge * itemNumSelector.get(i);
				}
				if(edgeTotalLength > 0 && edgeTotalLength < maxLength && 
						!retSolutions.contains(edgeTotalLength)) {
					retSolutions.add(edgeTotalLength);
				}
				itemNumSelector = MathUtils.getNextPermutation(itemNumSelector, itemSize, itemNumLimits);
			}
			edgeSelectSelector = MathUtils.getNextPermutation(edgeSelectSelector, itemSize, edgeSelectLimits);
		}
		retSolutions.sort(new Comparator<Long>() {

			@Override
			public int compare(Long o1, Long o2) {
				return o2 > o1 ? 1 : -1;
			}
		});
		return retSolutions;
	}
	

	public static long calculateLowerLimit(int minimunCombines, List<Item> remainItems) {
		long lowerLimit = 0l;
		List<Long> edges = Lists.newArrayList();
		for (Item item : remainItems) {
			edges.add(item.getLength().longValue());
			edges.add(item.getHeight().longValue());
			edges.add(item.getWidthAsLong());
		}
		edges.sort(new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				return new Long(o1 - o2).intValue();
			}
		});
		for (int i = 0; i < minimunCombines; i++) {
			lowerLimit += edges.get(i);
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

	public static List<Box> splitBox(Box box, FillResult lastResult) {
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
			Box newBox = new Box(box.getId(), new Long[] { unfillUnit.getBase(), width, box.getHeightAsLong() },
					box.getWeight().longValue(), box.getNumber());
			newBox.setLocation(unfillUnit.getLocation().clone());
			newBox.setSurfaceChoosed(0);
			newBoxes.add(newBox);
		}
		newBoxes.addAll(calculateBoxesOnTheTop(box,lastResult));
		//newBoxes = Box.mergeAll(newBoxes);
		return newBoxes;
	}

	private static List<Box> calculateBoxesOnTheTop(Box box,FillResult fillResult){
		List<Box> boxes = Lists.newArrayList();
		List<FillUnit> fillUnits = fillResult.getAllFillUnits();
		for(FillUnit fillUnit : fillUnits) {
			Location location = fillUnit.getLocation().clone();
			Item originItem = (Item) fillUnit.getBelongObject();
			Long itemHeight = originItem.getHeight(fillUnit.getBase(), fillUnit.getHeight());
			Long boxHeight = (box.getLocation().getZ().longValue() + box.getHeight().longValue())
					- location.getZ().longValue() - itemHeight;
			Box newBox = new Box(box.getId(), new Long[] {fillUnit.getBase(),
					fillUnit.getHeight(),boxHeight}, box.getWeight().longValue(), 1l);
			location.setZ(location.getZ() + itemHeight);
			newBox.setLocation(location);
			boxes.add(newBox);
		}
		return boxes;
	}
}
