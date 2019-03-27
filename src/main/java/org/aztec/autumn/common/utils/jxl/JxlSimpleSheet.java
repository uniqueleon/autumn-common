package org.aztec.autumn.common.utils.jxl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JxlSimpleSheet {

	private Map<JxlSimplePosition,JxlSimpleCell> sheetContent;
	private int sheetNo;
	private String name;
	private final static String DEFAULT_NAME = "SHEET";
	
	public JxlSimpleSheet(String name,int sheetNo,Object[][] contents)
	{
		this.name = name;
		if(name == null)this.name = DEFAULT_NAME;
		this.sheetNo = sheetNo;
		sheetContent = new HashMap<JxlSimplePosition, JxlSimpleCell>();
		for(int i = 0;i < contents.length;i++)
		{
			Object[] cols = contents[i];
			if(cols != null)
			{
				for(int j = 0;j < cols.length;j++)
				{
					JxlSimplePosition pos = new JxlSimplePosition(j,i);
					JxlSimpleCell jxlSimpleCell = new JxlSimpleCell(j, i, contents[i][j]);
					sheetContent.put(pos, jxlSimpleCell);
				}
			}
		}
	}
	
	public JxlSimpleCell getCell(int x,int y)
	{
		JxlSimplePosition param = new JxlSimplePosition(x,y);
		return sheetContent.get(param);
	}
	
	public int getSheetNo()
	{
		return sheetNo;
	}
	
	public List<JxlSimpleCell> getCells()
	{
		List<JxlSimpleCell> cells = new ArrayList<JxlSimpleCell>();
		for(JxlSimpleCell cell : sheetContent.values())
		{
			cells.add(cell);
		}
		return cells;
	}
	
	public String getSheetName()
	{
		return name;
	}
}
