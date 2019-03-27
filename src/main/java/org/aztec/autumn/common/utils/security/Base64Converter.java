package org.aztec.autumn.common.utils.security;

import org.glassfish.grizzly.http.util.Base64Utils;

public class Base64Converter implements CodeConverter{

	public Base64Converter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString(byte[] ecode) {

    	Base64Utils decoder = new Base64Utils(); 
		return decoder.encodeToString(ecode,false);
	}

	@Override
	public byte[] toByteCode(String codeString) {
		// TODO Auto-generated method stub
    	Base64Utils decoder = new Base64Utils(); 
		return decoder.decode(codeString);
	}

}
