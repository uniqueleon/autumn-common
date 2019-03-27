package org.aztec.autumn.common.utils.compress.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import org.aztec.autumn.common.utils.compress.CodeFile;
import org.aztec.autumn.common.utils.compress.CodeUnit;
import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressConstant;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.CompressException.ErrorCode;
import org.aztec.autumn.common.utils.compress.FileHeader;
import org.aztec.autumn.common.utils.compress.code.BaseCodeHeader;

import com.google.common.collect.Lists;

public abstract class BaseCodeFile implements CodeFile{

	protected FileHeader header;
	protected ByteBuffer buffer;
	protected File file ;
	protected RandomAccessFile raf;
	protected FileChannel fileChannel;
	protected boolean virtual = false;
	public FileHeader getHeader() {
		return header;
	}
	public void CodeHeader(FileHeader header) {
		this.header = header;
	}
	public byte[] getCodes() {
		return buffer.array();
	}
	public void setCodes(byte[] codes) {
		this.buffer = ByteBuffer.wrap(codes);
	}
	
	@Override
	public CodeUnit read() throws CompressException {
		
		return null;
	}
	@Override
	public void write(CodeUnit unit) throws CompressException {
		buffer.put(unit.toBytes());
		
	}
	public ByteBuffer getBuffer() {
		return buffer;
	}
	public BaseCodeFile(FileHeader header, byte[] codes) throws IOException {
		super();
		this.header = header;
		this.buffer = ByteBuffer.wrap(codes);
		this.virtual = true;
	
	}
	
	public BaseCodeFile(FileHeader header, int capacity) throws IOException {
		super();
		this.header = header;
		this.buffer = ByteBuffer.allocate(capacity);
		this.virtual = true;
	
	}
	
	public BaseCodeFile() throws IOException {
		super();
	}
	
	protected abstract File getTargetFile();
	
	protected abstract String getSessionID();
	
	protected void init() throws IOException {
		if(!isVirtual()) {
			if(isTemp()) {
				file = File.createTempFile(CompressConstant.TEMP_FILE_PREFIX.replace("{lable}", getLabel()), getSessionID());
			}
			else {
				file = getTargetFile();
			}
			raf = new RandomAccessFile(file, "rw");
			fileChannel = raf.getChannel();
		}
	}
	
	public boolean isVirtual() {
		return virtual;
	}
	
	public BaseCodeFile(byte[] codes,CodingConfigure config) throws IOException {
		super();
		this.header = new EmptyHeader();
		this.buffer = ByteBuffer.wrap(codes);
		init();
	}
	
	protected File persist() throws IOException, CompressException {
		
		
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(toBytes());
		fos.flush();
		fos.close();
		return file;
	}
	
	
	public abstract boolean isTemp();
	public abstract String getLabel();
	
	
	public static class EmptyHeader implements FileHeader{


		@Override
		public List<CodingMetaData> getSectors() {
			// TODO Auto-generated method stub
			return Lists.newArrayList();
		}

		@Override
		public Integer getLength() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "empty";
		}

		@Override
		public void parse(File file) throws CompressException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void parse(byte[] bytes) throws CompressException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean hasWritten() {
			return false;
		}

		@Override
		public void write() {
			// TODO Auto-generated method stub
			
		}

		
	}


	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return file;
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
	public byte[] toBytes() throws CompressException {
		// TODO Auto-generated method stub
		if(isVirtual()) {
			return buffer.array();
		}
		else {
			throw new CompressException(CompressException.ErrorCode.UNSUPPORTED_OPERATION);
		}
	}
	@Override
	public void write(byte[] bytes) throws CompressException {
		try {
			if(isVirtual()) {
				if(buffer.remaining() < bytes.length) {
					flush();
					buffer.clear();
				}
				buffer.put(bytes);
			}
			else {
				if(!header.hasWritten()) {
					header.write();
				}
				fileChannel.write(ByteBuffer.wrap(bytes));
			}
		} catch (IOException e) {
			throw new CompressException(ErrorCode.CODE_FILE_OPERATE_ERROR,e.getMessage(),e);
		}
	}
	@Override
	public Long position() throws CompressException {
		// TODO Auto-generated method stub
		try {
			if(isVirtual()) {
				return (long) buffer.position();
			}
			else {
				return fileChannel.position();
			}
		} catch (IOException e) {
			throw new CompressException(ErrorCode.CODE_FILE_OPERATE_ERROR,e.getMessage(),e);
		}
	}
	@Override
	public void setPosition(Long position) throws CompressException {
		try {
			if(isVirtual()) {
				buffer.position(position.intValue());
			}
			else {
				fileChannel.position(position);
			}
		} catch (IOException e) {
			throw new CompressException(ErrorCode.CODE_FILE_OPERATE_ERROR,e.getMessage(),e);
		}
	}
	@Override
	public void flush() throws CompressException {
		try {
			if(isVirtual()) {
				fileChannel.force(false);
			}
		} catch (IOException e) {
			throw new CompressException(ErrorCode.CODE_FILE_OPERATE_ERROR,e.getMessage(),e);
		}
	}
	@Override
	public void close() throws CompressException {
		// TODO Auto-generated method stub
		try {
			fileChannel.close();
			if(isTemp()) {
				file.delete();
			}
			else {
				buffer.clear();
			}
		} catch (IOException e) {
			throw new CompressException(ErrorCode.CODE_FILE_OPERATE_ERROR,e.getMessage(),e);
		}
	}
	@Override
	public void merge(CodeFile codeFile) throws CompressException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public <T> T adapt() throws CompressException {
		return (T) this;
	}
	
}
