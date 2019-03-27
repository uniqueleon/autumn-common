package org.aztec.autumn.common.utils.security;

import java.util.ArrayList;
import java.util.List;

public class DefaultConverter implements CodeConverter {

	public final static int ASCII_CODE_A = 65;
	public final static int ASCII_CODE_a = 97;
	public final static int ASCII_CODE_0 = 48;
	public final static int ASCII_CODE_EXCLAMATION_MARK = 33;// !
	//public final static int ASCII_CODE_NUMBER_MARK = 35;// #
	public final static int ASCII_CODE_UNDERLINE_MARK = 95;//_
	public final static int ASCII_CODE_NEGATIVE_MARK = 45;// -

	// ASSIC:
	// rule : a-b,A-Z,0-9 (0-61) !a-!b,!A-!Z,!0-!9(62-123) _a-_d(124-127)
	// (62+62+4=128) nagative(+ '-')
	public String toString(byte[] ecode) {
		if (ecode == null)
			return null;
		StringBuilder readableString = new StringBuilder();
		for (int i = 0; i < ecode.length; i++) {
			int code = ecode[i];
			if (code < 0) {
				readableString.append((char) (ASCII_CODE_NEGATIVE_MARK));
				code = Math.abs(code) - 1;
			}
			if (code < 26) {
				readableString.append((char) (ASCII_CODE_a + code));
			} else if (code < 52) {
				readableString.append((char) (ASCII_CODE_A + code - 26));
			} else if (code < 62) {
				readableString.append((char) (ASCII_CODE_0 + code - 52));
			} else if (code < 88) {
				readableString.append((char) (ASCII_CODE_EXCLAMATION_MARK));
				readableString.append((char) (ASCII_CODE_a + (code - 62)));
			} else if (code < 114) {
				readableString.append((char) (ASCII_CODE_EXCLAMATION_MARK));
				readableString.append((char) (ASCII_CODE_A + code - 88));
			} else if (code < 124) {
				readableString.append((char) (ASCII_CODE_EXCLAMATION_MARK));
				readableString.append((char) (ASCII_CODE_0 + code - 114));
			} else {
				readableString.append((char) (ASCII_CODE_UNDERLINE_MARK));
				readableString.append((char) (ASCII_CODE_a + code - 124));
			}
		}
		return readableString.toString();
	}

	public byte[] toByteCode(String codeString) {
		if (codeString == null)
			return null;
		String temp = codeString;
		List<Byte> ecode = new ArrayList<Byte>();
		while (temp.length() > 0) {
			int base = 1;
			int offset = 0;
			if (temp.startsWith("-")) {
				base = -1;
				temp = temp.substring(1, temp.length());
			}
			if (temp.startsWith("!")) {
				//offset = getOffSet(base, (int)temp.charAt(1));
				ecode.add((byte) (base * (62 + (int) getRealCode(temp.charAt(1),base) + offset)));
				temp = temp.substring(2, temp.length());
			} else if (temp.startsWith("_")) {
				//offset = getOffSet(base, (int)temp.charAt(1));
				ecode.add((byte) (base * (124 + (int) getRealCode(temp
						.charAt(1),base) + offset)));
				temp = temp.substring(2, temp.length());
			} else {
				//offset = getOffSet(base, (int)temp.charAt(1));
				ecode.add((byte) (base * ((int) getRealCode(temp.charAt(0),base) + offset)));
				temp = temp.substring(1, temp.length());
			}
		}
		byte[] retByte = new byte[ecode.size()];
		for (int i = 0; i < ecode.size(); i++)
			retByte[i] = ecode.get(i);
		return retByte;
	}

	private byte getRealCode(int code,int base) {
		if (code >= ASCII_CODE_a && code < (ASCII_CODE_a + 26)) {
			return (byte) (code - ASCII_CODE_a + (base == -1 ? 1 : 0));
		} else if (code >= ASCII_CODE_A && code < (ASCII_CODE_A + 26)) {
			return (byte) (26 + code - ASCII_CODE_A + (base == -1 ? 1 : 0));
		} else if (code >= ASCII_CODE_0 && code < (ASCII_CODE_0 + 10)) {
			return (byte) (52 + code - ASCII_CODE_0 + (base == -1 ? 1 : 0));
		} else {
			throw new IllegalArgumentException("code " + ((char) code)
					+ " is not a acceptable char!");
		}
	}
}
