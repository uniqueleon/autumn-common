package org.aztec.autumn.common.utils.compress.code.phase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.utils.ByteUtils;
import org.aztec.autumn.common.utils.compress.CodeFile;
import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingPhase;
import org.aztec.autumn.common.utils.compress.CodingProgress;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.CompressException.ErrorCode;
import org.aztec.autumn.common.utils.compress.io.CongruenceCodeFile;
import org.aztec.autumn.common.utils.compress.io.MultipleCodeFile;
import org.aztec.autumn.common.utils.compress.io.OverflowCodeFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CongruenceCodePhase implements CodingPhase {
	
	private static final Logger LOG = LoggerFactory.getLogger(CongruenceCodePhase.class);
	
	private static final AtomicLong cost = new AtomicLong(0l);
	private static final AtomicLong reduce = new AtomicLong(0l);
	private static final AtomicLong maxResult = new AtomicLong(0l);
	
	private int[] getCongruenceSequence(CodingConfigure config) {
		String[] seqNoStrArr = config.getCongruenceSequence().split(",");
		int[] congruenceSeq = new int[seqNoStrArr.length];
		for(int i = 0;i < seqNoStrArr.length;i++) {
			congruenceSeq[i] = Integer.parseInt(seqNoStrArr[i]);
		}
		return congruenceSeq;
	}
	
	protected Map<Integer,Integer> getCongruenceMapping(byte[] bytes,CodingConfigure config){
		Map<Integer,Integer> mappingData = Maps.newHashMap();
		int[] congruenceSeq = getCongruenceSequence(config);
		for(int i = 0;i < bytes.length;i++) {
			mappingData.put((int)bytes[i], congruenceSeq[i]);
		}
		return mappingData;
	}
	

	@Override
	public List<CodeFile> encode(CodingConfigure config, List<CodeFile> input, CodingProgress progress)
			throws CompressException {
		// TODO Auto-generated method stub
		try {
			File sourceFile = input.get(0).getFile();
			Long fileLength = sourceFile.length();
			//cost.set(fileLength / 8);
			//ByteBuffer headerBuff = ByteBuffer.allocate(CompressConstant.DEFAULT_HEADER_LENGTH);
			Map<Integer,Integer> dictionary = getCongruenceMapping(input.get(1).toBytes(), config);
			//CodeFile congruenceCode = new CongruenceCodeFile(headers, config);
			progress.setCurrentPhase(this);
			List<List<CodeFile>> codeFiles = Lists.newArrayList();
			codeFiles.add(getCongruenceFiles(config, fileLength));
			codeFiles.add(getOverFlowFiles(config));
			codeFiles.add(getMultipleDataFiles(config));
			startCoding(sourceFile, config, codeFiles, dictionary, progress);
			
		} catch (Exception e) {
			throw new CompressException(ErrorCode.CONGRUENCE_ENCODING_FAIL,e.getMessage(),e);
		}
		return null;
	}
	
	public List<CodeFile> getCongruenceFiles(CodingConfigure config,Long fileLength) throws IOException{

		List<CodeFile> codeFiles = Lists.newArrayList();
		for(int i = 0;i < config.getWorkThreadNum();i++) {
			//codeFiles.add(new CongruenceCodeFile(config));
		}
		return codeFiles;
	}
	
	public List<CodeFile> getOverFlowFiles(CodingConfigure config) throws IOException{

		List<CodeFile> codeFiles = Lists.newArrayList();
		for(int i = 0;i < config.getWorkThreadNum();i++) {
			codeFiles.add(new OverflowCodeFile(config));
		}
		return codeFiles;
	}
	
	public List<CodeFile> getMultipleDataFiles(CodingConfigure config) throws IOException{

		List<CodeFile> codeFiles = Lists.newArrayList();
		for(int i = 0;i < config.getWorkThreadNum();i++) {
			codeFiles.add(new MultipleCodeFile());
		}
		return codeFiles;
	}
	
	
	private List<CodeFile> startCoding(File dataFile,CodingConfigure config,List<List<CodeFile>> codeFiles,
			Map<Integer,Integer> dictionary,CodingProgress progress) throws InterruptedException, IOException, CompressException {
		FileInputStream fis = new FileInputStream(dataFile);
		List<CongruenceCodingThread> threads = Lists.newArrayList();
		Integer bufferSize = config.getBufferSize();
		if(bufferSize > dataFile.length()) {
			bufferSize = new Long(dataFile.length()).intValue();
		}
		Long offset = new Long(0l);
		int readCount = 0;
		List<CodeFile> retCodeFiles = Lists.newArrayList();
		//retCodeFiles.add(new CongruenceCodeFile(config));
		retCodeFiles.add(new OverflowCodeFile(config));
		retCodeFiles.add(new MultipleCodeFile());
		do {

			byte[] readBytes = new byte[bufferSize];
			readCount = fis.read(readBytes);
			if(readCount == -1)
				break;
			CongruenceCodingThread newThread = new CongruenceCodingThread(codeFiles.get(0).get(threads.size())
					,codeFiles.get(1).get(threads.size()),codeFiles.get(2).get(threads.size()), 
					readBytes,dictionary,config);
			newThread.start();
			threads.add(newThread);
			if(threads.size() >= config.getWorkThreadNum()) {
				for(CongruenceCodingThread thread : threads) {	
					thread.join();
					mergeFile(retCodeFiles, thread);
				}
				threads.clear();
				progress.setFinishedWork(offset);
				progress.print();
			}
			//readCount = fis.read(readBytes);
			offset+= readCount;
		}while(readCount != -1);
		if(threads.size() > 0) {
			for(CongruenceCodingThread thread : threads) {	
				thread.join();
				mergeFile(retCodeFiles, thread);
				progress.setFinishedWork(offset);
				progress.print();
			}
			threads.clear();
		}
		fis.close();
		return retCodeFiles;
	}
	
	private void mergeFile(List<CodeFile> mainFiles,CongruenceCodingThread worker) throws CompressException {
		//mainFiles.get(0).merge(worker.getCongruenceCodeFile());
		//mainFiles.get(1).merge(worker.getOverflowFile());
		//mainFiles.get(2).merge(worker.getMultipleFile());
	}
	
	
	private static class CongruenceCodingThread extends Thread{
		
		private CodeFile congruenceFile;
		private CodeFile overflowFile;
		private CodeFile multipleFile;
		private byte[] tmpByte;
		private final Map<Integer,Integer> dictionary;
		private CodingConfigure config;

		private static ThreadLocal<Integer> lastStep = new ThreadLocal<Integer>();
		private static ThreadLocal<Integer> lastMultiple = new ThreadLocal<Integer>();
		private static ThreadLocal<Integer> stepCursor = new ThreadLocal<Integer>();
		private static ThreadLocal<Integer> maxBeginIndex = new ThreadLocal<Integer>();
		private static ThreadLocal<Integer> maxEndIndex = new ThreadLocal<Integer>();
		
		public CongruenceCodingThread(CodeFile congruenceFile, CodeFile overflowFile, CodeFile multipleFile,
				byte[] tmpByte,Map<Integer,Integer> dictionary,CodingConfigure config) {
			super();
			this.congruenceFile = congruenceFile;
			this.overflowFile = overflowFile;
			this.multipleFile = multipleFile;
			this.tmpByte = tmpByte;
			this.dictionary = dictionary;
			this.config = config;
		}

		@Override
		public void run() {
			try {
				Integer maxNumber = config.getStatusRange() - 1;
				List<Integer> locations = Lists.newArrayList();
				List<Boolean> overflowFlags = Lists.newArrayList();
				byte lastByte = 0;

				//int overflowCost = 1;
				//int overflowBase = (int) Math.pow(2, overflowCost);
				//int succetiveValve = 2;
				//int succetiveCount = 0;
				//Boolean lastFlag = null;

				maxBeginIndex.set(-1);
				maxEndIndex.set(-1);
				lastStep.set(-1);
				lastMultiple.set(0);
				stepCursor.set(0);
				for(int i = 0;i < tmpByte.length;i = i + 2) {
					Integer byte1 = dictionary.get((int)tmpByte[i]);
					byte cByte1 = toByte(byte1,maxNumber);
					if(i == 0){
						lastByte = cByte1;
					}
					//lastByte = doTest1(lastByte,cByte1);
					Integer byte2 = 0;
					if(i + 1 < tmpByte.length) {
						byte2 = dictionary.get((int)tmpByte[i + 1]);
					}
					byte cByte2 = toByte(byte2,maxNumber);
					//lastByte = doTest1(lastByte,cByte2);
					byte writeByte = ByteUtils.mix(cByte1 , cByte2);
					congruenceFile.write(new byte[] {writeByte});
					boolean overflow1 = testOverflow(byte1, overflowFlags, maxNumber,i);
						
					
					boolean overflow2 = testOverflow(byte2, overflowFlags, maxNumber,i);
					
					addMultiple(byte1,overflow1,locations);
					addMultiple(byte2,overflow2,locations);
				}
			} catch (CompressException e) {
				LOG.error(e.getMessage(),e);
			}
		}
		
		
		
		
		private boolean testOverflow(Integer testByte,List<Boolean> overflowFlags,Integer maxNumber,Integer cursor) throws CompressException {
			boolean overflow = (testByte >= config.getProduct() && !maxNumber.equals(testByte));
			
			overflowFlags.add(overflow);
			if(overflowFlags.size() >= 8) {
				overflowFile.write(new byte[] {ByteUtils.booleansToByte(overflowFlags)});
			}
			if(!overflow) {
				reduce.addAndGet(3);
			}
			else {
				Integer addBase = 13 + 10;
				if(cursor - stepCursor.get() >= 1000) {
					cost.addAndGet(addBase -10);
					stepCursor.set(cursor);
				}
				else {
					Integer thisStep = (cursor - stepCursor.get()) % 1000;
					int multiple = testByte / config.getProduct();
					if(lastStep.get() != -1) {
						int stepRange = Math.abs((thisStep - lastStep.get() ));
						GreatestCommonDivisor gd = GreatestCommonDivisor.getGCD(new Long[] {lastMultiple.get() + 0l,multiple + 0l});
						int maxStepRange =  lastMultiple.get() + multiple / gd.getGcd().intValue();
						if(thisStep < lastStep.get() || stepRange > maxStepRange ) {
							
							stepCursor.set(cursor);
							cost.addAndGet(addBase);
						}
					}

					lastStep.set(thisStep);
					lastMultiple.set(multiple);
				}
				//cost.addAndGet(1);
			}
			return overflow;
		}
		
		private void addMultiple(Integer testByte,boolean overflow,List<Integer> locations) throws CompressException {
			if(!overflow)
				return ;
			Integer multiple = (testByte / config.getProduct()) - 1;
			if(testByte == 255) {
				return;
			}
			if(locations.size() == 0) {
				locations.add(multiple);
			}
			else if(locations.get(locations.size() - 1) <= multiple ) {
				//doTest2(locations.size());
				multipleFile.write(ByteUtils.integersToByte(locations, 2));
				locations.clear();
			}
			else {
				locations.add(multiple);
			}
		}
		
		
		
		private byte toByte(Integer rawByte,Integer maxNumber) {
			if (rawByte == maxNumber) {
				return maxNumber.byteValue();
			}
			else {
				Integer remainder = rawByte % config.getProduct();
				return remainder.byteValue();
				//codeByte  = codeByte 
			}
		}
		
		
		public CodeFile getCongruenceCodeFile() {
			return congruenceFile;
		}
		
		public CodeFile getOverflowFile() {
			return overflowFile;
		}
		
		public CodeFile getMultipleFile() {
			return multipleFile;
		}
		
	}

	@Override
	public List<CodeFile> decode(CodingConfigure config, List<CodeFile> input, CodingProgress progress)
			throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingPhase nextPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingPhase previousPhase() {
		return new ByteCountingPhase();
	}

	@Override
	public String getName() {
		return "CongruenceCodePhase";
	}

	@Override
	public boolean isLastPhase() {
		return false;
	}

	@Override
	public boolean isFirstPhase() {
		return false;
	}

}
