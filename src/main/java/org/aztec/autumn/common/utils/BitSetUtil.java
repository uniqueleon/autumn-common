package org.aztec.autumn.common.utils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ���ڴ������λ����Ķ����������Ĺ����ࡣ�����ڼ�Ȩ������ƥ���ҵ�񳡾�.
 * @author 10064513
 *
 */
public class BitSetUtil {
	
	public static final int BIT_SET_READ_INDEX = 0;
	public static final int VERSION_READ_INDEX = 1;
	public static final Map<Character,Integer> charIndexMap = new ConcurrentHashMap<Character, Integer>();
	
	static {
		charIndexMap.put(new Character('0'), 0);
		charIndexMap.put(new Character('1'), 1);
		charIndexMap.put(new Character('2'), 2);
		charIndexMap.put(new Character('3'), 3);
		charIndexMap.put(new Character('4'), 4);
		charIndexMap.put(new Character('5'), 5);
		charIndexMap.put(new Character('6'), 6);
		charIndexMap.put(new Character('7'), 7);
		charIndexMap.put(new Character('8'), 8);
		charIndexMap.put(new Character('9'), 9);
		charIndexMap.put(new Character('a'), 10);
		charIndexMap.put(new Character('b'), 11);
		charIndexMap.put(new Character('c'), 12);
		charIndexMap.put(new Character('d'), 13);
		charIndexMap.put(new Character('e'), 14);
		charIndexMap.put(new Character('f'), 15);
	}

	public static BitSet array2BitSet(List<Integer> locations){
		BitSet bs = new BitSet();
		for(Integer loc : locations){
			bs.set(loc);
		}
		return bs;
	}
	
	public static String toString(BitSet bitSet,Integer version){
		long[] dataArray = bitSet.toLongArray();
		StringBuilder builder = new StringBuilder();
		for(int i = 0;i < dataArray.length;i++){
			if(i != 0 && 
					i != dataArray.length){
				builder.append("_");
			}
			builder.append(dataArray[i]);
			/*if(dataArray[i] == Long.MAX_VALUE){
				builder.append("8000000000000000");
				//builder.append(dataArray[i]);
			}
			else{
				builder.append(Long.toHexString(dataArray[i]));
			}*/
		}
		builder.append("(" + version + ")");
		return builder.toString();
	}
	
	public static BitSetData readBitSet(String bitsetString){
		if(StringUtils.isBlank(bitsetString))
			return new BitSetData(new BitSet(), 0);
		int backetIndex = bitsetString.indexOf("(");
		String versionNumStr = bitsetString.substring(backetIndex + 1,bitsetString.length() - 1);
		Integer version = 0;
		if(!StringUtils.isBlank(versionNumStr)){
			version = Integer.parseInt(versionNumStr);
		}
		String workString = bitsetString.substring(0,backetIndex);
		if(StringUtils.isBlank(workString))
			return new BitSetData(new BitSet(), version);
		String[] longStrArr = workString.split("_");
		List<Long> longList = new ArrayList<>();
		boolean overflow = false;
		int length = longStrArr.length;
		for(int i = 0;i < length;i++){
			
			try {
				long longValue = Long.parseLong(longStrArr[i]);
				//long longValue = longStrArr.length > i ? hex2Long(longStrArr[i]) : 0;
				if(overflow){
					longValue ++ ;
					if(longValue == Long.MAX_VALUE){
						throw new OverFlowException();
					} else{
						longList.add(longValue);
					}
				}
				longList.add(longValue);
				overflow =false;
			} catch (OverFlowException e) {
				longList.add(0l);
				if(i == length - 1){
					length ++;
				}
				overflow = true;
			}
		}
		long[] longArr = new long[longList.size()];
		for(int i = 0;i < longList.size();i++){
			longArr[i] = longList.get(i);
		}
		BitSet readBitset = BitSet.valueOf(longArr);
		return new BitSetData(readBitset,version);
	}
	
	private static class OverFlowException extends Exception{

		public OverFlowException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public OverFlowException(String arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public static long hex2Long(String hexString) throws OverFlowException{
		long retValue = 0;
		int length = hexString.length();
		for(int i = length - 1;i >= 0;i--){
			char checkChar = hexString.charAt(i);
			int hexIndex = -1;
			hexIndex = charIndexMap.get(new Character(checkChar));
			if(hexIndex != -1){
				long addValue = (long) (hexIndex * Math.pow(16, length - (i+1)));
				if(addValue == Long.MAX_VALUE){
					throw new OverFlowException();
				}
				retValue += addValue;
			}
			else{
				throw new IllegalArgumentException("");
			}
		}
		return retValue;
	}
	
	public static List<Integer> readSetBits(BitSet readBs){
		List<Integer> locations = new ArrayList<>();
		int bitIndex = readBs.nextSetBit(0);
		int lastBitIndex = bitIndex;
		while(bitIndex != -1){
			locations.add(bitIndex);
			bitIndex = readBs.nextSetBit((bitIndex + 1));
			if(lastBitIndex != bitIndex)
				lastBitIndex = bitIndex;
			else{
				break;
			}
		}
		return locations;
	}
	
	public static List<Long> readSetBitsAsLong(String acl){
		List<Integer> locations = readSetBits(readBitSet(acl).getData());
		List<Long> longValues = new ArrayList<>();
		for(Integer loc : locations){
			longValues.add(new Long(loc));
		}
		return longValues;
	}
	
	public static String doIntersection(String acl1,String acl2){
		BitSetData bsData = readBitSet(acl1);
		BitSetData bsData2 = readBitSet(acl2);
		BitSet bs1 = bsData.getData();
		BitSet bs2 = bsData2.getData();
		bs1.and(bs2);
		return toString(bs1, bsData.getVersion());
	}
	
	public static class BitSetData{
		private BitSet data;
		private int version;
		public BitSetData(BitSet data, int version) {
			super();
			this.data = data;
			this.version = version;
		}
		public BitSet getData() {
			return data;
		}
		public void setData(BitSet data) {
			this.data = data;
		}
		public int getVersion() {
			return version;
		}
		public void setVersion(int version) {
			this.version = version;
		}
		
	}
	
	
	
}
