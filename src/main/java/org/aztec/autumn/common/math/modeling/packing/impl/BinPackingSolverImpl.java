package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.Collections;
import java.util.List;

import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.modeling.packing.AlgorithmContextFactory.ContextKeys;
import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContext;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContextFactory;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolution;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolver;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.SaleOrder;
import org.aztec.autumn.common.math.modeling.packing.SolutionEvaluator;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResultImpl.FillType;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;

public class BinPackingSolverImpl implements BinPackingSolver{

	private static SolutionEvaluator evaluator = new FillRatioEvaluator();

	private void init(BinPackingConfig config) {
		clear();
		BinPackingContext context = BinPackingContextFactory.build(config);
		List<Long> edges = Lists.newArrayList();
		for (Item configItem : config.getItems()) {
			edges.add(configItem.getWidthAsLong());
			edges.add(configItem.getHeightAsLong());
			edges.add(configItem.getLengthAsLong());
		}
		Collections.sort(edges);
		context.put(BinPackingContext.ContextKeys.SORT_EDGES, edges);
	}
	
	private void clear(){
		BinPackingContextFactory.destroy();
	}
	
	

	public BinPackingSolution solve(BinPackingConfig config) {

		SaleOrder order = config.getOrder();
		List<Box> boxes = order.getSelectableBoxes();
		BinPackingSolution finalSolution = null;
		Double maxScore = 0d;
		
		for (Box box : boxes) {
			Box testBox = box.clone();
			BinPackingConfig runConfig = new BinPackingConfig(order.getItems(), testBox, config.getSpeed());
			BinPackingSolution solution = doSolve(runConfig);
			Double score = solution.getScore();
			if (score > maxScore) {
				finalSolution = solution;
				maxScore = score;
			}
			System.out.println("score:" + score + ", use count:" + solution.getTotalUsedCount());
		}
		return finalSolution;
	}

	private BinPackingSolution doSolve(BinPackingConfig config) {
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
				FillResultImpl fillResult = fillIn2D(config, startingBox, remainItem);
				if (fillResult == null){
					continue;
				}
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

	private FillResultImpl fillIn2D(BinPackingConfig config, Box box, List<Item> remainItems) {
		if (box.getLength() == 0 || box.getWidth() == 0 || box.getHeight() == 0)
			return null;
		//List<List<Integer>> chooseSurface = trySolveSurfaceEquation(remainItems, box, config.getLevel1TryTime());
		/*List<List<Integer>> chooseSurface = Lists.newArrayList();
		return chooseSurface.size() > 0 ? fillBySelectedSurface(chooseSurface, config, remainItems, box.clone()) : 
			fillByExhaution(config, remainItems, box.clone());*/
		return fillByExhaution(config, remainItems, box.clone());
		//return fillByRandom(config, remainItems, box.clone());
		//return fillByOnce(config, remainItems, box.clone());
	}
	

	
	
	private static FillResultImpl fillByExhaution(BinPackingConfig config,List<Item> items,Box tmpBox){

		RectangleSurfaceFiller filler = new RectangleSurfaceFiller();
		Long itemCount = 0l;
		for (Item item : items) {
			itemCount += item.getNumber();
		}
		long step = PackingUtils.calculateMinStep(items);
		long upperLimit = PackingUtils.calculateUpperLimit(config.getMinimumCombines(), items);
		long lowerLimit = PackingUtils.calculateLowerLimit(config.getMinimumCombines(), items);
		if(upperLimit < tmpBox.getLength()){
			tmpBox.setLength(new Double(upperLimit));
		}
		tmpBox.resize(step, true,true);
		int[] limits = new int[items.size()];
		for (int i = 0; i < items.size(); i++) {
			limits[i] = 3;
		}
		FillResultImpl fillResult = null;
		FillResultImpl bestResult = null;

		Integer tryTime = config.getLevel1TryTime();
		Integer count = 0;
		boolean shrinkable = true;
		do {
			List<Integer> surfaceIndexes = MathUtils.getNextPermutation(null, items.size(), limits);
			while (surfaceIndexes != null) {
				boolean isSkip = false;
				for (int i = 0; i < surfaceIndexes.size(); i++) {
					Item testItem = items.get(i);
					testItem.setSurfaceChoosed(surfaceIndexes.get(i));
					if (!tmpBox.isFillable(testItem)) {
						List<Integer> tmpIndexes = PackingUtils.reselectSurface(i, surfaceIndexes, items, tmpBox);
						if(tmpIndexes == null) {
							isSkip = true;
							surfaceIndexes = null;
							break;
							
						}
					}
				}
				if (!isSkip) {
					Long curTime = System.currentTimeMillis();
					fillResult = filler.fill(new FillParameter(tmpBox.clone(), items, FillType.SURFACE));
					if (fillResult.score() > 0) {
						return fillResult;
					}
					/*fillResult = filler.fill(new FillParameter(tmpBox.clone(), items, FillType.SURFACE));
					if (bestResult == null || bestResult.score() < fillResult.score()) {
						bestResult = fillResult.clone();
					}
					if (bestResult.score() >= itemCount) {
						return bestResult;
					}*/
					surfaceIndexes = MathUtils.getNextPermutation(surfaceIndexes, items.size(), limits);
					/**/
					Long fillUsedTime = System.currentTimeMillis()- curTime;
					//System.out.println("fill use time:" + fillUsedTime);
					count ++;
					if(count > tryTime){
						return fillResult;
					}
				}
				/**/
			}
			//shrinkable = tmpBox.isShrinkable(step, lowerLimit, onlyBottom);
			shrinkable = tmpBox.getLength().longValue() >= lowerLimit;
			
			if (!shrinkable)
				break;
			//tmpBox.shrink(step, lowerLimit, onlyBottom);
			tmpBox.setLength(tmpBox.getLength() - step);
		} while (shrinkable);
		return bestResult;
	}
	
	private static FillResultImpl fillByRandom(BinPackingConfig config,List<Item> items,Box tmpBox){

		RectangleSurfaceFiller filler = new RectangleSurfaceFiller();
		Long itemCount = 0l;
		for (Item item : items) {
			itemCount += item.getNumber();
		}
		long step = PackingUtils.calculateMinStep(items);
		long upperLimit = PackingUtils.calculateUpperLimit(config.getMinimumCombines(), items);
		long lowerLimit = PackingUtils.calculateLowerLimit(config.getMinimumCombines(), items);
		if(upperLimit < tmpBox.getLength()){
			tmpBox.setLength(new Double(upperLimit));
		}
		tmpBox.resize(step, true,true);
		int[] limits = new int[items.size()];
		for (int i = 0; i < items.size(); i++) {
			limits[i] = 3;
		}
		FillResultImpl fillResult = null;
		FillResultImpl bestResult = null;

		Integer tryTime = config.getLevel1TryTime();
		Integer count = 0;
		boolean shrinkable = true;
		List<Integer> histories = Lists.newArrayList();
		do {
			histories.clear();
			List<Integer> surfaceIndexes = MathUtils.getRandomPermutation(null, items.size(), limits, histories);
			
			while (surfaceIndexes != null) {
				histories.add(surfaceIndexes.hashCode());
				boolean isSkip = false;
				for (int i = 0; i < items.size(); i++) {
					Item testItem = items.get(i);
					testItem.setSurfaceChoosed(surfaceIndexes.get(i));
					if (testItem.getChoosedHeight() > tmpBox.getHeight()) {
						isSkip = true;
						break;
					}
				}
				if (!isSkip) {
					/*fillResult = filler.fill(new FillParameter(tmpBox.clone(), items, FillType.SURFACE));
					if (fillResult.score() > 0) {
						return fillResult;
					}*/
					//Long fillUsedTime = System.currentTimeMillis()- curTime;
					fillResult = filler.fill(new FillParameter(tmpBox.clone(), items, FillType.SURFACE));
					if (bestResult == null || bestResult.score() < fillResult.score()) {
						bestResult = fillResult.clone();
					}
					if (bestResult.score() >= itemCount) {
						return bestResult;
					}
					
				}
				surfaceIndexes = MathUtils.getRandomPermutation(surfaceIndexes, items.size(), limits,histories);
				if(surfaceIndexes == null){
					return null;
				}
				histories.add(surfaceIndexes.hashCode());
				/**/
				//System.out.println("fill use time:" + fillUsedTime);
				count ++;
				if(count > tryTime){
					return fillResult;
				}
				/**/
			}
			//shrinkable = tmpBox.isShrinkable(step, lowerLimit, onlyBottom);
			shrinkable = tmpBox.getLength().longValue() >= lowerLimit;
			if (!shrinkable)
				break;
			//tmpBox.shrink(step, lowerLimit, onlyBottom);
			tmpBox.setLength(tmpBox.getLength() - step);
		} while (shrinkable);
		return bestResult;
	}
	
	public static FillResultImpl fillBySelectedSurface(List<List<Integer>> chooseSurfaces,BinPackingConfig config,List<Item> items,Box tmpBox){

		FillResultImpl fillResult = null;
		FillResultImpl bestResult = null;
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
			if(fillResult != null){
				return fillResult;
			}
			/*if (bestResult == null || bestResult.score() < fillResult.score()) {
				bestResult = fillResult.clone();
			}
			if (bestResult.score() >= itemCount) {
				return bestResult;
			}*/
		}
		return bestResult;
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
