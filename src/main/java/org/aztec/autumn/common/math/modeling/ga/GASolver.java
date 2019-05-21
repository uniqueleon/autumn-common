package org.aztec.autumn.common.math.modeling.ga;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aztec.autumn.common.algorithm.AlgorithmException;
import org.aztec.autumn.common.algorithm.Result;
import org.aztec.autumn.common.algorithm.genetic.GeneticAlgorithmRunner;
import org.aztec.autumn.common.math.modeling.packing.AlgorithmContext;
import org.aztec.autumn.common.math.modeling.packing.AlgorithmContextFactory;
import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolution;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolver;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.SaleOrder;
import org.aztec.autumn.common.math.modeling.packing.SolutionEvaluator;
import org.aztec.autumn.common.math.modeling.packing.impl.FillRatioEvaluator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GASolver implements BinPackingSolver {
	

	private static SolutionEvaluator evaluator = new FillRatioEvaluator();

	@Override
	public BinPackingSolution solve(BinPackingConfig config) {

		SaleOrder so = config.getOrder();
		checkOrder(so);
		BinPackingSolution best = null;
		List<Box> sortedBox = sortBox(so);
		initContext(so.getItems());
		for(Box box : sortedBox){
			BinPackingConfig newConfig = new BinPackingConfig(so.getItems(), box, config.getSpeed());
			BinPackingSolution bps = getSolution(newConfig);
			bps.setScore(evaluator.getScore(bps));
			if(bps.isSuccess()){
				return bps;
			}
			if(best == null || best.getScore() < bps.getScore()){
				best = bps;
			}
		}
		AlgorithmContextFactory.clearContext();
		return best;
	}
	
	private void checkOrder(SaleOrder so){
		Map<String,Item> newItems = Maps.newHashMap();
		for(Item item : so.getItems()){
			String itemId = item.getId();
			if(newItems.containsKey(itemId)){
				Item duplicateItem = newItems.get(itemId);
				duplicateItem.setNumber(duplicateItem.getNumber() + item.getNumber());
			}
			else{
				newItems.put(itemId, item.clone());
			}
		}
		List<Item> newItemList = Lists.newArrayList();
		newItemList.addAll(newItems.values());
		so.setItems(newItemList);
	}
	
	private void initContext(List<Item> items){
		AlgorithmContextFactory.initContext();
		Map<String,Long> itemNums = new ConcurrentHashMap<String, Long>();
		for(Item item : items){
			itemNums.put(item.getId(), item.getNumber());
		}
		AlgorithmContext context = AlgorithmContextFactory.getContext();
		context.put(AlgorithmContextFactory.ContextKeys.ITEM_NUMS, itemNums);
	}
	
	
	private List<Box> sortBox(SaleOrder so){

		List<Box> sortedBox = so.getSelectableBoxes();
		Collections.sort(sortedBox,new Comparator<Box>() {

			@Override
			public int compare(Box o1, Box o2) {
				// TODO Auto-generated method stub
				return o1.getVolume() - o2.getVolume() > 0 ? 1 : -1;
			}
		});
		List<Box> retBox = Lists.newArrayList();
		List<Item> items = so.getItems();
		Long itemTotalVolumn = 0l;
		boolean volumnHasBeanCounted = false;
		for(Box testBox : sortedBox){
			boolean situable = true;
			//���������͵����ų���
			for(Item item : items){
				if(!testBox.isFillable(item)){
					situable = false;
				}
				if(!volumnHasBeanCounted){
					itemTotalVolumn += item.getVolume().longValue() * item.getNumber();
				}
			}
			if(!volumnHasBeanCounted){
				volumnHasBeanCounted = true;
			}
			if(situable){
				if(testBox.getVolume() > itemTotalVolumn){
					retBox.add(testBox);
				}
			}
		}
		for(int i = 0;i < sortedBox.size();i++){
			Box targetBox = sortedBox.get(i);
			if(!retBox.contains(targetBox) && !isNoFit(targetBox, items)){
				retBox.add(targetBox);
			}
		}
		return retBox;
	}
	
	private boolean isNoFit(Box box,List<Item> items){
		
		for(Item item : items){
			if(box.isFillable(item)){
				return false;
			}
		}
		return true;
	}
	
	private Integer getRealPopulation(BinPackingConfig config){
		Integer possibleCount = GeneUtils.countPossiblePlan(config.getBox(), config.getItems());
		Integer defaultPops = new Double(Math.floor(AlgorithmConst.DEFAULT_POPULATIONS
				/ config.getSpeed())).intValue();
		return possibleCount > defaultPops ? defaultPops : possibleCount;
	}
	
	
	private BinPackingSolution getSolution(BinPackingConfig config){
		try {
			config.setPopulations(getRealPopulation(config));
			BinPackingChaos bpc = new BinPackingChaos(config);
			GeneticAlgorithmRunner runner = new GeneticAlgorithmRunner(bpc);
			runner.run();
			Result algorithmResult = runner.getResult();
			PackingPlan plan = algorithmResult.adapt(PackingPlan.class);
			if(plan.getGenes().length > 0){
				plan.sortStep();
			}
			BinPackingSolution solution = toSolution(config, plan);
			return solution;
		} catch (AlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private BinPackingSolution toSolution(BinPackingConfig config,PackingPlan plan){
		BinPackingSolution solution = new BinPackingSolution(config.getBox(), config.getItems());
		solution.push(plan);
		return solution;
	}
	
}
