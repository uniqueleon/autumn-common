package org.aztec.autumn.common.utils.compress;

import java.io.File;
import java.util.List;

import org.aztec.autumn.common.utils.compress.CompressException.ErrorCode;

import com.google.common.collect.Lists;


public abstract class BaseCoder implements CompressCoder {
	
	public abstract CodingPhase getFirstPhase(CodingConfigure cofing);
	public abstract CodingPhase getLastPhase(CodingConfigure config);
	public abstract CodeFile toCodeFile(File file);
	public abstract CodingProgress getProgress(CodingConfigure config);

	@Override
	public File encode(CodingConfigure config) throws CompressException {
		CodingPhase phase = getFirstPhase(config);
		List<CodeFile> codeFiles = Lists.newArrayList();
		CodingProgress progress = getProgress(config);
		codeFiles.add(toCodeFile(config.getSourceFile()));
		while(phase != null && !phase.isLastPhase()) {
			codeFiles = phase.encode(config, codeFiles,progress);
			phase = phase.nextPhase();
		}
		CodeFile mainFile = mergeFile(codeFiles);
		return mainFile != null ? mainFile.getFile() : null;
	}
	
	private CodeFile mergeFile(List<CodeFile> codeFiles) throws CompressException {
		if(codeFiles == null)
			throw new CompressException(ErrorCode.RESULT_NOT_FOUND);
		CodeFile mainFile = null;
		for(CodeFile file : codeFiles) {
			if(file.isMain()) {
				mainFile = file;
			}
		}
		if(mainFile == null)
			throw new CompressException(ErrorCode.MAIN_FILE_NOT_FOUND);
		
		return mainFile;
	}

	@Override
	public File decode(CodingConfigure config) throws CompressException {
		CodingPhase phase = getLastPhase(config);
		List<CodeFile> codeFiles = Lists.newArrayList();
		codeFiles.add(toCodeFile(config.getSourceFile()));
		CodingProgress progress = getProgress(config);
		while(phase != null && !phase.isFirstPhase()) {
			codeFiles = phase.decode(config, codeFiles,progress);
			phase = phase.previousPhase();
		}
		CodeFile mainFile = mergeFile(codeFiles);
		return mainFile != null ? mainFile.getFile() : null;
	}

}
