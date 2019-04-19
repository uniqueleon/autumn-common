package org.aztec.autumn.common.utils;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.io.CharTypes;

public class StringUtils {

	private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
	private static final int[] ESCAPE_CODES = CharTypes.get7BitOutputEscapes();
	private static Random random = new Random();

	public StringUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String getUUID() {

		return UUID.randomUUID().toString();
	}

	public static String toHexString(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte oneByte : bytes) {
			builder.append(Integer.toHexString(oneByte));
			builder.append('#');
		}
		return builder.toString();
	}

	public static byte[] hexToBytes(String hexString) {
		String[] byteString = hexString.split("#");
		byte[] bytes = new byte[byteString.length];
		for (int i = 0; i < byteString.length; i++) {
			try {
				// bytes[i] = (byte) Integer.parseInt("0x" + byteString[i],16);
				bytes[i] = Integer.decode("0x" + byteString[i]).byteValue();
			} catch (NumberFormatException e) {
				try {
					String byteStr = byteString[i];
					int[] charValues = new int[byteStr.length()];
					StringBuilder revStr = new StringBuilder();
					for (int j = 0; j < byteStr.length(); j++) {

						charValues[j] = Character.digit(byteStr.charAt(j), 16);
						charValues[j] = charValues[j] ^ 15;
						revStr.append(Integer.toHexString(charValues[j]));
					}
					bytes[i] = (byte) (Integer.decode("-0x" + revStr) - 1);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}

			}
		}
		return bytes;
	}

	public static String getRandomInt() {
		return "" + random.nextInt((Integer.MAX_VALUE / 2));
	}

	public static String changeToUnicode(String content) {
		StringBuilder builder = new StringBuilder();
		for (char c : content.toCharArray()) {
			if (c >= 0x80) {
				writeUnicodeEscape(builder, c); // 为所有非ASCII字符生成转义的unicode字符
			} else {
				// 为ASCII字符中前128个字符使用转义的unicode字符
				int code = (c < ESCAPE_CODES.length ? ESCAPE_CODES[c] : 0);
				if (code == 0) {
					builder.append(c); // 此处不用转义
				} else if (code < 0) {
					writeUnicodeEscape(builder, (char) (-code - 1)); // 通用转义字符
				} else {
					writeShortEscape(builder, (char) code); // 短转义字符 (\n \t ...)
				}
			}
		}
		return builder.toString();
	}

	public static String unicode2String(String unicode) {
		
		String codeString = unicode;
		Pattern pattern = Pattern.compile("\\\\u[0-9|a-f]{4}");
		Matcher matcher = pattern.matcher(codeString);
		while(matcher.find()){
			String encodeStr = matcher.group();
			String decodeStr =  unicodeCharToStr(encodeStr);
			codeString = codeString.replace(encodeStr, decodeStr);
		}
		return codeString;
	}

	public static String unicodeCharToStr(String unicode) {
		StringBuffer string = new StringBuffer();

		String[] hex = unicode.split("\\\\u");

		for (int i = 1; i < hex.length; i++) {

			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);

			// 追加成string
			string.append((char) data);
		}

		return string.toString();
	}

	private static void writeUnicodeEscape(StringBuilder builder, char c) {
		builder.append('\\');
		builder.append('u');
		builder.append(HEX_CHARS[(c >> 12) & 0xF]);
		builder.append(HEX_CHARS[(c >> 8) & 0xF]);
		builder.append(HEX_CHARS[(c >> 4) & 0xF]);
		builder.append(HEX_CHARS[c & 0xF]);
	}

	private static void writeShortEscape(StringBuilder builder, char c) {
		builder.append('\\');
		builder.append(c);
	}

	public static String getRamdonNumberString(int length) {
		Random random = new Random();
		StringBuilder randomKey = new StringBuilder();
		for (int i = 0; i < length; i++) {
			randomKey.append("" + random.nextInt(10));
		}
		return randomKey.toString();
	}

	public static String getRandomCharNumberString(int length) {
		final int[][] ACCEPTABLE_RANGES = new int[][] { { 48, 57 }, { 65, 90 }, { 97, 122 } };
		StringBuilder secretBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			secretBuilder.append(getRandomChar(ACCEPTABLE_RANGES));
		}
		return secretBuilder.toString();
	}

	private static char getRandomChar(int[][] acceptableRanges) {
		Random random = new Random();
		int retChar = random.nextInt(128);
		while (!isCharAcceptable(retChar, acceptableRanges)) {
			retChar = random.nextInt(128);
		}
		return (char) retChar;
	}

	private static boolean isCharAcceptable(int character, int[][] acceptableRanges) {
		for (int[] acceptableRange : acceptableRanges) {
			if (character >= acceptableRange[0] && character <= acceptableRange[1])
				return true;
		}
		return false;
	}

	public static String subString(String regex, String text, int index) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			if (index == 0)
				return matcher.group();
			else {
				String findString = null;
				while (index > 0) {
					if (matcher.find()) {
						findString = matcher.group();
						index--;
					} else {
						return null;
					}
				}
				return findString;
			}
		}
		return null;
	}

	public static String setCharPadding(String text, int paddingNum,char paddingChar) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			builder.append(text.substring(i, i + 1));
			for (int j = 0; j < paddingNum; j++) {
				builder.append(paddingChar);
			}
		}
		return builder.toString();
	}
	
	public static String padding(boolean left,String text,int maxLength,char paddingChar) {
		String retText = new String(text);
		if(retText.length() > maxLength) {
			return retText;
		}
		int diff = maxLength - text.length();
		for(int i = 0; i < diff;i++) {
			if(left) {
				retText = paddingChar + retText;
			}
			else {
				retText += paddingChar;
			}
		}
		return retText;
	}
	
	public static String capitalize(String text){
		StringBuilder builder = new StringBuilder();
		builder.append(text.substring(0,1).toUpperCase());
		builder.append(text.substring(1));
		return builder.toString();
	}

	public static boolean isBlank(String testStr) {
		if(testStr == null || testStr.isEmpty())
			return true;
		return false;
	}
	
	public static boolean isEmpty(String testStr) {
		return isBlank(testStr);
	}
	
	public static int getLeasePadding(int maxNum) {
		int count = 0;
		int tmp = maxNum;
		while(tmp > 0) {
			count++;
			tmp /= 10;
		}
		return count;
	}
}
