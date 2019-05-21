package org.aztec.autumn.common.math.modeling.ga;

import java.util.List;

import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.algorithm.genetic.GeneGenerator;
import org.aztec.autumn.common.algorithm.genetic.Individual;
import org.aztec.autumn.common.algorithm.genetic.IndividualGenerator;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;

import com.google.common.collect.Lists;

public class PlanGenerator implements IndividualGenerator {
	
	
	private Box box;
	private List<Item> items;
	
	public PlanGenerator(Box box,List<Item> items){
		this.box = box.clone();
		this.items = Lists.newArrayList();
		for(Item item : items){
			this.items.add(item.clone());
		}
	}

	@Override
	public Individual generate(Gene[] genes) {
		// TODO Auto-generated method stub
		return new PackingPlan(genes);
	}

	@Override
	public void setGeneGenerator(GeneGenerator generator) {
		// TODO Auto-generated method stub
	}

	@Override
	public Individual generate() {
		// TODO Auto-generated method stub
		List<PackingStep> steps = Lists.newArrayList();
		List<Box> fillBoxes = Lists.newArrayList();
		fillBoxes.add(box);
		int cursor = 0;
		List<Box> tmpBoxes = Lists.newArrayList();
		List<Item> remainItems = Lists.newArrayList();
		for(Item item : items){
			remainItems.add(item.clone());
		}
		while(!fillBoxes.isEmpty()){
			for(Box targetBox : fillBoxes){
				if(remainItems.size() == 0){
					break;
				}
				//FillInfo fillInfo = GeneUtils.fill(targetBox, remainItems);
				FillInfo fillInfo = GeneUtils.multiFill(targetBox, remainItems);
				if(fillInfo != null){
					tmpBoxes.addAll(fillInfo.getNewBoxes());
					PackingStep newStep = toGene(cursor,fillInfo);
					steps.add(newStep);
					cursor ++;
					Item targetItem = remainItems.get(fillInfo.getSelectIndex());
					Long itemCount = targetItem.getNumber();
					itemCount -= fillInfo.getNum();
					if(itemCount > 0){
						targetItem.setNumber(itemCount);
					}
					else{
						remainItems.remove(fillInfo.getSelectIndex());
					}
				}
			}
			//fillBoxes = Box.mergeAll(tmpBoxes);
			fillBoxes.clear();
			fillBoxes.addAll(tmpBoxes);
			tmpBoxes.clear();
		}
		return new PackingPlan(steps.toArray(new PackingStep[steps.size()]));
	}

	
	private PackingStep toGene(int cursor,FillInfo fillInfo){
		Item item = fillInfo.getTargetItem();
		Double[] edges = item.getShapeData(item.getShape());
		PackingStep ps = new PackingStep(cursor,item.getId(),
				item.getLocation().getX(),item.getLocation().getY(),item.getLocation().getZ(),
				edges[0],edges[1],edges[2],fillInfo.getNum()
				);
		return ps;
	}
}
