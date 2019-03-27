package org.aztec.autumn.common.utils.jxl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class MSExcelUtil {

	public static File generateExlFile(MSExcelConfig config) throws IOException, WriteException
	{
		File exlFile = new File(config.getExlFilePath());
		if(!exlFile.exists())
		{
			exlFile.createNewFile();
		}
		WritableWorkbook wbook = Workbook.createWorkbook(exlFile);
		List<JxlSimpleSheet> sheets = config.getAllSheets();
		for(int i = 0;i < sheets.size();i++)
		{
			writeSheet(wbook, sheets.get(i));
		}
		wbook.write();
		wbook.close();
		return exlFile;
	}
	
	
	private static void writeSheet(WritableWorkbook wBook,JxlSimpleSheet jxlSimpleSheet) throws RowsExceededException, WriteException
	{
		WritableSheet wSheet;
		try {
			wSheet = wBook.getSheet(jxlSimpleSheet.getSheetNo());
		} catch (IndexOutOfBoundsException e) {
			wBook.createSheet( jxlSimpleSheet.getSheetName(),jxlSimpleSheet.getSheetNo());
			wSheet = wBook.getSheet(jxlSimpleSheet.getSheetNo());
		}
		List<JxlSimpleCell> cells = jxlSimpleSheet.getCells();
		for(int i = 0;i < cells.size();i++)
		{
			JxlSimpleCell cell = cells.get(i);			
			WritableCell wCell = WritableCellGenerator.getWritblableCell(cell);
			wSheet.addCell(wCell);
		}
	}
	
	public static List<List<List<String>>> read(File exlFile){
		List<List<List<String>>> readDatas = new ArrayList<List<List<String>>>();
		if(!exlFile.exists() || !exlFile.isFile() )
			throw new IllegalArgumentException("excel file is not a file or not exists!");

		try {
			Workbook wbook = Workbook.getWorkbook(exlFile);
			int sheetNumber = wbook.getNumberOfSheets();
			for(int i = 0;i < sheetNumber;i++){
				readDatas.add(readFromSheet(wbook.getSheet(i)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readDatas;
	}
	
	private static List<List<String>> readFromSheet(Sheet sheet) throws RowsExceededException, WriteException
	{
		List<List<String>> readDatas = new ArrayList<List<String>>();
		final int colNum = sheet.getColumns();
		final int rowNum = sheet.getRows();
		for(int i = 0;i < rowNum;i++){
			List<String> colData = new ArrayList<String>();
			for(int j = 0;j < colNum;j++){
				colData.add(sheet.getCell(j, i).getContents());
			}
			readDatas.add(colData);
		}
		return readDatas;
	}
	
	public static void main(String[] args) {
		List<List<List<String>>> readDatas = read(new File("C:/Users/bbb/Desktop/program.xls"));
		for(List<List<String>> sheetData : readDatas){
			for(List<String> rowData : sheetData){
				for(String columnData : rowData){
					System.out.print(columnData + "  ");
				}
				System.out.println();
			}
		}
	}
	
	
}


