package org.aztec.autumn.common.utils.security;

interface CodeConverter {
	public String toString(byte[] ecode);
	public byte[] toByteCode(String codeString);
}
