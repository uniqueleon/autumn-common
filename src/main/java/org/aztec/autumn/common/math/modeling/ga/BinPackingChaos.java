package org.aztec.autumn.common.math.modeling.ga;

import org.aztec.autumn.common.algorithm.genetic.Chaos;
import org.aztec.autumn.common.algorithm.genetic.impl.AbstractChaos;
import org.aztec.autumn.common.algorithm.genetic.impl.RouletteDoubleSelector;
import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;

public class BinPackingChaos extends AbstractChaos implements Chaos {

	
	public BinPackingChaos(BinPackingConfig config) {
		super(new PlanGenerator(config.getBox(), config.getItems()),
				new FillPlanEvaluator(), new RouletteDoubleSelector(),
				new Double(Math.floor(AlgorithmConst.DEFAULT_GENERATIONS
						/ config.getSpeed())).intValue(), config.getPopulations());
		
	}

}
