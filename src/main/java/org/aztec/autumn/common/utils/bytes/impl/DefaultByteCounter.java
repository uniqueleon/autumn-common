package org.aztec.autumn.common.utils.bytes.impl;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.bytes.AnalyzerConfig;
import org.aztec.autumn.common.utils.bytes.ByteCounter;
import org.aztec.autumn.common.utils.bytes.ByteCountInfo;
import org.aztec.autumn.common.utils.bytes.ByteStatisticsResult;
import org.aztec.autumn.common.utils.bytes.ByteNeighborInfo;

import com.google.common.collect.Lists;

public class DefaultByteCounter implements ByteCounter {

	public DefaultByteCounter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isSpecial(byte bite) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ByteCountInfo count(AnalyzerConfig config) {
		// TODO Auto-generated method stub
		Byte target = config.getTarget();
		ByteStatisticsResult result = config.getWorkingData();
		ByteCountInfo countInfo = new ByteCountInfo(target, isSpecial(target), 1l);
		if(result.getIndexes().containsKey(target)) {
			countInfo = result.getCountingInfo().get(result.getIndexes().get(target));
		}
		else {
			result.append(countInfo);
		}
		countInfo.setFrequency(countInfo.getFrequency() + 1);
		countInfo.setBite(target);
		countNeighbor(countInfo, config.getBackward(), config.getForward(), config.getL_Neighborhood(), config.getR_Neighborhood());
		//countInfo.set
		return countInfo;
	}
	
	private void countNeighbor(ByteCountInfo countInfo,int backward,int forward,byte[] ldata,byte[] rdata){
		int cursor = 0;
		while(cursor < backward) {
			Map<Integer,List<ByteNeighborInfo>> lNeighbors = countInfo.getL_neighborhoods();
			lNeighbors.put(cursor, refreshNeighbor(ldata[cursor], cursor, lNeighbors.get(cursor)));
			cursor++;
		}
		cursor = 0;
		while(cursor < forward) {
			Map<Integer,List<ByteNeighborInfo>> rNeighbors = countInfo.getR_neighborhoods();
			rNeighbors.put(cursor, refreshNeighbor(rdata[cursor], cursor, rNeighbors.get(cursor)));
			cursor++;
		}
		
	}
	
	private List<ByteNeighborInfo> refreshNeighbor(byte neighborByte,int distance,List<ByteNeighborInfo> neighbors){
		List<ByteNeighborInfo> newDatas = Lists.newArrayList();
		if(neighbors == null) {
			ByteNeighborInfo neighbor = new ByteNeighborInfo(neighborByte, distance, 1l);
			newDatas.add(neighbor);
		}
		else {
			newDatas.addAll(neighbors);
			ByteNeighborInfo neighborData = null;
			for(ByteNeighborInfo neighbor : neighbors) {
				if(neighbor.getNeighbor() == neighborByte) {
					neighborData = neighbor;
				}
			}
			if(neighborData == null) {
				neighborData = new ByteNeighborInfo(neighborByte, distance, 1l);
				newDatas.add(neighborData);
			}
			else {
				neighborData.setFrequence(neighborData.getFrequence() + 1);
			}
		}
		return newDatas;
	}
	

	@Override
	public byte map(byte bite, int order) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] map(byte[] bytes, List<ByteStatisticsResult> result) {
		// TODO Auto-generated method stub
		return null;
	}

}
