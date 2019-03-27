package org.aztec.autumn.common.db;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class FileDbReader {


	public static final String testDirPath = "test/db/data2/";
	
	public FileDbReader() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		try {
			int batch = 1000;
			int dataLength = 32;
			int fileNum = 100;
			int volumeSize1 = 10000;
			int volumeSize2 = 1000000;
			Long curTime = System.currentTimeMillis();
			readMultiFile("test/db/data2/account_name", dataLength, volumeSize1, fileNum, batch);
			Long usedTime = System.currentTimeMillis() - curTime;
			System.out.println("MULTI use:" + usedTime);
			curTime = System.currentTimeMillis();
			readSingleFile("test/db/data3/account_name_0.dat", batch, dataLength, volumeSize2);
			usedTime = System.currentTimeMillis() - curTime;
			System.out.println("SINGLE use:" + usedTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void readMultiFile(String filePrefix,int dataLength,int volumeSize,int fileNum,int batch) throws IOException {
		
		Random random = new Random();
		List<RandomAccessFile> files = Lists.newArrayList();
		List<FileChannel> channels = Lists.newArrayList();
		for(int i = 0;i < fileNum;i++) {
			File targetFile = new File(filePrefix + "_" + i + ".dat");
			RandomAccessFile raf = new RandomAccessFile(targetFile, "r");
			FileChannel fc = raf.getChannel();
			files.add(raf);
			channels.add(fc);
		}
		Long curTime = System.currentTimeMillis();
		for(int i = 0;i < batch ;i++) {
			Integer randomNum = random.nextInt(fileNum);
			FileChannel fc = channels.get(randomNum);
			long position = random.nextInt(volumeSize) * dataLength;
			fc.position(position);
			ByteBuffer bb = ByteBuffer.allocate(dataLength);
			fc.read(bb);
			//System.out.println(new String(bb.array()));
		}

		Long usedTime = System.currentTimeMillis() - curTime;
		System.out.println("Actually read use:" + usedTime);
		for(int i = 0;i < fileNum;i++) {
			FileChannel fc = channels.get(i);
			RandomAccessFile raf = files.get(i);
			fc.close();
			raf.close();
		}
	}
	
	public static void readSingleFile(String filePath,int batch,int dataLength,int volumeSize) throws IOException {

		Random random = new Random();
		File targetFile = new File(filePath);
		RandomAccessFile raf = new RandomAccessFile(targetFile, "r");
		FileChannel fc = raf.getChannel();
		for(int i = 0;i < batch;i++) {

			long position = random.nextInt(volumeSize) * dataLength;
			fc.position(position);
			ByteBuffer bb = ByteBuffer.allocate(dataLength);
			fc.read(bb);
			//System.out.println(new String(bb.array()));
		}
		fc.close();
		raf.close();
	}
	
}
