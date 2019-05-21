package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.impl.FillResultImpl;

public interface BoxSplitor {

	public List<Box> split(Box box,Box shrinkBox,FillResultImpl result);
}
