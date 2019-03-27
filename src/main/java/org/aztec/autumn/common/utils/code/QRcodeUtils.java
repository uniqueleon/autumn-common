package org.aztec.autumn.common.utils.code;

import java.io.File;

public interface QRcodeUtils {

	public File generateQRCode(String text, int width, int height, String format, String filePath)
			throws QRcodeException;

	public File generateBarCode(String text, int width, int height, String format, String filePath)
			throws QRcodeException;

	public String decode(File file) throws QRcodeException;
}
