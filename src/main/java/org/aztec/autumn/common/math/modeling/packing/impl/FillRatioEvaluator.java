package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.BinPackingSolution;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.SolutionEvaluator;

public class FillRatioEvaluator implements SolutionEvaluator {

	public FillRatioEvaluator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Double getScore(BinPackingSolution solution) {
		Double boxV = solution.getBox().getWidth() * solution.getBox().getHeight() * solution.getBox().getLength();
		Double itemV = 0d;
		List<Item> items = solution.getUsedItems();
		for (Item item : items) {
			itemV += item.getNumber() * item.getWidth() * item.getHeight() * item.getLength();
		}
		return itemV / boxV;
	}

}
