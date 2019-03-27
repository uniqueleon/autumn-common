package org.aztec.autumn.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

public class ByteCountTest {
	
	public static void main(String[] args) {
		try {
			Map<Integer,Long> byteCount = readByteCount(1024 * 1024,new File("test-1.gz"));
			List<Long> countSort = Lists.newArrayList();
			for(Integer byteData : byteCount.keySet()) {
				System.out.println(byteData + "__" + byteCount.get(byteData));
				countSort.add(byteCount.get(byteData));
			}
			countSort.sort(new Comparator<Long>() {

				@Override
				public int compare(Long o1, Long o2) {
					// TODO Auto-generated method stub
					return o1 - o2 < 0 ? -1 : 1;
				}
			});
			System.out.println(countSort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static Map<Integer,Long> readByteCount(int bufferSize,File targetFile) throws IOException{
		
		Map<Integer,Long> retMap = Maps.newHashMap();
		FileInputStream fis = new FileInputStream(targetFile);
		byte[] readBytes = new byte[bufferSize];
		int readCount = fis.read(readBytes);
		int cursor = 0;
		while(readCount != -1) {
			for(int i = 0;i < readBytes.length;i++) {
				Integer readByte = (int) readBytes[i];

				Long count = new Long(0);
				if(retMap.containsKey(readByte)) {
					count = retMap.get(readByte);
				}
				count++;
				retMap.put(readByte, count);
				cursor ++;
				System.out.println(cursor);
			}
			readCount = fis.read(readBytes);
		}
		fis.close();
		return retMap;
	}
}
