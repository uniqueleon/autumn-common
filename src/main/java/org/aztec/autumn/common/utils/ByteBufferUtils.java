package org.aztec.autumn.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

import com.google.common.collect.Lists;

public class ByteBufferUtils {

	public static void copy(ByteBuffer buff1,ByteBuffer buff2) {
		buff1.position(0);
		buff1.clear();
		buff1.put(new byte[buff1.capacity()]);
		for(int i = 0;i < buff2.position();i++) {
			buff1.put(i, buff2.get(i));
		}
		
	}
	
	public static byte[] cloneAsArray(ByteBuffer targetBuffer) {
		/*int curPos = targetBuffer.position();
		targetBuffer.position(0);
		byte[] retData = new byte[curPos];
		targetBuffer.get(retData,0,curPos);
		targetBuffer.position(curPos);
		return retData;*/
		return targetBuffer.array();
	}
	
	public static boolean contains(ByteBuffer buff1,ByteBuffer buff2) throws UnsupportedEncodingException {
		return indexOf(buff1, buff2) != -1;
	}
	
	public static boolean contains(ByteBuffer buff,byte[] array) throws UnsupportedEncodingException {
		ByteBuffer testBuffer = ByteBuffer.wrap(array);
		return contains(buff, testBuffer);
	}
	
	public static String getString(ByteBuffer buffer,String charsetName) throws UnsupportedEncodingException {
		//String retString = new String(buffer.array(),charsetName);
		String retString = new String(buffer.array());
		retString = retString.trim();
		return retString;
	}
	
	public static List<Integer> findIndexs(ByteBuffer buff1,ByteBuffer buff2,String charset) throws UnsupportedEncodingException{
		List<Integer> indexs = Lists.newArrayList();
		String txt1 = getString(buff1,charset);
		String txt2 = getString(buff2,charset);
		int fromIndex = 0;
		while(fromIndex != -1) {
			fromIndex = txt1.indexOf(txt2, fromIndex);
			if(fromIndex != -1) {
				fromIndex = txt1.indexOf(txt2, fromIndex) + txt2.length();
				indexs.add(fromIndex);
			}
		}
		return indexs;
	}
	
	/**
	 * 反转buffer ，例子:abcd ---> dcba
	 * @param buffer
	 * @return
	 */
	public static ByteBuffer inverse(ByteBuffer buffer) {
		ByteBuffer retBuffer = ByteBuffer.allocate(buffer.capacity());
		for(int i = buffer.position();i > 0;i--) {
			retBuffer.put(buffer.get(i));
		}
		return retBuffer;
	}
	
	public static ByteBuffer clean(ByteBuffer buffer) {
		buffer.clear();

		buffer.put(new byte[buffer.capacity()]);
		buffer.clear();
		return buffer;
	}
	
	public static ByteBuffer clone(ByteBuffer buffer) {
		
		return ByteBuffer.wrap(buffer.array());
	}
	
	public static Integer indexOf(ByteBuffer buff1,ByteBuffer buff2) throws UnsupportedEncodingException {

		/*String txt1 = getString(buff1,charset);
		String txt2 = getString(buff2,charset);
		return txt1.indexOf(txt2);*/
		int index = -1;
		int matchCount = 0;
		for(int i = 0;i < buff1.position() - buff2.position();i++) {
			for(int j = 0;j < buff2.position();j++) {
				if(buff2.get(j) == buff1.get(i + j)) {
					matchCount ++;
				}
				else {
					break;
				}
			}
			if(matchCount == buff2.position()) {
				index = i;
			}
			else {
				matchCount = 0;
			}
			if(index != -1)
				break;
		}
		return index;
	}
	
	public static Integer lastIndexOf(ByteBuffer buff1,ByteBuffer buff2) throws UnsupportedEncodingException {

		//String txt1 = getString(buff1,charset);
		//String txt2 = getString(buff2,charset);
		//System.out.println(txt1 + "---" + txt2+ "---" + txt1.lastIndexOf(txt2));
		//return txt1.lastIndexOf(txt2);
		int index = -1;
		int matchCount = 0;
		for(int i = buff1.position() - buff2.position();i > 0;i--) {
			for(int j = 0;j < buff2.position();j++) {
				if(buff2.get(j) == buff1.get(i + j)) {
					matchCount ++;
				}
				else {
					break;
				}
			}
			if(matchCount == buff2.position()) {
				index = i;
			}
			else {
				matchCount = 0;
			}
			if(index != -1)
				break;
		}
		return index;
	}
	
	public static void backward(ByteBuffer buffer,int step) {
		int curPos = buffer.position();
		if(curPos == 0)
			return;
		if(curPos - step < 0)
			throw new IllegalArgumentException("Error:can't move back " + step + " step,while cur pos is :" + curPos);
		buffer.position(curPos-step);
		buffer.put(new byte[step]);
		buffer.position(curPos-step);
	}
	

	public static Integer append(ByteBuffer dictBuffer,ByteBuffer lookBuffer) {

		byte[] codeBytes = cloneAsArray(lookBuffer);
		byte[] tmpArray = cloneAsArray(dictBuffer);
		dictBuffer.position(0);
		if((tmpArray.length + codeBytes.length) <= dictBuffer.capacity()) {
			dictBuffer.put(tmpArray);
			dictBuffer.put(codeBytes);
		}
		else {
			int exceedByte = ((tmpArray.length + codeBytes.length) - dictBuffer.capacity());
			dictBuffer.clear();
			for(int i = exceedByte; i < tmpArray.length;i++) {
				dictBuffer.put(tmpArray[i]);
			}
			for(int i = 0; i < codeBytes.length;i++) {
				dictBuffer.put(codeBytes[i]);
			}
		}
		return codeBytes.length;
	}
	
	public static void append(ByteBuffer buffer,byte[] bytes) {
		ByteBuffer tmpBuffer = ByteBuffer.wrap(bytes);
		append(buffer, tmpBuffer);
	}
	
	
}
