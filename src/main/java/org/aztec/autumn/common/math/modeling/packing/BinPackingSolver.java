package org.aztec.autumn.common.math.modeling.packing;

public interface BinPackingSolver {

	public BinPackingSolution solve(BinPackingConfig config) throws BinPackingException ;
}
