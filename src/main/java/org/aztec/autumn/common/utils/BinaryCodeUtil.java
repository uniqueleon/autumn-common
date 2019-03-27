package org.aztec.autumn.common.utils;

import java.util.Random;

/**
 * <p>
 * A util for reading and writing a unlimited length digital number as binary code. Every number String will be recognize as a 18 length number.<br/>
 * if a number's length larger than 18, it will be slice into 18 pieces. Each number slice will be handle independently and can be located by number.    
 * <br/>
 * Example:
 * <br/>
 * <br/>
 * 000000000000000023000000000000000233 will be slice to 000000000000000021,000000000000000233
 * <br/>
 * 000000000000000023 will be handle as binary form 
 * <br/>
 * </p>
 * <p>
 * 一个用于将无长度限制的数字转换成二进制码的工具。每个数字（字符串形式表示）会被分成18个数字一组的长整形数字（为了与Java的长整形兼容，同时又不想用BigDecimal类。）。<br/>
 * 如果一个数字的长度大于18，它就会被成几段（每段会有一个段号，从最左面开始数）。每个片段独立处理，而且可以通过数字来定位。
 * <br/>
 * 该工具类主要用于压缩权限字符串的大小，同时能提供一个快速的办法来访问权限数据标志位。
 * <br/>
 * 例子：
 * <br/>
 * 000000000000000023000000000000000233 会被分拆成 000000000000000021,000000000000000233
 * <br/>
 * 000000000000000023 会以二进制码的形式进行独立处理。 
 * </p>
 * @author 黎明
 *
 */
public class BinaryCodeUtil {

	public static int SLICE_MAX_LENGTH = 18;
	public static int SLICE_MAX_LOCACTION = 59;
	
	public BinaryCodeUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getBitValue(String decCode,int loc){
	  return getBitValue(decCode,getSliceNo(loc),getSliceLoc(loc));
	}
	
	public static String setValue(String decCode,int loc,int value){
	  return setValue(decCode, getSliceNo(loc),getSliceLoc(loc),value);
	}
	
	public static String merge(String sourceCode,String targetCode){
		String[] targetSlices = getSlices(targetCode);
		String tmpString = appendSlice(sourceCode,targetSlices.length);
		String[] sourceSlices = getSlices(tmpString);
		String result = new String();
		for(int i = 0;i < targetSlices.length;i++){
			long sourceSliceValue = Long.parseLong(targetSlices[i]);
			long tmpSliceValue = Long.parseLong(sourceSlices[i]);
			long newValue = sourceSliceValue | tmpSliceValue;
			result = toSliceString(newValue) + result;
		}
		return result.toString();
	}
	
	
	private static String appendSlice(String sourceCode,int sliceNumber){
		StringBuilder tmpCodeBuilder = new StringBuilder(sourceCode);
		while(tmpCodeBuilder.length() < (sliceNumber * SLICE_MAX_LENGTH)){
			tmpCodeBuilder.append("0");
		}
		return tmpCodeBuilder.toString();
	}
	
	public static boolean isInclude(String sourceCode,String targetCode){

		String[] targetSlices = getSlices(targetCode);

		String[] sourceSlices = getSlices(sourceCode);
		for(int i = 0;i < targetSlices.length ;i++){
			long targetVal = Long.parseLong(targetSlices[i]);
			if(i < sourceSlices.length){
				long sourceVal = Long.parseLong(sourceSlices[i]);
				if((sourceVal & targetVal) != targetVal){
					return false;
				}
			}
			else{
				if(targetVal > 0){
					return false;
				}
			}
		}
		return true;
	}
	
	public static int getSliceNo(int loc){
	  
	  return loc / SLICE_MAX_LOCACTION;
	}
	
	public static int getSliceLoc(int loc){
	  return loc % SLICE_MAX_LOCACTION;
	}
	
	
	/**
	 * 取数字字符串某一位的上值
	 * 
	 * @param decCode 完整的数字字符串
	 * @param sliceNo 段号
	 * @param loc 段中的位置
	 * @return 1 或 0
	 */
	public static int getBitValue(String decCode, int sliceNo, int loc){
		
		String[] slices = getSlices(decCode);
		if(loc >= SLICE_MAX_LOCACTION)
			throw new IllegalArgumentException("The locaction[" + loc + "] is not allowed while the max location only is " + (SLICE_MAX_LOCACTION - 1));
		if(sliceNo >= slices.length)
			throw new IllegalArgumentException("The slice[no=" + sliceNo + "] is not exists because " + decCode + " only has " + slices.length + " slices!");
		long val = Long.parseLong(slices[sliceNo]);
		//return 0;
		//return ((val & (1 << loc)) == 0) ? 0 : 1;
		return ((val & Math.round(Math.pow(2, loc))) == 0) ? 0 : 1;
	}
	
	/**
	 * 将十进制代码打成若干个18位的十进制数，从最右边的数字开始放进字符串数组中。
	 * 
	 * @param decCode 十进制数码
	 * @return
	 */
	public static String[] getSlices(String decCode){
		String temp = decCode;
		int arrayLength = (temp.length() % SLICE_MAX_LENGTH == 0) ? temp.length() / SLICE_MAX_LENGTH : (temp.length() / SLICE_MAX_LENGTH) + 1; 
		String[] tempArray = new String[arrayLength];
		for(int i = 0;i < tempArray.length;i++){
			if(temp.length() < SLICE_MAX_LENGTH){
				tempArray[i] = temp;
				break;
			}
			tempArray[i] = temp.substring(temp.length() - SLICE_MAX_LENGTH);
			temp = temp.substring(0, temp.length() - SLICE_MAX_LENGTH);
		}
		return tempArray;
	}
	
	/**
	 * 设置数字字符串的值。
	 * 
	 * @param decCode 数字字符号
	 * @param sliceNo 段号
	 * @param loc 段中位置
	 * @param value 值，0或1
	 * @return 设置完成后的值
	 */
	
	public static String setValue(String decCode, int sliceNo, int loc,int value){

		String[] slices = getSlices(decCode);
		if(loc >= SLICE_MAX_LOCACTION)
			throw new IllegalArgumentException("The locaction[" + loc + "] is not allowed while the max location only is " + (SLICE_MAX_LOCACTION - 1));
		if(sliceNo >= slices.length)
			throw new IllegalArgumentException("The slice[no=" + sliceNo + "] is not exists because " + decCode + " only has " + slices.length + " slices!");
		if(value != 0 && value != 1)
			throw new IllegalArgumentException("Only support 1 or 0 as a bit value!");			
		long val = Long.parseLong(slices[sliceNo]);
		//return 0;
		String newValue;
		if(value == 0){
		  //newValue = padding(new String("" + (val & (~(1 << loc)))));
		  newValue = padding(new String("" + (val & (~Math.round(Math.pow(2, loc))))));
		}
		else{
		  //newValue = padding(new String("" + (val | (1 << loc))));
			newValue = padding(new String("" + (val | Math.round(Math.pow(2, loc)))));
		}
		String result = "";
		for(int i = 0;i < slices.length;i++){
			if(i == sliceNo)
				result = newValue + result;
			else{
				if(slices[i].length() < SLICE_MAX_LENGTH)
					result = padding(slices[i]) + result;
				else
					result = slices[i] + result;
			}
		}
		return result;
	}
	
	private static String padding(String longVal){
		if(longVal.length() < SLICE_MAX_LENGTH){
			String newString = longVal;
			for(int i = longVal.length();i < SLICE_MAX_LENGTH;i++){
				newString = "0" + newString;
			}
			return newString;
		}
		return new String(longVal);
	}
	
	public static String toSliceString(long longVal){
		return padding("" + longVal);
	}

}
