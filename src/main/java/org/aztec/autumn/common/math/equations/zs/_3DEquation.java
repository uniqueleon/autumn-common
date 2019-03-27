package org.aztec.autumn.common.math.equations.zs;

import java.util.Comparator;
import java.util.List;

import org.aztec.autumn.common.math.MathException;
import org.aztec.autumn.common.math.MathException.DiophantineEquationErrorCode;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;


/**
 * 引理2-2：设a * x+b * y+ c * z = 0（零和方程），1=(a,b,c)，a = p'(mod c), b = q' (mod c)，
 * 设d=(p',q')，p = p' / d, q = q' / d，则有 X = q + ct1, Y = - p + ct2 , Z = r - at1 - bt2, r = (bp - aq) / c
 * 
 * @author liming
 */
public class _3DEquation implements NDimensionEquation{

	private long a = 0l;
	private long b = 0l;
	private long c = 0l;
	private long q = 0l;
	private long p = 0l;
	private long r = 0l;
	List<SortableCoefficient> sortCoefficients;
	
	public _3DEquation() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accept(List<Long> coefficients) {
		return coefficients.size() == 3;
	}
	
	private static class SortableCoefficient{
		private Long number;
		private Integer index;
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
		public SortableCoefficient(Long number, Integer index) {
			super();
			this.number = number;
			this.index = index;
		}
		
		
	}
	

	@Override
	public void init(List<Long> coefficients) {
		// TODO Auto-generated method stub

		a = b = c = p = q = r = 0l;
		GreatestCommonDivisor allGcd = GreatestCommonDivisor .getGCD(coefficients.toArray(new Long[3]));
		if(sortCoefficients == null) {
			sortCoefficients = Lists.newArrayList();
		}
		else {
			sortCoefficients.clear();
		}
		sortCoefficients.add(new SortableCoefficient(coefficients.get(0),0));
		sortCoefficients.add(new SortableCoefficient(coefficients.get(1),1));
		sortCoefficients.add(new SortableCoefficient(coefficients.get(2),2));
		sortCoefficients.sort(new Comparator<SortableCoefficient>() {

			@Override
			public int compare(SortableCoefficient o1, SortableCoefficient o2) {
				// TODO Auto-generated method stub
				return o2.getNumber() > o1.getNumber() ? 1 : -1;
			}
		});
		a = sortCoefficients.get(0).getNumber() / allGcd.getGcd();
		b = sortCoefficients.get(1).getNumber() / allGcd.getGcd();
		c = sortCoefficients.get(2).getNumber() / allGcd.getGcd();
		if(a % c == 0 && b % c != 0) {
			p = MathUtils.getLeastRemainder(b, c);
			GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(new Long[] {p,c});
			p = c / gcd.getGcd();
			r = b * p / c;
		}
		else if (a % c != 0 && b % c == 0) {
			q = MathUtils.getLeastRemainder(a, c);
			GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(new Long[] {q,c});
			q = c / gcd.getGcd();
			r = - a * q / c;
		}
		else if(a % c != 0 && b % c != 0) {
			GreatestCommonDivisor gd = GreatestCommonDivisor.calculate(a % c, b % c);
			p = (a % c) / gd.getGcd();
			q = (b % c) / gd.getGcd();
			r = (b * p - a * q ) / c;
		}
	}

	@Override
	public List<Long[]> getAcceptableSolution(Long[][] ranges, List<List<Long>> excluded,int acceptableNum) {
		
		Long[][] newRanges = new Long[ranges.length][];
		for(int i = 0;i < ranges.length;i++) {
			int realIndex = sortCoefficients.get(i).getIndex();
			newRanges[i] = ranges[realIndex];
		}
		List<Long> acceptableVars1 = findAcceptableVar1(newRanges[0]);
		if(acceptableVars1.size() == 0)
			return Lists.newArrayList();
		List<Long> acceptableVars2 = findAcceptableVar2(newRanges[1]);
		if(acceptableVars2.size() == 0 || (acceptableVars2.get(0) == 0l && acceptableVars1.get(0) == 0l))
			return Lists.newArrayList();
		List<Long[]> solutions = findAcceptableSolution(acceptableVars1, acceptableVars2, newRanges[2]);
		restore(solutions);
		return solutions;
	}
	
	private void restore(List<Long[]> solutions){
		for(int i = 0;i < solutions.size();i++) {
			Long[] s1 = solutions.get(i);
			Long[] s2 = new Long[sortCoefficients.size()];
			for(int j = 0;j < sortCoefficients.size();j++) {
				s2[sortCoefficients.get(j).getIndex()] = s1[j];
			}
			solutions.set(i, s2);
		}
	}
	
	private List<Long> findAcceptableVar1(Long[] rangeX) {
		Long[] varRange = new Long[2];

		long direction = p == 0 ? -1 : 1;
		varRange[0] = new Double(Math.floor((rangeX[0] - direction * q) / (q == 0 ? 1 : c))).longValue();
		varRange[1] = new Double(Math.ceil((rangeX[1] - direction * q) / (q == 0 ? 1 : c))).longValue();
		
		List<Long> vars = Lists.newArrayList();
		for(Long i = varRange[0];i <= varRange[1];i++) {
			if(i == varRange[0] || i == varRange[1]) {
				Long x = getX(i);
				if(x >= rangeX[0] && x <= rangeX[1]) {
					vars.add(i);
				}
			}
			else {
				vars.add(i);
			}
		}
		return vars;
	}
	
	private List<Long> findAcceptableVar2(Long[] rangeY){
		Long[] varRange = new Long[2];
		long direction = q == 0 ? -1 : 1;
		varRange[0] = new Double(Math.floor((rangeY[0] + direction * p) / (p == 0 ? 1 : c))).longValue();
		varRange[1] = new Double(Math.ceil((rangeY[1] + direction * p) / (p == 0 ? 1 : c))).longValue();
		
		List<Long> vars = Lists.newArrayList();
		for(Long i = varRange[0];i <= varRange[1];i++) {
			if(i == varRange[0] || i == varRange[1]) {
				Long y = getY(i);
				if(y >= rangeY[0] && y <= rangeY[1]) {
					vars.add(i);
				}
			}
			else {
				vars.add(i);
			}
		}
		return vars;
	}
	
	private List<Long[]> findAcceptableSolution(List<Long> vars1,List<Long> vars2,Long[] rangeZ){
		List<Long[]> allSolution = Lists.newArrayList();
		for(int i = 0;i < vars1.size();i++) {
			for(int j = 0;j < vars2.size();j++) {
				long t1 = vars1.get(i);
				long t2 = vars2.get(j);
				long z = getZ(t1, t2);
				if(z >= rangeZ[0] && z <= rangeZ[1] ) {
					allSolution.add(new Long[]{getX(t1),getY(t2),z});
				}
			}
		}
		return allSolution;
	}

	private Long getX(Long var1) {
		return q + c * var1;
	}
	
	private Long getY(Long var1) {
		 return -p + c * var1;
	}
	
	private Long getZ(Long var1,Long var2) {
		return r - a  * var1 - b * var2 ;
	}
	
	

	@Override
	public void validate(List<Long> coefficients,List<Long[]> solutions) throws MathException {
		// TODO Auto-generated method stub
		for(Long[] solution : solutions) {
			long totalResult = 0l;
			for(int i = 0;i < coefficients.size();i++) {
				totalResult += coefficients.get(i) * solution[i];
			}
			if(totalResult != 0) {
				throw new MathException(DiophantineEquationErrorCode.SOLUTION_DOES_NOT_MATCH,
						String.format("The Equation not match[%1$d * %2$d + %3$d * %4$d + %5$d * %6$d = 0]", new Object[] {coefficients.get(0),solution[0]
								,coefficients.get(1),solution[1],coefficients.get(2),solution[2]}));
			}
		}
		
	}

	@Override
	public Integer getDimension() {
		// TODO Auto-generated method stub
		return 3;
	}


}
