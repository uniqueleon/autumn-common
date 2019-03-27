package org.aztec.autumn.common.utils.ocr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRReader {

	public static void main(String[] args) {
		try {
			genAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String testSingle(ITesseract instance,File file) {
		return null;
	}
	
	public static void genAll() throws IOException, InterruptedException {
		File imageFileDir = new File("D:\\work\\test\\zyyhxfys");
		//File[] imageFiles = imageFileDir.listFiles();
		int threadNum = 35;
		int begin = 1;
		int end = 36;
		
		StringBuilder builder  = new StringBuilder();
		List<Thread> runningThreads = new ArrayList<>();
		List<OCRThread> ocrThread = new ArrayList<>();
		
		for(int j = begin;j < end;) {
			int realThreadNum = (j + threadNum ) < end ? threadNum : end - j ;
			for(int i = 0;i < realThreadNum;i++) {
				File imgFile = new File(imageFileDir.getAbsolutePath() + "/zyyhxfys_" + (j + i) + "_I.jpg");
				if(!imgFile.exists())
				{
					System.exit(-1);
				}
				OCRThread ocrThd = new OCRThread(j + i,imgFile);
				ocrThread.add(ocrThd);
				Thread runThread = new Thread(ocrThd);
				runningThreads.add(runThread);
				runThread.start();
			}
			for(Thread testThread : runningThreads) {
				testThread.join();
			}
			for(int i = 0;i < ocrThread.size();i++) {
				OCRThread ocrThd = ocrThread.get(i);
				builder.append(ocrThd.getResult());
			}
			j = j + realThreadNum;
		}
		
		System.out.println("导出完毕");
		File targetFile = new File("test/jylc.txt");
		if(!targetFile.exists()) {
			targetFile.createNewFile();
		}
		FileUtils.write(targetFile, builder.toString(), "UTF-8");
	}
	
	
	public static class OCRThread implements Runnable{
		
		private File imageFile;
		private String result;
		private int index;
		
		public OCRThread(int index,File imgFile) {
			this.imageFile = imgFile;
			this.index = index;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println("正在导出第" + index + "页...");
				ITesseract instance = getTesseractInstance();
				result = instance.doOCR(imageFile);
				System.out.println("第" + index + "页导出完成!");
			} catch (TesseractException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
		
		
		
	}
	
	
	public static ITesseract getTesseractInstance() {
		ITesseract instance = new Tesseract();  // JNA Interface Mapping
		// ITesseract instance = new Tesseract1(); // JNA Direct Mapping
		instance.setDatapath("D:\\work\\develop\\workspaces3\\autumn-common\\test");
		instance.setLanguage("chi_sim");
		return instance;
	}

}
