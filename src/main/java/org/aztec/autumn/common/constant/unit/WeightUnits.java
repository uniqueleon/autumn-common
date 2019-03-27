package org.aztec.autumn.common.constant.unit;

public enum WeightUnits {

	MG(new MicroGram()),G(new Gram()),KG(new KiloGram()),TON(new Ton());
	
	public ArithmeticUnit unit;
	
	public Double convert(Double amount,WeightUnits otherUnit) {
		return unit.convert(amount, otherUnit.unit);
	}
	
	private WeightUnits(ArithmeticUnit unit) {
		this.unit = unit;
	}

	private static class MicroGram extends ArithmeticUnit{

		public MicroGram() {
			super(0, 1000d, CalculableUnitType.WEIGHT);
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return new Gram();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static class Gram extends ArithmeticUnit{

		public Gram() {
			super(1, 1000d, CalculableUnitType.WEIGHT);
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return new KiloGram();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			// TODO Auto-generated method stub
			return new MicroGram();
		}
		
	}
	
	private static class KiloGram extends ArithmeticUnit{

		public KiloGram() {
			super(2, 1000d, CalculableUnitType.WEIGHT);
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return new Ton();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			// TODO Auto-generated method stub
			return new Gram();
		}
		
	}
	
	private static class Ton extends ArithmeticUnit{

		public Ton() {
			super(3, 1000d, CalculableUnitType.WEIGHT);
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			return new KiloGram();
		}
		
	}
	
}
