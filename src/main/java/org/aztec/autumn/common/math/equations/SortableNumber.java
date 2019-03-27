package org.aztec.autumn.common.math.equations;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

public class SortableNumber {
	
	private Long number;
	private Integer index;

	public SortableNumber() {
		// TODO Auto-generated constructor stub
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public SortableNumber(Long number, Integer index) {
		super();
		this.number = number;
		this.index = index;
	}
	
	

	@Override
	public String toString() {
		return "SortableNumber [number=" + number + ", index=" + index + "]";
	}

	public static List<SortableNumber> sort(Long[] numbers,Ordering ordering){
		List<SortableNumber> retNumbers = Lists.newArrayList();
		for(int i = 0;i < numbers.length;i++) {
			retNumbers.add(new SortableNumber(numbers[i], i));
		}
		retNumbers.sort(new SortableNumberComparator(ordering));
		return retNumbers;
	}


	public static enum Ordering{
		ASC,DESC;
	}
	
	public static class SortableNumberComparator implements Comparator<SortableNumber> {
		
		private Ordering ordering;
		
		public SortableNumberComparator(Ordering desc) {
			this.ordering = desc;
		}

		@Override
		public int compare(SortableNumber o1, SortableNumber o2) {
			
			
			Long subResult = o2.getNumber() - o1.getNumber();
			int retVal = 0;
			if(subResult == 0) {
				return 0;
			}
			if(ordering.equals(Ordering.DESC)) {
				retVal = subResult > 0 ? 1 : -1;
			}
			else {
				retVal = subResult < 0 ? 1 : -1;
			}
			return retVal;
		}
		
	}
}
