package org.aztec.autumn.common.math.equations.impl.de;

import java.util.List;

import org.aztec.autumn.common.math.equations.ConstrainedDEAlgorithm;

import com.google.common.collect.Lists;

/**
 * 绠�鍗曠┓涓炬硶
 * @author 10064513
 *
 */
public class SimpleAlgorithm extends BaseConstraintDEAlgorithm implements ConstrainedDEAlgorithm {

	public SimpleAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<Long> getCandiadateSolution(int round, Long result) {
		// TODO Auto-generated method stub
		return super.getCandiadateSolution(round, result);
	}

	@Override
	protected List<Long> doFind(Long[] factors, Long result, Long[][] ranges, List<List<Long>> histories) {
		// TODO Auto-generated method stub
		List<List<Long>> candidateSolutions = Lists.newArrayList();
		for(int i = 0;i < ranges.length;i++) {
			Long var = ranges[i][0];
			List<Long> candidateSolution = Lists.newArrayList();
			while(var <= ranges[i][1]) {
				candidateSolution.add(var);
				var ++;
			}
			candidateSolutions.add(candidateSolution);
		}
		return pickUpFinalResult(candidateSolutions, factors, ranges, result);
	}


}
