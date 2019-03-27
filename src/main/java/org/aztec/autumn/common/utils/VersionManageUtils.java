package org.aztec.autumn.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionManageUtils {

	private static final String STANDARD_VERSION_REGEX = "\\d+";
	private static final Pattern DEFAULT_VERSION_PATTERN = Pattern.compile(STANDARD_VERSION_REGEX);

	public VersionManageUtils() {
		// TODO Auto-generated constructor stub
	}

	private static void checkFormat(String text) {
		if (text == null || text.isEmpty())
			throw new IllegalArgumentException("the version string is null or empty");
		if (!DEFAULT_VERSION_PATTERN.matcher(text).find())
			throw new IllegalArgumentException(
					"the version string is not illegal!The acceptable version format is : XXX.XXX.XXX");
	}

	public static int getMajorVersion(String version) {
		checkFormat(version);
		return Integer.parseInt(version.split("\\.")[0]);
	}

	public static int getMinorVersion(String version) {
		checkFormat(version);
		return Integer.parseInt(version.split("\\.")[1]);
	}

	public static int getMiniVersion(String version) {
		checkFormat(version);
		return Integer.parseInt(version.split("\\.")[2]);
	}

	public static String trim(String version) {
		checkFormat(version);
		return getMajorVersion(version) + "." + getMinorVersion(version) + "." + getMiniVersion(version);
	}

	public static String changeToStandardForm(String version, int padding) {
		checkFormat(version);
		return padding(getMajorVersion(version), padding) + "." + padding(getMinorVersion(version), padding) + "."
				+ padding(getMiniVersion(version), padding);
	}

	private static String padding(int version, int padding) {
		StringBuilder paddings = new StringBuilder();
		int paddingChars = (padding - new String("" + version).length());
		if (paddingChars > 0) {
			for (int i = 0; i < paddingChars; i++)
				paddings.append("0");
		}
		return paddings.toString() + version;
	}

	public static void main(String[] args) {
		String textString = "000.000.001";
		System.out.println(trim("000.000.001"));
		System.out.println(changeToStandardForm("1.0.0", 3));
		System.out.println(changeToStandardForm("10000.20000.30000", 3));
	}
}
