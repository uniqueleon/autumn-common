package org.aztec.autumn.common.math.congruence;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;


public class CongruenceTable {

	
	private Map<TableKey,Integer> innerMap = Maps.newHashMap();
	private int[] modulars;
	private int product = 1;
	private Integer supplement;
	
	public Integer getSupplement() {
		return supplement;
	}

	public void setSupplement(int supplement) {
		this.supplement = supplement;
	}

	public CongruenceTable(List<CongruenceUnit[]> unitList) {
		init(unitList);
	}
	
	public void init(List<CongruenceUnit[]> unitList) {
		if(unitList.size() > 0) {
			for(int i = 0;i < unitList.size();i++) {
				CongruenceUnit[] units = unitList.get(i);
				if(modulars == null) {
					modulars = new int[units.length];
					for(int j = 0;j < units.length;j++) {
						modulars[j] = units[j].getModular();
						product *= modulars[j];
					}
				}
				innerMap.put(new TableKey(units, i / product), i);
			}
		}
	}
	
	
	public Integer find(TableKey key) {
		return innerMap.get(key) - (supplement == null ? 0 : supplement);
	}
	
	public TableKey getKey(Integer digit) {
		Integer tmp = digit + (supplement == null ? 0 : supplement);
		CongruenceUnit[] units = new CongruenceUnit[modulars.length];
		for(int i = 0;i < units.length;i++) {
			units[i] = new CongruenceUnit(modulars[i], tmp % modulars[i]);
		}
		//Integer 
		return new TableKey(units,tmp / product);
	}
	
	public int getProduct() {
		return product;
	}

	public static class TableKey {
		private CongruenceUnit[] units;
		private Integer round = -1;
		public CongruenceUnit[] getUnits() {
			return units;
		}
		public void setUnits(CongruenceUnit[] units) {
			this.units = units;
		}
		public Integer getRound() {
			return round;
		}
		public void setRound(Integer round) {
			this.round = round;
		}
		public TableKey(CongruenceUnit[] units, Integer round) {
			super();
			this.units = units;
			this.round = round;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((round == null) ? 0 : round.hashCode());
			for(CongruenceUnit unit : units) {

				result = prime * result + unit.getModular();
				result = prime * result + unit.getRemainder();
			}
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TableKey other = (TableKey) obj;
			if (round == null) {
				if (other.round != null)
					return false;
			} else if (!round.equals(other.round))
				return false;
			if (!Arrays.equals(units, other.units))
				return false;
			return true;
		}
		
		
	}
	
	public static void main(String[] args) {
		CongruenceTable cTable = new CongruenceTable(CongruenceUnitGenerator.generate(new int[] {15,17}, 256));
		cTable.setSupplement(127);
		TableKey key = cTable.getKey(-66);
		for(CongruenceUnit cUnit : key.getUnits()) {

			System.out.println(cUnit.getModular() + "--" +  cUnit.getRemainder());
		}
		System.out.println(cTable.find(key));
	}
	
}
