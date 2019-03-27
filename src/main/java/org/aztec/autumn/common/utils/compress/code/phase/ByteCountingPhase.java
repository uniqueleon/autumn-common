package org.aztec.autumn.common.utils.compress.code.phase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aztec.autumn.common.utils.compress.CodeFile;
import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingPhase;
import org.aztec.autumn.common.utils.compress.CodingProgress;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.CompressException.ErrorCode;
import org.aztec.autumn.common.utils.compress.entity.ByteStatisticInfo;
import org.aztec.autumn.common.utils.compress.io.CongruenceSortTableFile;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

public class ByteCountingPhase implements CodingPhase {

	@Override
	public List<CodeFile> encode(CodingConfigure config, List<CodeFile> input,CodingProgress progress) throws CompressException {
		if(input == null || input.size() == 0)
			throw new CompressException(ErrorCode.WORK_FILE_NOT_FOUND);
		List<CodeFile> codeFiles = Lists.newArrayList();
		try {
			File dataFile = input.get(0).getFile();
			progress.setWorkLoad(dataFile.length());
			List<ByteStatisticInfo> result = startCounting(dataFile, config,progress);
			System.out.println(result);
			codeFiles.addAll(input);
			codeFiles.add(toCodeFile(result, config));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CompressException(ErrorCode.COUNTING_FAIL);
		}
		return codeFiles;
	}
	
	
	public CodeFile toCodeFile(List<ByteStatisticInfo> countData,CodingConfigure config) throws IOException, CompressException {
		byte[] newBytes = new byte[countData.size()];
		long[] counts = new long[countData.size()];
		for(int i = 0;i < countData.size();i++) {
			newBytes[i] = countData.get(i).getByteData();
			counts[i] = countData.get(i).getCount();
			//newBytes[i + 1] = countData.get(i).getCount()
		}
		CongruenceSortTableFile cstf = new CongruenceSortTableFile(config, newBytes,counts);
		return cstf;
	}
	
	public List<ByteStatisticInfo> startCounting(File dataFile,CodingConfigure config,CodingProgress progress) throws IOException, InterruptedException{
		FileInputStream fis = new FileInputStream(dataFile);
		List<CountingThread> threads = Lists.newArrayList();
		Integer bufferSize = config.getBufferSize();
		if(bufferSize > dataFile.length()) {
			bufferSize = new Long(dataFile.length()).intValue();
		}
		Long offset = new Long(0l);
		Map<Integer,Long> countDatas = Maps.newHashMap();
		List<ByteStatisticInfo> result = Lists.newArrayList();
		int readCount = 0;
		do {

			byte[] readBytes = new byte[bufferSize];
			readCount = fis.read(readBytes);
			CountingThread newThread = new CountingThread(readCount, readBytes);
			newThread.start();
			threads.add(newThread);
			if(threads.size() >= config.getWorkThreadNum()) {
				for(CountingThread thread : threads) {	
					thread.join();
					countDatas = merge(countDatas, thread.getResult());
				}
				threads.clear();
				progress.setFinishedWork(offset);
				progress.print();
			}
			//readCount = fis.read(readBytes);
			offset+= readCount;
		}while(readCount != -1);
		if(threads.size() > 0) {
			for(CountingThread thread : threads) {	
				thread.join();
				countDatas = merge(countDatas, thread.getResult());
				progress.setFinishedWork(offset);
				progress.print();
			}
			threads.clear();
		}
		fis.close();
		result = sort(countDatas);
		return result;
	}
	
	private class CountDataSorter implements Comparator<ByteStatisticInfo>{


		@Override
		public int compare(ByteStatisticInfo o1, ByteStatisticInfo o2) {
			// TODO Auto-generated method stub
			return new Long(o2.getCount() - o1.getCount()).intValue();
		}
		
	}
	
	private List<ByteStatisticInfo> sort(Map<Integer,Long> datas){
		List<ByteStatisticInfo> countData = Lists.newArrayList();
		for(Entry<Integer,Long> data : datas.entrySet()) {
			countData.add(new ByteStatisticInfo(data.getKey().byteValue(), data.getValue()));
		}
		countData.sort(new CountDataSorter());
		return countData;
	}
	
	private Map<Integer,Long> merge(Map<Integer,Long> datas1,Map<Integer,Long> datas2){
		if(datas2 == null || datas2.size() == 0) {
			return datas1;
		}
		for(Integer key : datas2.keySet()) {
			Long data1 = datas1.get(key);
			if(data1 == null) {
				datas1.put(key, datas2.get(key));
			}
			else {
				Long data2 = datas2.get(key);
				datas1.put(key, data1 + data2);
			}
		}
		return datas1;
	}
	
	
	
	@Override
	public List<CodeFile> decode(CodingConfigure config, List<CodeFile> input,CodingProgress progress)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingPhase nextPhase() {
		return new HuffmanCodingPhase();
	}

	@Override
	public CodingPhase previousPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLastPhase()  {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFirstPhase()  {
		// TODO Auto-generated method stub
		return true;
	}

	private class CountingThread extends Thread{

		private byte[] buffer;
		private int readSize;
		private Map<Integer,Long> result = Maps.newHashMap();
		private Boolean runnable = true;
		
		public CountingThread(int readSize,byte[] buffer) {
			this.readSize = readSize;
			this.buffer = buffer;
		}
		
		@Override
		public void run() {
			for(int i = 0;i < readSize;i++) {
				Integer readByte = (int) buffer[i];
				
				Long count = 0l;
				if(result.containsKey(readByte)) {
					count = result.get(readByte);
				}
				count++;
				result.put(readByte,count);
			}
		}
		
		public Map<Integer,Long> getResult(){	
			return result;
		}
		
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "byteCountingPhase";
	}
}
