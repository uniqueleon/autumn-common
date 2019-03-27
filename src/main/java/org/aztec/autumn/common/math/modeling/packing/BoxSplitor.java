package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.impl.FillResult;

public interface BoxSplitor {

	public List<Box> split(Box box,Box shrinkBox,FillResult result);
}
