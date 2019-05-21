package org.aztec.autumn.common.math.modeling.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.math.modeling.packing.FillResult;
import org.aztec.autumn.common.math.modeling.packing.FillStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PackingPlan implements Individual,FillResult {

	private Gene[] steps = new PackingStep[0];
	private static final Logger LOG = LoggerFactory.getLogger(PackingPlan.class);
	private Map<String,Long> itemCount = Maps.newHashMap();
	
	public PackingPlan(Gene[] steps) {
		super();
		this.steps = steps;
	}
	

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getAsMap() {
		Map<String,Object> genesMap = Maps.newHashMap();
		for(Gene gene : steps){
			PackingStep ps = (PackingStep) gene;
			genesMap.put("" + ps.getNo(), ps);
		}
		return genesMap;
	}

	@Override
	public Gene[] getGenes() {
		// TODO Auto-generated method stub
		return steps;
	}
	
	public Gene[] cloneGenes() throws CloneNotSupportedException{
		Gene[] newGenes = new Gene[steps.length];
		for(int i = 0;i < newGenes.length;i++){
			newGenes[i] = steps[i].clone();
		}
		return newGenes;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Individual couple(Individual individual) {
		PackingPlan plan1 = adapt(PackingPlan.class);
		PackingPlan plan2 = individual.adapt(PackingPlan.class);
		try {
			List<Gene> excGenes = pickupGenes(plan2, GeneUtils.getCompatibleGenes(plan1, plan2));
			if(excGenes.size() != 0){
				Gene[] newGenes = GeneUtils.rebuildGenes(plan2, excGenes);
				return new PackingPlan(newGenes);
			}
		} catch (CloneNotSupportedException e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	
	
	private Map<String,Long> getItemCount(List<Gene> genes){
		if(itemCount.size() == 0){
			for(Gene excGene : genes){
				PackingStep ps = excGene.get();
				if(itemCount.containsKey(ps.getItemID())){
					Long oldCount = itemCount.get(ps.getItemID());
					itemCount.put(ps.getItemID(), oldCount + ps.getNum());
				}
				else{
					itemCount.put(ps.getItemID(), ps.getNum() + 0l);
				}
			}
		}
		return itemCount;
	}
	
	private List<Gene> pickupGenes(PackingPlan plan,List<Gene> excGenes){
		
		List<Gene> retGene = Lists.newArrayList();
		Map<String,Long> itemCount = getItemCount(Lists.newArrayList(plan.getGenes()));
		Map<String,List<Gene>> newItemMap = Maps.newHashMap();
		for(Gene gene : excGenes){
			PackingStep ps =gene.get();
			List<Gene> geneList = newItemMap.get(ps.getItemID());
			if(geneList == null){
				geneList = Lists.newArrayList();
			}
			geneList.add(ps);
			newItemMap.put(ps.getItemID(),geneList);
		}
		Map<String,Long> insertableItemCount = Maps.newHashMap();
		Map<String,Long> itemNums = GeneUtils.getContextItemNums();
		for(String itemID : newItemMap.keySet()){
			Long totalCount = itemNums.get(itemID);
			Long planCount = itemCount.get(itemID);
			if(planCount == null){
				insertableItemCount.put(itemID, totalCount);
			}
			else if(totalCount > planCount){
				insertableItemCount.put(itemID, totalCount - planCount);
			}
		}
		for(String itemID : insertableItemCount.keySet()){
			Long insertCount = insertableItemCount.get(itemID);
			if(insertCount > 0){
				List<Gene> insertGenes = newItemMap.get(itemID);
				for(int i = 0;i < insertGenes.size();i++){
					PackingStep ps = insertGenes.get(i).get();
					if(ps.getNum() <= insertCount){
						retGene.add(ps);
						insertCount -= ps.getNum();
						if(insertCount <= 0){
							break;
						}
					}
				}
			}
		}
		return retGene;
	}

	@Override
	public <T> T adapt(Class<T> targetCls) {
		if(targetCls.isAssignableFrom(this.getClass())){
			return (T) this;
		}
		return null;
	}


	@Override
	public Integer getTotalUsedCount() {
		int totalUsedCount = 0;
		for(Gene gene : steps){
			PackingStep ps = gene.get();
			totalUsedCount += ps.getNum();
		}
		return totalUsedCount;
	}


	@Override
	public Map<String, Long> getUsedCount() {
		Map<String,Long> retMap = Maps.newHashMap();
		for(Gene gene : steps){
			PackingStep ps = (PackingStep) gene;
			String key = "" + ps.getItemID();
			Long curNum = 0l;
			if(retMap.containsKey(key)){
				curNum = retMap.get(key);
			}
			retMap.put(key, curNum + ps.getNum());
		}
		return retMap;
	}


	@Override
	public Queue<FillStep> getSteps() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toString() {
		return "PackingPlan [steps=\n" + Arrays.toString(steps) + "\n]";
	}
	
	
	public boolean isPerfect(){
		//Map<String,Long> itemNums = Algo
		Map<String,Long> itemCount = getItemCount(Lists.newArrayList(getGenes()));
		Map<String,Long> itemNums = GeneUtils.getContextItemNums();
		if(itemCount.isEmpty()){
			return false;
		}
		for(String itemID : itemNums.keySet()){
			if(itemCount.containsKey(itemID)){
				Long fillCount = itemCount.get(itemID);
				if(!fillCount.equals(itemNums.get(itemID))){
					return false;
				}
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	
	public void sortStep(){
		List<Gene> sortedGenes = Lists.newArrayList();
		List<List<PackingStep>> zGroups = groupByZ();
		for(int i = 0;i < zGroups.size();i++){
			List<PackingStep> zGroup = zGroups.get(i);
			List<List<PackingStep>> yGroups = groupByY(zGroup);
			for(int j = 0; j < yGroups.size();j++){
				List<PackingStep> yGroup = yGroups.get(j);
				Collections.sort(yGroup, new Comparator<PackingStep>() {

					@Override
					public int compare(PackingStep o1, PackingStep o2) {
						// TODO Auto-generated method stub
						return o1.getX() - o2.getX() > 0 ? 1 : -1;
					}
				});
				sortedGenes.addAll(yGroup);
			}
		}
		sortedGenes.toArray(steps);
	}

	
	public List<List<PackingStep>> groupByZ(){
		List<List<PackingStep>> zGroups = Lists.newArrayList();
		List<Gene> tmpList = Lists.newArrayList(steps);
		Collections.sort(tmpList,new Comparator<Gene>() {

			@Override
			public int compare(Gene o1, Gene o2) {
				PackingStep s1 = o1.get();
				PackingStep s2 = o2.get();
				return s1.getZ() - s2.getZ() > 0 ? 1 : -1;
			}
		});
		double tmpZ = ((PackingStep)tmpList.get(0).get()).getZ();
		zGroups.add(new ArrayList<PackingStep>());
		int groupIndex = 0;
		for(int i = 0;i < tmpList.size();i++){
			PackingStep ps = tmpList.get(i).get();
			if(tmpZ == ps.getZ()){
				zGroups.get(groupIndex).add(ps);
			}
			else {
				zGroups.add(new ArrayList<PackingStep>());
				groupIndex ++;
				zGroups.get(groupIndex).add(ps);
				tmpZ = ps.getZ();
			}
		}
		return zGroups;
	}
	
	public List<List<PackingStep>> groupByY(List<PackingStep> zGroup){
		List<List<PackingStep>> yGroups = Lists.newArrayList();
		List<Gene> tmpList = Lists.newArrayList();
		tmpList.addAll(zGroup);
		Collections.sort(tmpList,new Comparator<Gene>() {

			@Override
			public int compare(Gene o1, Gene o2) {
				PackingStep s1 = o1.get();
				PackingStep s2 = o2.get();
				return s1.getY() - s2.getY() > 0 ? 1 : -1;
			}
		});

		double tmpY = ((PackingStep)tmpList.get(0).get()).getY();
		yGroups.add(new ArrayList<PackingStep>());
		int groupIndex = 0;
		for(int i = 0;i < tmpList.size();i++){
			PackingStep ps = tmpList.get(i).get();
			if(tmpY == ps.getY()){
				yGroups.get(groupIndex).add(ps);
			}
			else {
				yGroups.add(new ArrayList<PackingStep>());
				
				groupIndex ++;
				yGroups.get(groupIndex).add(ps);
				tmpY = ps.getY();
			}
		}
		return yGroups;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(steps);
		return result;
	}


	@Override
	public Integer hash() {
		return hashCode();
	}

	
}
