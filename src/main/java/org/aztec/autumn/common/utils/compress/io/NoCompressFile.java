package org.aztec.autumn.common.utils.compress.io;

import java.io.File;
import java.nio.ByteBuffer;

import org.aztec.autumn.common.utils.compress.CodeFile;
import org.aztec.autumn.common.utils.compress.FileHeader;
import org.aztec.autumn.common.utils.compress.CodeUnit;
import org.aztec.autumn.common.utils.compress.CompressException;

public class NoCompressFile implements CodeFile {
	
	public File file;

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return file;
	}

	@Override
	public FileHeader getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getStartPoint() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void parse(File file) {
		// TODO Auto-generated method stub
		this.file = file;
	}


	@Override
	public boolean isMain() {
		return false;
	}


	@Override
	public byte[] toBytes() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(byte[] bytes) throws CompressException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long position() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPosition(Long position) throws CompressException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() throws CompressException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws CompressException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(CodeFile codeFile) throws CompressException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isVirtual() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ByteBuffer getBuffer() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(CodeUnit unit) throws CompressException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CodeUnit read() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T adapt() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

}
