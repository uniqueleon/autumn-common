package org.aztec.autumn.common.utils.jxl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MSExcelConfig {

	Map<Integer,JxlSimpleSheet> jxlSimpleSheets = new HashMap<Integer, JxlSimpleSheet>();;
	String filePath;
	
	public MSExcelConfig(String filePath,String sheetname,List<List<String>> datas){
		this.filePath = filePath;
		Object[][] content = new Object[datas.size()][];
		for(int i = 0;i < datas.size();i++){
			List<String> rowData = datas.get(i);
			content[i] = new Object[rowData.size()];
			for(int j = 0;j < rowData.size();j++){
				content[i][j] = rowData.get(j);
			}
		}
		JxlSimpleSheet jxlSimpleSheet = new JxlSimpleSheet(sheetname,0,content);
		jxlSimpleSheets.put(jxlSimpleSheet.getSheetNo(),jxlSimpleSheet);
	}
	
	public MSExcelConfig(String filePath,Map<Integer,String> sheetNames,List<Object[][]> contentList)
	{
		this.filePath = filePath;
		for(int i = 0;i < contentList.size();i++)
		{
			Object[][] content = contentList.get(i);
			JxlSimpleSheet jxlSimpleSheet = new JxlSimpleSheet(sheetNames.get(i),i,content);
			jxlSimpleSheets.put(jxlSimpleSheet.getSheetNo(),jxlSimpleSheet);
		}
	}
	
	public JxlSimpleSheet getSheet(int sheetNo)
	{
		return jxlSimpleSheets.get(sheetNo);
	}
	
	public List<JxlSimpleSheet> getAllSheets()
	{
		List<JxlSimpleSheet> ret = new ArrayList<JxlSimpleSheet>();
		for(JxlSimpleSheet jxlSimpleSheet : jxlSimpleSheets.values())
		{
			ret.add(jxlSimpleSheet);
		}
		return ret;
	}
	
	public String getExlFilePath()
	{
		return filePath;
	}
	
}
