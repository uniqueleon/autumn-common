package org.aztec.autumn.common.utils.compress;

import java.io.File;
import java.nio.ByteBuffer;

public interface CodeFile {

	public File getFile();
	public FileHeader getHeader();
	public long getStartPoint();
	public void parse(File file) throws CompressException;
	public boolean isMain();
	public boolean isVirtual();
	public byte[] toBytes() throws CompressException;
	public ByteBuffer getBuffer() throws CompressException;
	public CodeUnit read() throws CompressException;
	public void write(CodeUnit unit) throws CompressException;
	public void write(byte[] bytes) throws CompressException;
	public Long position() throws CompressException;
	public void setPosition(Long position) throws CompressException;
	public void merge(CodeFile codeFile) throws CompressException; 
	public void flush() throws CompressException;
	public void close() throws CompressException;
	public <T> T adapt() throws CompressException;
}
