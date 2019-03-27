package org.aztec.autumn.common.utils.jxl;

public interface HandleResult {

	public boolean isRowResult();
	public boolean isCellResult();
	public boolean isColumnResult();
	public boolean isSheetResult();
	public int getSheetNum();
	public int getRowNum();
	public int getColumnNum();
	
}
