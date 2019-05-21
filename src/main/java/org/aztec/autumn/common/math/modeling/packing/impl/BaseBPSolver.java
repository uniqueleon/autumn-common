package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContext;
import org.aztec.autumn.common.math.modeling.packing.BinPackingContextFactory;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolution;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolver;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.SaleOrder;
import org.aztec.autumn.common.math.modeling.packing.SolutionEvaluator;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResultImpl.FillType;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;

public abstract class BaseBPSolver implements BinPackingSolver {

	private static SolutionEvaluator evaluator = new FillRatioEvaluator();
	private static final Integer randomChangeItemSeqTryTime = 100;

	protected void init(BinPackingConfig config) {
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
	
	protected void clear(){
		BinPackingContextFactory.destroy();
	}
	
	private List<Box> sortBoxByVolume(List<Box> boxes){
		List<Box> sortedBoxes = Lists.newArrayList();
		sortedBoxes.addAll(boxes);
		Collections.sort(sortedBoxes,new Comparator<Box>() {

			@Override
			public int compare(Box o1, Box o2) {
				// TODO Auto-generated method stub
				return o1.getVolume() > o2.getVolume() ? 1 : -1;
			}
		});
		return sortedBoxes;
	}
	
	
	private boolean canFillAll(Box box,List<Item> remainItems){
		
		Double boxVolume = box.getVolume();
		Double itemVolume = 0d;
		for(Item item : remainItems){
			itemVolume += item.getNumber() * item.getVolume();
		}
		return boxVolume >= itemVolume;
	}
	
	private List<Item> sortItemByRandom(List<Item> items,List<Integer> histories,boolean append){
		List<Item> sortedBoxes = Lists.newArrayList();
		int[] limits = new int[items.size()];
		for(int i = 0;i < items.size();i++){
			limits[i] = items.size();
		}
		List<Integer> itemIndexes = MathUtils.getRandomCombination(null, items.size(), limits, histories);
		if(itemIndexes == null)
			return null;
		for(int i = 0;i < items.size();i++){
			sortedBoxes.add(items.get(itemIndexes.get(i)));
		}
		if(append)histories.add(itemIndexes.hashCode());
		return sortedBoxes;
	}
	
	
	private BinPackingSolution randomFillBox(BinPackingConfig config,List<Item> remainItems,int tryTime,Box box) throws BinPackingException{

		List<Integer> histories = Lists.newArrayList();
		int count = 0;
		BinPackingSolution best = null;
		tryTime = (int) (tryTime / config.getSpeed());
		while(count < tryTime){
			//List<Item> sortItems = remainItems;
			List<Item> sortItems = sortItemByRandom(remainItems,histories,true);
			if(sortItems == null)
				return best;
			Box testBox = box.clone();
			BinPackingConfig runConfig = new BinPackingConfig(sortItems, testBox, config.getSpeed());
			BinPackingSolution solution = doSolve(runConfig);
			if(solution.isSuccess())
				return solution;
			if(best == null || best.getScore() < solution.getScore()){
				best = solution;
			}
			count ++;
		}
		return best;
	}

	public BinPackingSolution solve(BinPackingConfig config) throws BinPackingException {

		SaleOrder order = config.getOrder();
		List<Box> boxes = sortBoxByVolume(order.getSelectableBoxes());
		//List<Item> sortItems = sortItemByVolume(order.getItems(), 1);
		//List<Item> sortItems = order.getItems();
		Double maxScore = 0d;
		BinPackingSolution finalSolution = null;
		for (int i = 0;i < boxes.size();i++) {
			Box box = boxes.get(i);
			Box testBox = box.clone();
			BinPackingConfig runConfig = new BinPackingConfig(order.getItems(), testBox, config.getSpeed());
			//BinPackingSolution solution = doSolve(runConfig);

			
			if(i != boxes.size() - 1 && !canFillAll(testBox, order.getItems())){
				continue;
			}

			runConfig = new BinPackingConfig(order.getItems(), testBox, config.getSpeed());
			//BinPackingSolution solution = sequenceFillBox(runConfig, order.getItems(),randomChangeItemSeqTryTime,box);
			BinPackingSolution quickSolveSolution = quickSolve(box.clone(),runConfig);
			if(quickSolveSolution.isSuccess() || quickSolveSolution.getFillRatio() == 1){
				return quickSolveSolution;
			}
			BinPackingSolution solution = randomFillBox(runConfig, order.getItems(),randomChangeItemSeqTryTime,box);
			if(solution != null && solution.isSuccess()){
				return solution;
			}
			if(quickSolveSolution.getScore() > solution.getScore()){
				solution = quickSolveSolution;
			}
			Double score = solution.getScore();
			if (score > maxScore) {
				finalSolution = solution;
				maxScore = score;
			}
			//System.out.println("score:" + score + ", use count:" + solution.getTotalUsedCount());
		}
		return finalSolution;
	}
	

	protected BinPackingSolution doSolve(BinPackingConfig config) throws BinPackingException {
		init(config);
		BinPackingSolution solution = new BinPackingSolution(config.getBox(), config.getItems());
		//List<Item> remainItem = sortItemByVolume(config.getItems(),1);
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
					//System.out.println(" empty suck!");
					fillResult = tryFill(startingBox.clone(), remainItem);
					//continue;
					//throw new BinPackingException("can't find a solution for specified box!",ErrorCodes.SOLUTION_NOT_FOUND);
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
					if(solution.getTotalUsedCount().equals(config.getItemTotalCount())){
						solution.setSuccess(true);
					}
					break;
				}
				newBoxes.addAll(PackingUtils.splitBox(startingBox, fillResult));
			}
			fillingBoxes = Box.mergeAll(newBoxes);
		} while (fillingBoxes.size() > 0 && !solution.isSuccess());
		solution.setScore(evaluator.getScore(solution));
		return solution;
	}

	protected FillResultImpl fillIn2D(BinPackingConfig config, Box box, List<Item> remainItems) {
		if (box.getLength() == 0 || box.getWidth() == 0 || box.getHeight() == 0)
			return null;
		return doFill( box.clone(),remainItems,config);
	}
	

	protected abstract FillResultImpl doFill(Box box,List<Item> remainItems,BinPackingConfig config);

	private List<Item> filt(Box box,List<Item> items){
		List<Item> fillableItems = Lists.newArrayList();
		for(Item item : items) {
			if(box.isFillable(item)) {
				fillableItems.add(item);
			}
		}
		return fillableItems;
	}
	
	
	private BinPackingSolution quickSolve(Box box,BinPackingConfig config) throws BinPackingException{
		BinPackingSolution solution = new BinPackingSolution(box, config.getItems());
		List<Item> remainItem = config.getItems();
		List<Box> fillingBoxes = Lists.newArrayList();
		fillingBoxes.add(box);
		do {
			List<Box> newBoxes = Lists.newArrayList();
			for (int i = 0; i < fillingBoxes.size(); i++) {
				Box startingBox = fillingBoxes.get(i);
				remainItem = filt(startingBox, remainItem);
				if(remainItem.size() == 0){
					remainItem = solution.getRemainItems();
					continue;
				}
				FillResultImpl fillResult = tryFill(startingBox.clone(), remainItem);
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
					if(solution.getTotalUsedCount().equals(config.getItemTotalCount())){
						solution.setSuccess(true);
					}
					break;
				}
				newBoxes.addAll(PackingUtils.splitBox(startingBox, fillResult));
			}
			fillingBoxes = Box.mergeAll(newBoxes);
		} while (fillingBoxes.size() > 0 && !solution.isSuccess());
		solution.setScore(evaluator.getScore(solution));
		return solution;
	}
	
	private FillResultImpl tryFill(final Box box, List<Item> fillItem)
			throws BinPackingException {
		Long totalCount = 0l;
		for (Item item : fillItem) {
			totalCount += item.getNumber();
		}
		List<FillUnit> fillableUnits = Lists.newArrayList();
		List<Item> volumeSortItem = Lists.newArrayList();
		volumeSortItem.addAll(fillItem);
		Collections.sort(volumeSortItem, new Comparator<Item>() {

			@Override
			public int compare(Item o1, Item o2) {
				// TODO Auto-generated method stub
				FillUnit fu1 = o1.getEdgeMatchFillUnit(box);
				FillUnit fu2 = o2.getEdgeMatchFillUnit(box);
				if(fu1 == null && fu2 != null){
					return 1;
				}
				else if(fu1 != null && fu2 == null){
					return -1;
				}
				else{
					return o1.getVolume() - o2.getVolume() > 0 ? 1 : -1;
				}
			}

		});
		Random random = new Random();
		for (Item item : volumeSortItem) {
			FillUnit unit = item.getEdgeMatchFillUnit(box);
			if(unit != null){
				unit.setLocation(box.getLocation());
				fillableUnits.add(unit);
			}
			else{
				List<FillUnit> fillUnits = item.getFillableUnits(box);
				int randomIndex = random.nextInt(fillUnits.size());
				unit = fillUnits.get(randomIndex);
				unit.setLocation(box.getLocation());
				fillableUnits.add(unit);
			}
		}
		Long base = box.getLengthAsLong();
		Long targetBase = 0l;
		List<Long> solutions = Lists.newArrayList();

		for (int i = 0;i < fillableUnits.size();i++) {
			FillUnit fillUnit = fillableUnits.get(i);
			Item item = volumeSortItem.get(i);
			Long remainBase = base - targetBase;
			if(remainBase < fillUnit.getLength()){
				solutions.add(0l);
				continue;
			}
			Long itemNum = remainBase / fillUnit.getLength();
			if(itemNum > item.getNumber()){
				itemNum = item.getNumber();
			}
			solutions.add(itemNum);
			targetBase += itemNum * fillUnit.getLength();
		}
		box.setLength(targetBase.doubleValue());

		FillUnit startPoint = new FillUnit(box,
				box.getLengthAsLong(), box.getWidthAsLong(),box.getLocation());
		FillResultImpl result = new FillResultImpl(startPoint,
				FillType.RANDOM, box.getLocation());
		//FillUnit fillTarget = 
		result.forward(totalCount,
				result.getStartPoint(), solutions,
				fillableUnits);
		return result;
	}

	
	
}
