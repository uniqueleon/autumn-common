package org.aztec.autumn.common;

public class SqlStringUtils {

	
	public static String integerPadding(int number,int minLength){
		StringBuilder sb = new StringBuilder("" + number);
		if(sb.length() < minLength){
			int paddingSize = minLength - sb.length();
			for(int i = 0;i < paddingSize;i++){
				sb.insert(0, "0");
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(integerPadding(8, 4));
	}
}
