package org.aztec.autumn.common.utils.compress;

import java.util.List;

public interface CodingPhase {

	public List<CodeFile> encode(CodingConfigure config,List<CodeFile> input,CodingProgress progress) throws CompressException;
	public List<CodeFile> decode(CodingConfigure config,List<CodeFile> input,CodingProgress progress) throws CompressException;
	public CodingPhase nextPhase();
	public CodingPhase previousPhase();
	public String getName();
	public boolean isLastPhase();
	public boolean isFirstPhase();
}
