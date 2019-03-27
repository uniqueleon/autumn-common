package org.aztec.autumn.common.constant.unit;

public abstract class ArithmeticUnit implements CalculableUnit{
	
	protected Integer power;
	protected Double gap;
	protected CalculableUnitType type;
	
	protected Double getUnitGap() {
		return gap;
	}
	
	protected abstract ArithmeticUnit getNextUnit() ;
	protected abstract ArithmeticUnit getPreviousUnit() ;

	public ArithmeticUnit(Integer position, Double unitGap,CalculableUnitType type) {
		super();
		this.power = position;
		this.gap = unitGap;
		this.type = type;
	}

	@Override
	public Double convert(Double amount, CalculableUnit oldUnit) {
		if(isSameUnitType(oldUnit)) {
			ArithmeticUnit arUnit1 = (ArithmeticUnit) oldUnit;
			Double newAmount = amount;
			Double conversion = getConversion(oldUnit);
			if(conversion != 1) {
				newAmount = conversion > 0 ? newAmount * conversion : newAmount / Math.abs(conversion);
			}
			return newAmount; 
		}
		else {
			 throw new ArithmeticException("Can convert two unit with different type of unit!");
		}
	}
	
	public boolean isSameUnitType(CalculableUnit unit) {
		return unit != null && ArithmeticUnit.class.isAssignableFrom(unit.getClass()) && ((ArithmeticUnit) unit ).type.equals(this.type);
	}

	@Override
	public Double getConversion(CalculableUnit oldUnit) {
		if(isSameUnitType(oldUnit)) {
			ArithmeticUnit arUnit1 = (ArithmeticUnit) oldUnit;
			Double conversion = 1d;
			if(power == arUnit1.power)
				return conversion;
			int direction = 0;
			ArithmeticUnit tmpUnit = null;
			if(power > arUnit1.power) {
				tmpUnit = getPreviousUnit();
				direction = -1;
			}
			else if(power < arUnit1.power){
				tmpUnit = this;
				direction = 1;
			} 

			int step = Math.abs(power - arUnit1.power);
			for(int i = 0;i < step ;i ++) {
				if(tmpUnit == null) {
					throw new ArithmeticException("no more unit for proceeding!");
				}
				conversion *= tmpUnit.gap;
				if(direction > 0 ) {
					tmpUnit = tmpUnit.getNextUnit();
				}
				else {
					tmpUnit = tmpUnit.getPreviousUnit();
				}
			}
			return conversion * direction;
		}
		else {
			 throw new ArithmeticException("Can convert two unit with different type of unit!");
		}
	}

	
}
