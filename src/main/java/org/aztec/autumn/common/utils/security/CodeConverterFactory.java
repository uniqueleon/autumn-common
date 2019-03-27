package org.aztec.autumn.common.utils.security;

public class CodeConverterFactory {

	public static CodeConverter converter(){
		//return new HexConverter();
		//return new DefaultConverter();
		return new Base64Converter();
	}
}
