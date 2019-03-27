package org.aztec.autumn.common.utils.compress.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.io.header.CongruenceCodeHeader;


public class CongruenceCodeFile extends BaseCodeFile{
	
	private File outputFile;
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	public CongruenceCodeFile(CodingConfigure config,byte[] dictionary) 
			throws IOException, CompressException {
		super(new CongruenceCodeHeader(config, dictionary),DEFAULT_BUFFER_SIZE);
		this.outputFile = config.getTargetFile();
		if(this.outputFile.exists()) {
			outputFile.createNewFile();
		}
		this.virtual = false;
		super.init();
	}

	@Override
	public boolean isMain() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isTemp() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "congruenceCodeFile";
	}

	@Override
	protected File getTargetFile() {
		return outputFile;
	}

	@Override
	protected String getSessionID() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
