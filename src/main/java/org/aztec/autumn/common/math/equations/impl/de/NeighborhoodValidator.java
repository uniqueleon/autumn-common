package org.aztec.autumn.common.math.equations.impl.de;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class NeighborhoodValidator implements CustomerizedValidator {

	@Override
	public boolean isValid(ValidateParameter validateParam) {
		int index = validateParam.getIndex();
		Long[] factors = validateParam.getFactors();
		if(index >= factors.length){
			index = validateParam.getSpecialIndex();
		}
		Map<String,Object> params = validateParam.getParams();
		List<Long> found = validateParam.getFound();
		Long testSolution = validateParam.getTestSolution();
		Long[] range = validateParam.getRanges()[index];
		int[] neighborhoods = (int[]) params.get(PARAMETER_KEYS.NEIGHBORHOOD_GROUP_DATA);
		if(neighborhoods == null){
			return true;
		}
		int blockNo = -1;
		Integer[] seperators = getSeperators(neighborhoods);
		for(int i = 0;i < seperators.length;i++){
			if(index < seperators[i]){
				blockNo = i - 1;
				break;
			}
		}
		Long accumulateSolution = 0l;
		int beginIndex = blockNo < 0 ? 0 : seperators[blockNo];
		int endIndex = seperators[blockNo + 1];
		for(int i = beginIndex;i < endIndex;i++){
			if(found.size() > i && found.get(i) != 0){
				accumulateSolution += found.get(i);
			}
		}
		accumulateSolution += testSolution;
		return accumulateSolution >= range[0] && accumulateSolution <= range[1];
	}
	
	private Integer[] getSeperators(int[] neighborhoods){
		List<Integer> seperators = Lists.newArrayList();
		
		int cursor = 0;
		for(int i = 0;i < neighborhoods.length;i++){
			cursor += neighborhoods[i];
			seperators.add(cursor);
		}
		return seperators.toArray(new Integer[seperators.size()]);
	}

}
