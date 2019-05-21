package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolver;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResultImpl.FillType;

public class BinPackingSolver2 extends BaseBPSolver implements BinPackingSolver{

	
	private static final Double supplement = 20d;
	
	protected FillResultImpl doFill(Box box, List<Item> items,BinPackingConfig config){
		

		Box tmpBox = box.clone();
		RectangleSurfaceFiller2 filler = new RectangleSurfaceFiller2();
		Long itemCount = 0l;
		for (Item item : items) {
			itemCount += item.getNumber();
		}
		List<List<Long>> edges = PackingUtils.getFillableEdges(items, tmpBox);
		long step = PackingUtils.calculateMinStepByEdge(edges);
		long upperLimit = PackingUtils.calculateUpperLimit(edges, items);
		long lowerLimit = PackingUtils.calculateLowerLimit(edges);
		if(upperLimit < tmpBox.getLength()){
			tmpBox.setLength(new Double(upperLimit));
		}
		tmpBox.resize(step, true,true);
		FillResultImpl bestResult = null;
		boolean shrinkable = true;
		//long tryTime = new Double(supplement / config.getSpeed()).longValue();
		long tryTime = new Double(supplement).longValue();
		do {
			try {
				FillResultImpl result = filler.fill(new FillParameter(tmpBox.clone(), items, FillType.SURFACE));
				if(result != null && result.score() > 0){
					//return result;
					if(bestResult == null || result.score() > bestResult.score()){
						bestResult = result.clone();
					}
					//return result;
				}
			} catch (BinPackingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("fill Time:" + usedTime);
			shrinkable = tmpBox.getLength().longValue() >= lowerLimit;
			if (!shrinkable)
				break;
			//tmpBox.shrink(step, lowerLimit, onlyBottom);
			tmpBox.setLength(tmpBox.getLength() - step);
			tryTime --;
		} while (shrinkable && tryTime > 0);
		return bestResult;
	}


}
