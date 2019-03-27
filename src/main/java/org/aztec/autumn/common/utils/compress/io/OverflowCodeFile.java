package org.aztec.autumn.common.utils.compress.io;

import java.io.File;
import java.io.IOException;

import org.aztec.autumn.common.utils.compress.CodingConfigure;

public class OverflowCodeFile extends BaseCodeFile {

	public OverflowCodeFile(CodingConfigure config) throws IOException {
		super();
	}

	@Override
	public boolean isTemp() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getLabel() {
		return "overflowFile";
	}

	@Override
	protected File getTargetFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getSessionID() {
		// TODO Auto-generated method stub
		return null;
	}

}
