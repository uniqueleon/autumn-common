package org.aztec.autumn.common.math.modeling.packing;

import java.util.Collections;
import java.util.List;

import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContext.ContextKeys;
import org.aztec.autumn.common.math.modeling.packing.impl.FillParameter;
import org.aztec.autumn.common.math.modeling.packing.impl.FillRatioEvaluator;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResult;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResult.FillType;
import org.aztec.autumn.common.math.modeling.packing.impl.PackingUtils;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;

public class BinPackingSolver {

	private static SolutionEvaluator evaluator = new FillRatioEvaluator();

	private void init(BinPackingConfig config) {
		BinPackingContext context = BinPackingContextFactory.build(config);
		List<Long> edges = Lists.newArrayList();
		for (Item configItem : config.getItems()) {
			edges.add(configItem.getWidthAsLong());
			edges.add(configItem.getHeightAsLong());
			edges.add(configItem.getLengthAsLong());
		}
		Collections.sort(edges);
		context.put(ContextKeys.SORT_EDGES, edges);
	}
	

	public BinPackingSolution solve(SaleOrder order,BinPackingConfig config) {

		List<Box> boxes = order.getSelectableBoxes();
		BinPackingSolution finalSolution = null;
		Double maxScore = 0d;
		
		for (Box box : boxes) {
			Box testBox = box.clone();
			BinPackingConfig runConfig = new BinPackingConfig(order.getItems(), testBox, config.getMinimumCombines(), config.getSatisfiedValve());
			runConfig.setLevel1TryTime(config.getLevel1TryTime());
			BinPackingSolution solution = solve(runConfig);
			Double score = solution.getScore();
			if (score > maxScore) {
				finalSolution = solution;
				maxScore = score;
			}
			System.out.println("score:" + score + ", use count:" + solution.getTotalUsedCount());
		}
		return finalSolution;
	}

	public BinPackingSolution solve(BinPackingConfig config) {
		init(config);
		BinPackingSolution solution = new BinPackingSolution(config.getBox(), config.getItems());
		List<Item> remainItem = config.getItems();
		List<Box> fillingBoxes = Lists.newArrayList();
		fillingBoxes.add(config.getBox());
		do {

			List<Box> newBoxes = Lists.newArrayList();
			for (int i = 0; i < fillingBoxes.size(); i++) {
				Box startingBox = fillingBoxes.get(i);
				remainItem = filt(startingBox, remainItem);
				if(remainItem.size() == 0){
					remainItem = solution.getRemainItems();
					continue;
				}
				FillResult fillResult = fillIn2D(config, startingBox, remainItem);
				if (fillResult == null)
					continue;
				if (fillResult.score() > 0) {
					solution.push(fillResult);
				} else {
					continue;
				}
				if (solution.isSuccess()) {
					break;
				}
				remainItem = solution.getRemainItems();
				if (remainItem.size() == 0) {
					solution.setSuccess(true);
					break;
				}
				newBoxes.addAll(PackingUtils.splitBox(startingBox, fillResult));
			}
			fillingBoxes = Box.mergeAll(newBoxes);
		} while (fillingBoxes.size() > 0 && !solution.isSuccess());
		solution.setScore(evaluator.getScore(solution));
		return solution;
	}

	private FillResult fillIn2D(BinPackingConfig config, Box box, List<Item> remainItems) {
		if (box.getLength() == 0 || box.getWidth() == 0 || box.getHeight() == 0)
			return null;
		List<List<Integer>> chooseSurface = trySolveSurfaceEquation(remainItems, box, config.getLevel1TryTime());
		return chooseSurface.size() > 0 ? fillBySelectedSurface(chooseSurface, config, remainItems, box.clone()) : 
			fillByExhaution(config, remainItems, box.clone());
	}

	
	private static FillResult fillByExhaution(BinPackingConfig config,List<Item> items,Box tmpBox){

		RectangleSurfaceFiller filler = new RectangleSurfaceFiller();
		Long itemCount = 0l;
		for (Item item : items) {
			itemCount += item.getNumber();
		}
		long step = PackingUtils.calculateMinStep(items);
		long base = PackingUtils.calculateLowerLimit(config.getMinimumCombines(), items);
		tmpBox.resize(step, true);
		boolean onlyBottom = true;
		int[] limits = new int[items.size()];
		for (int i = 0; i < items.size(); i++) {
			limits[i] = 3;
		}
		FillResult fillResult = null;
		FillResult bestResult = null;

		Integer tryTime = config.getLevel1TryTime();
		boolean shrinkable = true;
		do {
			List<Integer> surfaceIndexes = MathUtils.getNextPermutation(null, items.size(), limits);
			while (surfaceIndexes != null) {
				boolean isSkip = false;
				for (int i = 0; i < surfaceIndexes.size(); i++) {
					Item testItem = items.get(i);
					testItem.setSurfaceChoosed(surfaceIndexes.get(i));
					if (testItem.getChoosedHeight() > tmpBox.getHeight()) {
						surfaceIndexes = reselectSurface(i, surfaceIndexes, testItem, tmpBox);
						if(surfaceIndexes == null) {
							tryTime = 0;
							break;
						}
					}
				}
				if (!isSkip) {
					fillResult = filler.fill(new FillParameter(tmpBox.clone(), items, FillType.SURFACE));
					if (fillResult.score() > 0) {
						return fillResult;
					}
					/*if (bestResult == null || bestResult.score() < fillResult.score()) {
						bestResult = fillResult.clone();
					}
					if (bestResult.score() >= itemCount) {
						return bestResult;
					}*/
				}
				surfaceIndexes = MathUtils.getNextPermutation(surfaceIndexes, items.size(), limits);
				tryTime--;
				if (tryTime <= 0)
					break;
			}
			shrinkable = tmpBox.isShrinkable(step, base, onlyBottom);
			if (!shrinkable)
				break;
			tmpBox.shrink(step, base, onlyBottom);
			tryTime = config.getLevel1TryTime();
		} while (shrinkable && tryTime > 0);
		return bestResult;
	}
	
	public static FillResult fillBySelectedSurface(List<List<Integer>> chooseSurfaces,BinPackingConfig config,List<Item> items,Box tmpBox){

		FillResult fillResult = null;
		FillResult bestResult = null;
		Long itemCount = 0l;
		for (Item item : items) {
			itemCount += item.getNumber();
		}
		RectangleSurfaceFiller filler = new RectangleSurfaceFiller();
		for(List<Integer> chooseSurface : chooseSurfaces){
			for(int i = 0;i < items.size();i++){
				Item item = items.get(i);
				item.setSurfaceChoosed(chooseSurface.get(i));
			}
			fillResult = filler.fill(new FillParameter(tmpBox.clone(), items, FillType.SURFACE));
			if (bestResult == null || bestResult.score() < fillResult.score()) {
				bestResult = fillResult.clone();
			}
			if (bestResult.score() >= itemCount) {
				return bestResult;
			}
		}
		return bestResult;
	}

	private static List<Integer> reselectSurface(int index,List<Integer> indexes,Item item,Box box) {
		List<Integer> newIndexes = Lists.newArrayList();
		newIndexes.addAll(indexes);
		Integer surfaceIndex = newIndexes.get(index);
		Integer tryTime = 2;
		while(item.getChoosedHeight() > box.getHeight()) {
			if(surfaceIndex < 2) {
				surfaceIndex ++;
			}
			else {
				surfaceIndex = 0;
			}
			item.setSurfaceChoosed(surfaceIndex);
			tryTime --;
			if(tryTime == 0) {
				return null;
			}
		}
		newIndexes.set(index, surfaceIndex);
		return newIndexes;
	}

	private List<Item> filt(Box box,List<Item> items){
		List<Item> fillableItems = Lists.newArrayList();
		for(Item item : items) {
			if(box.isFillable(item)) {
				fillableItems.add(item);
			}
		}
		return fillableItems;
	}
	
	private List<List<Integer>> trySolveSurfaceEquation(List<Item> items,Box box,int tryTime){
		List<List<Long>> areaDatas = Lists.newArrayList();
		int itemSize = items.size();
		Long boxSurface = box.getLengthAsLong() * box.getHeightAsLong(); 
		for(int i = 0;i < items.size();i++){
			Item item = items.get(i);
			List<Long> areaData = Lists.newArrayList();
			areaData.add(item.getLength().longValue() * item.getWidth().longValue());
			areaData.add(item.getLength().longValue() * item.getHeight().longValue());
			areaData.add(item.getHeight().longValue() * item.getWidth().longValue());
			areaDatas.add(areaData);
		}
		int[] limits = new int[items.size()];
		for(int i = 0;i < items.size();i++){
			limits[i] = 3;
		}
		Long[][] ranges = new Long[itemSize][2];
		for(int i = 0;i < ranges.length;i++){
			ranges[i][0] = 0l;
			ranges[i][1] = items.get(i).getNumber();
		}
		List<Integer> chooseIndexes = MathUtils.getNextPermutation(null, items.size(), limits);
		int count = 0;
		List<List<Integer>> suitableSurface = Lists.newArrayList();
		while(chooseIndexes != null && count < tryTime){
			Long[] factors = new Long[itemSize];
			for(int i = 0;i < chooseIndexes.size();i++){
				factors[i] = areaDatas.get(i).get(chooseIndexes.get(i));
			}
			DiophantineResult dr = DiophantineEquation.getSolution(factors, boxSurface,ranges);
			if(dr != null && dr.getSolutions().size() > 0 && isItemFillable(items, box, chooseIndexes)){
				suitableSurface.add(chooseIndexes);
			}
			chooseIndexes = MathUtils.getNextPermutation(chooseIndexes, itemSize, limits);
			count ++;
		}
		return suitableSurface;
	}
	
	private boolean isItemFillable(List<Item> items,Box box,List<Integer> surfaceIndex){
		for(int i = 0;i < items.size();i++){
			Item item = items.get(i);
			item.setSurfaceChoosed(surfaceIndex.get(i));
			if(item.getChoosedHeight() > box.getHeight()){
				return false;
			}
		}
		return true;
	}
}
