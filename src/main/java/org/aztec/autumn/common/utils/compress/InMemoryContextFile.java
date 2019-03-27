package org.aztec.autumn.common.utils.compress;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;

import com.google.common.collect.Maps;

public class InMemoryContextFile implements CodeFile {
	
	
	private Map<String,Object> contextMap = Maps.newHashMap();
	
	public InMemoryContextFile() {
	}
	
	public void put(String key,Object contextObj) {
		contextMap.put(key, contextObj);
	}
	
	public void clear() {
		contextMap.clear();
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return null;
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
	public void parse(File file) throws CompressException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMain() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVirtual() {
		return true;
	}

	@Override
	public byte[] toBytes() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuffer getBuffer() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeUnit read() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(CodeUnit unit) throws CompressException {
		// TODO Auto-generated method stub

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
	public void merge(CodeFile codeFile) throws CompressException {
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
	public <T> T adapt() throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

}
