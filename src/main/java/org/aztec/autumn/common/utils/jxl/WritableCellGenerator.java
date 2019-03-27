package org.aztec.autumn.common.utils.jxl;

import java.util.HashMap;
import java.util.Map;

import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;

class WritableCellGenerator {

	private final static Map<Class,CellGenerator> generators = new HashMap<Class, CellGenerator>();
	
	static{
		generators.put(Integer.class, new NumberGenerator());
		generators.put(Long.class, new NumberGenerator());
		generators.put(Double.class, new NumberGenerator());
		generators.put(Float.class, new NumberGenerator());
		generators.put(Short.class, new NumberGenerator());
		generators.put(String.class, new LabelGenerator());
	}
	
	public static WritableCell getWritblableCell(JxlSimpleCell jxlSimpleCell)
	{
		CellGenerator generator = generators.get(jxlSimpleCell.getContent().getClass());
		if(generator != null)
		{
			return generator.getWritableCell(jxlSimpleCell);
		}
		return null;
	}

}

interface CellGenerator
{
	public WritableCell getWritableCell(JxlSimpleCell jxlSimpleCell);	
}

class LabelGenerator implements CellGenerator
{
	public WritableCell getWritableCell(JxlSimpleCell jxlSimpleCell) {
		
		String content = (String) jxlSimpleCell.getContent();
		CellFormat format = new WritableCellFormat();
		Label label = new Label(jxlSimpleCell.getX(), jxlSimpleCell.getY(), content, format);
		return label;
	}
}

class NumberGenerator implements CellGenerator
{

	public WritableCell getWritableCell(JxlSimpleCell jxlSimpleCell) {
		Number number = new Number(jxlSimpleCell.getX(), jxlSimpleCell.getY(), 
				(Double)jxlSimpleCell.getContent());
		return number;
	}
	
}

