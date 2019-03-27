package org.aztec.autumn.common.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class FileDBWriter {

	public static final String testDirPath = "test/db/data3/";

	public FileDBWriter() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		try {
			Long curTime = System.currentTimeMillis();
			writeFile("test/db/data2/","account_name", 32, 100000, 10000000, "liming");
			writeFile("test/db/data3/","account_name", 32, 100000000, 10000000, "liming");
			Long usedTime = System.currentTimeMillis() - curTime;
			System.out.println(usedTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void writeFile(String testDirPath,String filePrefix,int length,int volumeSize,
			int size,String contentPrefix) throws UnsupportedEncodingException, IOException {
		File testDir = new File(testDirPath);
		if(!testDir.exists()) {
			testDir.mkdirs();
		}
		for(int i = 0;i < size;) {
			int volNo = i / volumeSize;
			int upperLimit = (i + volumeSize ) < size ? volumeSize : (size - i);
			File dataFile = new File(testDirPath + "/" + filePrefix + "_" + volNo + ".dat");
			OutputStream os = new FileOutputStream(dataFile);
			for(int j = 0; j < upperLimit;j++) {
				String writeContent = contentPrefix + "_" + (i + j);
				os.write(toFixedSizeBytes(writeContent, length));
			}
			i += upperLimit;
			os.flush();
			os.close();
		}
	}

	private static byte[] toFixedSizeBytes(String str,int length) throws UnsupportedEncodingException {

		byte[] strBytes = str.getBytes("UTF-8");
		byte[] writeBuffer = new byte[length];
		for(int i = 0;i < strBytes.length;i++) {
			writeBuffer[i] = strBytes[i];
		}
		return writeBuffer;
	}
}
