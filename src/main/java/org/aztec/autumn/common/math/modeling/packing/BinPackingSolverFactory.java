package org.aztec.autumn.common.math.modeling.packing;

import org.aztec.autumn.common.math.modeling.ga.GASolver;

public class BinPackingSolverFactory {

	public static BinPackingSolver getSolver(){
		//return new BinPackingSolver2();
		return new GASolver();
	}
}
