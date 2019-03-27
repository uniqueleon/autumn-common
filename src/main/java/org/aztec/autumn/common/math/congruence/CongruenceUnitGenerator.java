package org.aztec.autumn.common.math.congruence;

import java.util.List;

import com.google.common.collect.Lists;

public class CongruenceUnitGenerator {

	
	public static List<CongruenceUnit[]> generate(int[] modulars,int range) {
		List<CongruenceUnit[]> unitList = Lists.newArrayList();
		for(int i = 0;i < range;i++) {
			CongruenceUnit[] units = new CongruenceUnit[modulars.length];
			for(int j = 0;j < units.length;j++) {
				units[j] = new CongruenceUnit(modulars[j],i % modulars[j]);
			}
			unitList.add(units);
		}
		return unitList;
	}
}
