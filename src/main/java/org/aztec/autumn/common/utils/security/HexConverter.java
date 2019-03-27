package org.aztec.autumn.common.utils.security;

import org.aztec.autumn.common.utils.StringUtils;

public class HexConverter implements CodeConverter {

	public HexConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString(byte[] ecode) {
		// TODO Auto-generated method stub
		return StringUtils.toHexString(ecode);
		//String hexString = StringUtils.toHexString(ecode);
		//return CodeCipher.
	}

	@Override
	public byte[] toByteCode(String codeString) {
		// TODO Auto-generated method stub
		return StringUtils.hexToBytes(codeString);
	}

}
