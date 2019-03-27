package org.aztec.autumn.common.utils.jxl;

import jxl.format.CellFormat;

class JxlSimpleCell {
	JxlSimplePosition jxlSimplePosition;
	Object content;
	CellFormat format;
	
	public JxlSimpleCell(int x,int y,Object content)
	{
		jxlSimplePosition = new JxlSimplePosition(x,y);
		this.content = content;
	}
	
	public JxlSimpleCell(int x,int y,Object content,CellFormat format)
	{
		jxlSimplePosition = new JxlSimplePosition(x,y);
		this.content = content;
	}
	
	public int getX() {
		return jxlSimplePosition.getX();
	}
	public void setX(int x) {
		jxlSimplePosition.setX(x);
	}
	public int getY() {
		return jxlSimplePosition.getY();
	}
	public void setY(int y) {
		jxlSimplePosition.setY(y);
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
}
