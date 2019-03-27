package org.aztec.autumn.common.constant.unit;

import org.eclipse.jetty.webapp.MetaInfConfiguration;

public enum LengthUnits {

	//毫米
	MM(new Millimeter()),
	//厘米
	CM(new Centimeter()),
	//分米
	DM(new Decimeter()),
	//米
	M(new Meter()),
	//千米
	KM(new KiloMeter()),
	//微米,千分之一毫米
	Micron(new Micrometer()),
	//纳米,千分之一微米
	NANO(new Nanometer());
	
	public ArithmeticUnit unit;
	
	private LengthUnits(ArithmeticUnit unit) {
		this.unit = unit;
	}
	
	public Double convert(Double amount,LengthUnits oldUnit) {
		return this.unit.convert(amount, oldUnit.unit);
	}

	
	/**
	 * 厘米
	 * @author 10064513
	 *
	 */
	private static class Nanometer extends ArithmeticUnit{

		public Nanometer() {
			super(0, 1000d, CalculableUnitType.LENGTH);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			return new Micrometer();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			return null;
		}
		
	}


	/**
	 * 厘米
	 * @author 10064513
	 *
	 */
	private static class Micrometer extends ArithmeticUnit{

		public Micrometer() {
			super(1, 1000d, CalculableUnitType.LENGTH);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			return new Millimeter();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			return new Nanometer();
		}
		
	}

	/**
	 * 毫米
	 * @author 10064513
	 *
	 */
	private static class Millimeter extends ArithmeticUnit{

		public Millimeter() {
			super(2, 10d, CalculableUnitType.LENGTH);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return new Centimeter();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			// TODO Auto-generated method stub
			return new Micrometer();
		}
		
	}
	
	/**
	 * 厘米
	 * @author 10064513
	 *
	 */
	private static class Centimeter extends ArithmeticUnit{

		public Centimeter() {
			super(3, 10d, CalculableUnitType.LENGTH);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return new Decimeter();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			// TODO Auto-generated method stub
			return new Millimeter();
		}
		
	}
	
	
	/**
	 * 分米
	 * @author 10064513
	 *
	 */
	private static class Decimeter extends ArithmeticUnit{

		public Decimeter() {
			super(4, 10d, CalculableUnitType.LENGTH);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			return new Meter();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			return new Centimeter();
		}
		
	}
	
	/**
	 * 米
	 * @author 10064513
	 *
	 */
	private static class Meter extends ArithmeticUnit{

		public Meter() {
			super(5, 1000d, CalculableUnitType.LENGTH);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return new KiloMeter();
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			// TODO Auto-generated method stub
			return new Decimeter();
		}
		
	}
	
	/**
	 * 米
	 * @author 10064513
	 *
	 */
	private static class KiloMeter extends ArithmeticUnit{

		public KiloMeter() {
			super(6, 10d, CalculableUnitType.LENGTH);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ArithmeticUnit getNextUnit() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected ArithmeticUnit getPreviousUnit() {
			// TODO Auto-generated method stub
			return new Meter();
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(CM.convert(1d, DM));
	}
}
