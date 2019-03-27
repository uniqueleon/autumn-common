package org.aztec.autumn.common.utils.compress.io;

import java.io.File;
import java.io.IOException;

public class MultipleCodeFile extends BaseCodeFile {

	public MultipleCodeFile() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTemp() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "multipleFile";
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
