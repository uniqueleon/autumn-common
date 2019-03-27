package org.aztec.autumn.common.math.equations.impl.de;

import java.util.Comparator;
import java.util.List;

import org.aztec.autumn.common.math.equations.ConstrainedDEAlgorithm;
import org.aztec.autumn.common.math.equations.SortableNumber;

import com.beust.jcommander.internal.Lists;

public class Algorithm_4 extends BaseConstraintDEAlgorithm implements ConstrainedDEAlgorithm {

	@Override
	public List<Long> doFind(Long[] factors, Long result, Long[][] ranges, List<List<Long>> histories) {
		// TODO Auto-generated method stub
		List<SortableNumber> sortFactors = Lists.newArrayList();
		for(int i = 0;i < factors.length;i++) {
			sortFactors.add(new SortableNumber(factors[i], i));
		}
		sortFactors.sort(new Comparator<SortableNumber>() {

			@Override
			public int compare(SortableNumber o1, SortableNumber o2) {
				// TODO Auto-generated method stub
				return o2.getNumber() - o1.getNumber() > 0 ? 1 : -1;
			}
		});
		Long[] newSortFactors = new Long[sortFactors.size()];
		List<List<Long>> candidateSolutions = Lists.newArrayList();
		for(int i = 0;i < sortFactors.size();i++) {
			List<Long> candidateSolution = Lists.newArrayList();
			newSortFactors[i] = sortFactors.get(i).getNumber();
			Long firstSolution = ranges[sortFactors.get(i).getIndex()][1];
			while(firstSolution > ranges[sortFactors.get(i).getIndex()][0]) {
				candidateSolution.add(firstSolution);
				firstSolution --;
			}
			candidateSolutions.add(candidateSolution);
		}
		List<Long> findSolutions = pickUpFinalResult(candidateSolutions, newSortFactors, ranges, result);
		if(findSolutions == null)
			return null;
		Long[] finalSolutions = new Long[factors.length];
		for(int i = 0;i < findSolutions.size();i++) {
			Integer index = sortFactors.get(i).getIndex();
			finalSolutions[index] = findSolutions.get(i);
		}
		return Lists.newArrayList(finalSolutions);
	}

}
