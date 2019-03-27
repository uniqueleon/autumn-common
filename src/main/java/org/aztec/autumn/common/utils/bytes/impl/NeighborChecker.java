package org.aztec.autumn.common.utils.bytes.impl;

import java.util.List;

import org.aztec.autumn.common.utils.bytes.ByteAnalyzer;
import org.aztec.autumn.common.utils.bytes.ByteCountInfo;
import org.aztec.autumn.common.utils.bytes.ByteNeighborInfo;
import org.aztec.autumn.common.utils.bytes.ByteStatisticsResult;

public class NeighborChecker implements ByteAnalyzer {

	public NeighborChecker() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isSatisfied(ByteStatisticsResult result) {
		// TODO Auto-generated method stub

		boolean satisfied = true;
		for (ByteCountInfo countInfo : result.getCountingInfo()) {
			if (countInfo.getR_neighborhoods().get(0) != null) {
				List<ByteNeighborInfo> neighbors = countInfo.getR_neighborhoods().get(0);
				long frequeceCheck = 0l;
				for (ByteNeighborInfo neighbor : neighbors) {
					frequeceCheck += neighbor.getFrequence();
				}
				if(Math.abs(frequeceCheck - countInfo.getFrequency()) > 1 ) {
					
					System.err.println("suck");
					System.exit(-1);
				}
				System.out.println("neighbors size:" + neighbors.size());
				if (neighbors.size() > 128) {
					satisfied = false;
				}
				else {
					System.out.println(neighbors.size());
				}
			}
		}
		return satisfied;
	}

}
