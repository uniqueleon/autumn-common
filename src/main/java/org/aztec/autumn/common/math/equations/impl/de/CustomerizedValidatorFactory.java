package org.aztec.autumn.common.math.equations.impl.de;

import java.util.List;

import com.google.common.collect.Lists;

public class CustomerizedValidatorFactory {

	public static interface ValidatorIndex{
		public static final int NEIGHBORHOOD_VALIDATOR = 1;
		public static final int ORDER_VALIDATOR = 2;
	}
	
	public static List<CustomerizedValidator> getValidators(int[] choosenValidators){
		List<CustomerizedValidator> validatorList = Lists.newArrayList();
		if(choosenValidators == null)
			return validatorList;
		for(int index : choosenValidators){
			switch(index){
			case ValidatorIndex.NEIGHBORHOOD_VALIDATOR:
				validatorList.add(new NeighborhoodValidator());
				break;
			case ValidatorIndex.ORDER_VALIDATOR:
				break;
			}
		}
		return validatorList;
	}
}
