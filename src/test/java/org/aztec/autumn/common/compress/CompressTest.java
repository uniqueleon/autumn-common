package org.aztec.autumn.common.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.base.CongruenceCoder;
import org.aztec.autumn.common.utils.compress.base.HuffmanCoder;
import org.aztec.autumn.common.utils.compress.code.HuffmanCode;
import org.aztec.autumn.common.utils.compress.code.phase.ByteCountingPhase;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CompressTest {

	public static void main(String[] args) {
		try {
			//dataFileGenerate("test/compress/data2.txt",2800170 * 2, 1024);
			//countSequence("test/compress/data2.txt", 15, 1024);
			//countSequence("test/compress/Jellyfish_2.jpg", 10, 1024 * 1024);
			//testCongruenceCodeCompress();
			//System.out.println(329938158 % 4967);
			
			/*double bitHitRate = Math.pow((16d / 17d), 2) + Math.pow((1d/17), 2d);
			double totalHitRage = Math.pow(bitHitRate, 8);
			System.out.println(totalHitRage);*/
			
			//System.out.println(1 << 8);
			Map<Integer,Long> bufferMap =  readBuffer(new File("test/compress/test-1.cpz"));
			Long totalLength = 0l;
			for(Entry<Integer,Long> entry : bufferMap.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
				totalLength += entry.getValue();
			}
			totalLength = totalLength * 8;
			Long compressLength = 0l;
			List<HuffmanCode> hManCode = toHuffmanCode(bufferMap);
			for(HuffmanCode manCode : hManCode) {
				compressLength += manCode.getOldCode().getFrequence() * manCode.getNewCode().getLength();
			}
			double compressRate = compressLength * 1d / totalLength;
			double valve = 4d / 17;
			boolean success = valve > compressRate; 
			System.out.println("compress rate:" + compressRate + ",success:" + success);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static List<HuffmanCode> toHuffmanCode(Map<Integer,Long> byteFrequences){
		
		List<HuffmanCode> rawCodes = Lists.newArrayList();
		for(Entry<Integer,Long> entry : byteFrequences.entrySet()) {
			rawCodes.add(new HuffmanCode(entry.getKey(), entry.getValue()));
		}
		HuffmanCoder coder = new HuffmanCoder();
		return coder.transfer(rawCodes);
	}
	

	private static Map<Integer,Long> readBuffer(File dataFile) throws IOException {

		Long length = dataFile.length();
		//int capacity = (int) ((length % 8 == 0) ? length / 8 : (length / 8 ) + 1);
		int capacity = 8 * 1024 * 1024;
		ByteBuffer buffer = ByteBuffer.allocate(capacity);
		Integer readCount = 0, cursor = 0;
		RandomAccessFile rFile = new RandomAccessFile(dataFile,"r");
		FileChannel fc = rFile.getChannel();
		Map<Integer,Long> frequecesMap = Maps.newHashMap();
		fc.position(cursor);
		while(fc.read(buffer) != -1) {
			buffer.flip();
			readCount ++;
			System.out.println("read:" + (readCount * 8 ) + " mb!");
			int writeByte = 0;
			while(buffer.hasRemaining()) {
				byte readByte = buffer.get();
				int intValue = Byte.toUnsignedInt(readByte);
				if(cursor == 8) {
					Long frequence = frequecesMap.get(writeByte);
					frequence = frequence == null ? 1l : frequence + 1l;
					frequecesMap.put(writeByte, frequence);
					cursor = 0;
					writeByte = 0;
				}
				if(intValue > 14 && intValue != 255 ) {
					writeByte = writeByte | (1 << cursor);
				}
				cursor++;
			}
			buffer.clear();
		}
		rFile.close();
		return frequecesMap;
	}
	
	private static void testZip() {
		try {
			//CompressUtils.zip("test/compress/data.txt", "test/compress/data.zip", 1024, true);
			//CompressUtils.zip("test/compress/data2.txt", "test/compress/data2.zip", 1024, true);
			/*File testFile = new File("test/compress/data.txt");
			String strText = FileUtils.readFileAsString(testFile);
			System.out.println(strText);

			CodeCipher cipher = new CodeCipher();
			Long curTime = System.currentTimeMillis();*/
			//cipher.getMD5Substract(strText, "UTF-8");
			/*String sha512 = CodeCipher.SHA512(strText);
			System.out.println("use Time : " + (System.currentTimeMillis() - curTime));
			System.out.println(sha512);
			System.out.println(sha512.getBytes().length);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testCongruenceCodeCompress() {
		try {
			CodingConfigure config = new CodingConfigure("limingTest",new int[] {3, 5}, 256, 1024 * 1024, 1, "UTF-8", 
					"test/compress/eclipse_3.dat","test/compress/test-1.cpz");
			System.out.println(config.getCongruenceSequence());
			CongruenceCoder coder = new CongruenceCoder(new ByteCountingPhase());
			coder.encode(config);
		} catch (CompressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void dataFileGenerate(String filePath,int length,int bufferSize) throws Exception {
		File dataFile = new File(filePath);
		if(!dataFile.exists()) {
			dataFile.createNewFile();
		}
		RandomAccessFile raf = new RandomAccessFile(dataFile, "rw");
		Random random = new Random();
		int round = 0;
		int writeSize = bufferSize;
		while(round * bufferSize < length) {
			if((round + 1) * bufferSize > length) {
				writeSize = (length - round * bufferSize);
			}
			byte[] writeBuff = new byte[writeSize];
			filtBytes(random,writeBuff);
			raf.write(writeBuff);
			round ++;
		}
		raf.close();
		
	}
	
	private static byte[] filtBytes(Random random,byte[] writeBuff) throws Exception {
		for(int i = 0;i < writeBuff.length;i++) {
			writeBuff[i] = (byte) random.nextInt(16);
			if(writeBuff[i] >= 16)
				throw new Exception("file generate fail!");
		}
		return writeBuff;
	}
	
	private static class ByteScore {
		private byte rawByte;
		private Long score = 0l;
		
		
	}
	
	private static class ByteSequenceCount {
		private byte[] byteSequence;
		private long count = 0l;
		
		public byte[] getByteSequence() {
			return byteSequence;
		}
		public void setByteSequence(byte[] byteSequence) {
			this.byteSequence = byteSequence;
		}
		public long getCount() {
			return count;
		}
		public void setCount(long count) {
			this.count = count;
		}
		public ByteSequenceCount(byte[] rawByte, long count) {
			super();
			this.byteSequence = rawByte;
			this.count = count;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(byteSequence);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ByteSequenceCount other = (ByteSequenceCount) obj;
			if (!Arrays.equals(byteSequence, other.byteSequence))
				return false;
			return true;
		}
	
		public String toDigitString() {
			StringBuilder builder = new StringBuilder();
			for(byte bite : byteSequence) {
				builder.append("[" + (int) bite + "]");
			}
			return builder.toString();
		}
	}

	private static void countSequence(String filePath,int maxSeqLenght,int bufferSize) throws IOException {
		Map<Integer,ByteSequenceCount> sequeceCountMap= Maps.newHashMap();
		byte[] checkBuffer = new byte[bufferSize];
		File tmpFile = new File(filePath);
		FileInputStream fis = new FileInputStream(tmpFile);
		int readSize = fis.read(checkBuffer);
		byte[] checkByte = new byte[maxSeqLenght];
		int checkCursor = 0;
		byte testByte  = 0;
		boolean isContinue = false;
		while(readSize != -1) {
			for(int i = 0;i < readSize;i++) {
				if(i != 0) {
					isContinue = false;
				}
				if(isContinue && isContains(checkByte,checkCursor,checkBuffer[i])) {
					updateSequenceMap(sequeceCountMap, checkByte,checkCursor);
					checkCursor = 0;
					continue;
				}
				else {
					checkByte[checkCursor] = checkBuffer[i];
				}
				checkCursor++;
				if (checkCursor >= maxSeqLenght) {
					updateSequenceMap(sequeceCountMap, checkByte,checkCursor);
					checkCursor = 0;
				} else if(i + 1 < readSize){
					testByte = checkBuffer[i + 1];
					if(isContains(checkByte,checkCursor,testByte) || checkCursor >= maxSeqLenght) {
						updateSequenceMap(sequeceCountMap, checkByte,checkCursor);
						checkCursor = 0;
					}
				}
				if(i + 1 >= readSize && checkCursor != 0) {
					isContinue = true;
				} 
			}
			readSize = fis.read(checkBuffer);
		}
		fis.close();
		long totalCount = 0l;
		for(Integer hashCode : sequeceCountMap.keySet()) {
			ByteSequenceCount bsc = sequeceCountMap.get(hashCode);
			totalCount += bsc.getByteSequence().length * bsc.getCount();
			
			if(bsc.getCount() > 1)System.out.println(bsc.toDigitString() + "-" + bsc.getCount());
		}

		Set<Integer> keys = sequeceCountMap.keySet();
		System.out.println("key count : " + keys.size());
		System.out.println("TOTAL COUNT :" + totalCount);
		System.out.println("file lenght:" + tmpFile.length());
	}
	
	private static boolean isContains(byte[] chkBytes,int testLength,byte thisByte) {
		for(int i = 0;i < testLength;i++) {
			if(chkBytes[i] == thisByte) {
				return true;
			}
		}
		return false;
	}
	
	private class ByteSequenceComparator implements Comparator<ByteSequenceCount>{

		@Override
		public int compare(ByteSequenceCount o1, ByteSequenceCount o2) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
	
	
	
	private static void updateSequenceMap(Map<Integer,ByteSequenceCount> sequenceMap,byte[] byteSeq,int checkLength) {

		ByteBuffer bBuffer = ByteBuffer.allocate(checkLength);
		bBuffer.put(byteSeq, 0, checkLength);
		byte[] arrBytes = bBuffer.array();
		Integer hashCode = Arrays.hashCode(arrBytes);
		if(sequenceMap.containsKey(hashCode)) {
			ByteSequenceCount bsc = sequenceMap.get(hashCode);
			bsc.setCount(bsc.getCount() + 1);
		}
		else {
			sequenceMap.put(hashCode, new ByteSequenceCount(arrBytes, 1l));
		}
	}
}
