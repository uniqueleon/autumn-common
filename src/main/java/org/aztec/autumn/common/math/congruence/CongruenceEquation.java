package org.aztec.autumn.common.math.congruence;

import java.util.List;

import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.utils.MathUtils;

import com.google.common.collect.Lists;

public class CongruenceEquation {
	
	

	public static CongruenceEquationResult solve(Long coefficient,Long modular,Long remainder) {
		
		if(coefficient % modular == 0) {

			CongruenceEquationResult result = new CongruenceEquationResult(coefficient,modular, remainder);
			Long cursor = 0l;
			result.setSolved(true);
			List<Long> results = Lists.newArrayList();
			while(cursor < modular) {
				results.add(new Long(cursor));
				cursor ++;
			}
			result.setResults(results);
			return result;
		}
		else {
			Long leastRemainder = MathUtils.getLeastRemainder(remainder, modular);
			Long coe = MathUtils.getLeastRemainder(coefficient, modular);
			GreatestCommonDivisor gcd = GreatestCommonDivisor.getGCD(new Long[] {coe,modular});
			if(leastRemainder == 0) {
				CongruenceEquationResult result = new CongruenceEquationResult(coefficient,modular, remainder);
				result.setSolved(true);
				List<Long> results = Lists.newArrayList();
				if(gcd.getGcd() == 1) {
					results.add(0l);
					result.setResults(results);
				}
				else {
					Long baseResult = 0l;
					Long step = modular / gcd.getGcd();
					while(baseResult < modular) {
						results.add(new Long(baseResult));
						baseResult += step;
					}
				}
				result.setResults(results);
				return result;
			}
			else {

				CongruenceEquationResult result = new CongruenceEquationResult(coefficient,modular, remainder);
				if(leastRemainder % gcd.getGcd() == 0) {
					Long[] factors = new Long[] {coe / gcd.getGcd(),modular / gcd.getGcd()};
					Long deResult = leastRemainder / gcd.getGcd();
					DiophantineResult dr = DiophantineEquation.getSolution(factors, deResult);
					Long baseResult = new Long(dr.getBaseResults().get(0));
					Long step = factors[1];
					while(baseResult > modular || baseResult < 0) {
						baseResult += (baseResult > modular ? -1 : 1) * factors[1] ;
					}
					boolean forward = baseResult > step ? false : true;
					while(baseResult >= 0 && baseResult < modular) {
						result.getResults().add(new Long(baseResult));
						baseResult += forward ? step : - step;
					}
					result.setSolved(true);
				}
				return result;
			}
		}
	}
	
}
