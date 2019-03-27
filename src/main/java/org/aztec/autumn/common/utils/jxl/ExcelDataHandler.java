package org.aztec.autumn.common.utils.jxl;

import java.util.List;

public interface ExcelDataHandler {

	public boolean isSheetHandler();
	public boolean isRowHandler();
	public boolean isColumnHandler();
	public boolean isCellHandler();
	public void setExlData(List<List<List<String>>> StringDatas);
	public HandleResult handle() throws ExcelHandleException;
	public List<ExcelDataHandler> getRowHandler();
	public List<ExcelDataHandler> getCellHandler();
	//public void getHandle
}
