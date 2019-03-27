package org.aztec.autumn.common.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glassfish.grizzly.http.util.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeCipher {

	Logger logger = LoggerFactory.getLogger(CodeCipher.class);
	public static final String ALGORITHM_AES = "AES";
	public static final String ALGORITHM_BASE_64 = "BASE_64";
	private static final String AES_ALGORITHM = "AES/CBC/PKCS7Padding";
	public static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	// public static final String AES_ALGORITHM = "AES";

	private static final String IV_STRING = "baybaybearXX.com";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public CodeCipher() {
		// TODO Auto-generated constructor stub
	}

	public String getMD5Substract(String content, String charset)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] contentByte = content.getBytes(charset);
		byte[] buffered = digest.digest(contentByte);
		// digest.update(buffered);
		int j = buffered.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = buffered[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
		// return encodeBase64(new String(buffered));
		// return StringUtils.toHexString(buffered);
	}

	public String encrypt(String algorithm, String content, String... secretKey) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportAlgorithmException, InvalidAlgorithmParameterException {
		String encryptString = null;
		byte[] ecryptBytes = null;
		if (algorithm.equals(ALGORITHM_AES)) {
			ecryptBytes = encryptAES(content, secretKey[0]);
			CodeConverter converter = CodeConverterFactory.converter();
			return converter.toString(ecryptBytes);
		} else if (algorithm.equals(ALGORITHM_BASE_64)) {
			return decodeBase64(content);
		} else {
			throw new UnsupportAlgorithmException("unsupport algorithm:" + algorithm);
		}
	}

	public byte[] decrypt(String algorithm, String content, String... secretKey) throws UnsupportedEncodingException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportAlgorithmException, InvalidAlgorithmParameterException {
		if (algorithm.equals(ALGORITHM_AES)) {
			CodeConverter converter = CodeConverterFactory.converter();
			byte[] ecode = converter.toByteCode(content);
			return decryptAES(ecode, secretKey[0]);
		} else if (algorithm.equals(ALGORITHM_BASE_64)) {
			return decodeBase64(content).getBytes("UTF-8");
		} else {
			throw new UnsupportAlgorithmException("unsupport algorithm:" + algorithm);
		}
	}

	private byte[] encryptAES(String content, String secret)
			throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		/*
		 * javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator
		 * .getInstance("AES"); SecureRandom secureRandom =
		 * SecureRandom.getInstance("SHA1PRNG" );
		 * secureRandom.setSeed(secret.getBytes()); kgen.init(128, secureRandom);
		 * SecretKey secretKey = kgen.generateKey(); byte[] enCodeFormat =
		 * secretKey.getEncoded();
		 */

		byte[] enCodeFormat = secret.getBytes();// secretKey.getEncoded();
		SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES_ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

		byte[] byteContent = content.getBytes("utf-8");
		byte[] initParam = IV_STRING.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initParam));
		// cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(byteContent);
		return result;
	}

	private byte[] decryptAES(byte[] content, String secrectKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		/*
		 * javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator
		 * .getInstance("AES"); SecureRandom secureRandom =
		 * SecureRandom.getInstance("SHA1PRNG" );
		 * secureRandom.setSeed(secrectKey.getBytes()); kgen.init(128, secureRandom);
		 * SecretKey secretKey = kgen.generateKey(); byte[] enCodeFormat =
		 * secretKey.getEncoded();
		 */
		byte[] enCodeFormat = secrectKey.getBytes();
		// SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");

		byte[] initParam = IV_STRING.getBytes();
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
		// SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES_ALGORITHM);
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		// Cipher cipher = Cipher.getInstance("AES");
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
		// cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(content);
		return result;
	}
	
	

	public static String encodeBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new Base64Utils().encodeToString(b, true);
		}
		return s;
	}

	public static String decodeBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			Base64Utils decoder = new Base64Utils();
			try {
				b = decoder.decode(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static String SHA(final byte[] rawBytes, final String strType) {
		// 返回值
		String strResult = null;

		// 是否是有效字符串
		if (rawBytes.length > 0) {
			try {
				// SHA 加密开始
				// 创建加密对象 并傳入加密類型
				MessageDigest messageDigest = MessageDigest.getInstance(strType);
				// 传入要加密的字符串
				messageDigest.update(rawBytes);
				// 得到 byte 類型结果
				byte byteBuffer[] = messageDigest.digest();

				// 將 byte 轉換爲 string
				StringBuffer strHexString = new StringBuffer();
				// 遍歷 byte buffer
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回結果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return strResult;
	}
	
	
	/**
	 * 传入文本内容，返回 SHA-256 串
	 * 
	 * @param strText
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String SHA256(final String strText) throws UnsupportedEncodingException {
		return SHA(strText.getBytes(GlobalConst.DEFAULT_CHARSET), "SHA-256");
	}

	/**
	 * 传入文本内容，返回 SHA-512 串
	 * 
	 * @param strText
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String SHA512(final String strText) throws UnsupportedEncodingException {
		return SHA(strText.getBytes(GlobalConst.DEFAULT_CHARSET), "SHA-512");
	}
}
