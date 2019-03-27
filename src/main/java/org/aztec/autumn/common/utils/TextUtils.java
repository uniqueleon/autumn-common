package org.aztec.autumn.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

	public TextUtils() {
		// TODO Auto-generated constructor stub
	}

	

	public static double getSimilarity(String title1,String title2){
		double similarity = 0d;
		double sectionCount = 0;
		for(int i = 0;i < title1.length();){
			int j = 0;
			int matchIndex = 0;
			do{
				j++;
				String word = title1.substring(i,i+j);
				Pattern pattern = Pattern.compile(word);
				Matcher matcher = pattern.matcher(title2);
				if(matcher.find()){
					matchIndex = j;
				}
			}while(matchIndex == j && ((i + j + 1) <= title1.length()));
			//String word2 = title2.substring(i)
			Double ratio = Math.pow(new Double(title2.length() - matchIndex) / title2.length(),2);
			Double result = (1 - ratio);
			System.out.println(result);
			similarity += result;
			if(matchIndex == 0){
				i++;
			}
			else{
				i += matchIndex;
			}
			sectionCount ++;
		}
		return similarity / sectionCount;
	}
	
	public static void main(String[] args) {
		TextUtils textUtil = new TextUtils();
		String textString= "abvavaadsdvaewafwqefqwefqwefqwefwqfqwefqwef";
		double similarity = textUtil.getSimilarity("abvavasdvaewa", textString);
		//double similarity = textUtil.getSimilarity("aaaaaaaaaaaaaaa", textString);
		//double similarity =  textUtil.getSimilarity(textString,"afdsf" + textString + "afaf");
		System.out.println(">>>>>>>>>>>>>>>>>>>" + similarity);
	}
}
