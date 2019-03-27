package org.aztec.autumn.common.utils.compress.base;

import java.io.File;

import org.aztec.autumn.common.math.congruence.CongruenceTable;
import org.aztec.autumn.common.math.congruence.CongruenceUnitGenerator;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.compress.BaseCoder;
import org.aztec.autumn.common.utils.compress.CodeFile;
import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingPhase;
import org.aztec.autumn.common.utils.compress.CodingProgress;
import org.aztec.autumn.common.utils.compress.CompressCoder;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.code.phase.ByteCountingPhase;
import org.aztec.autumn.common.utils.compress.io.NoCompressFile;


public class CongruenceCoder extends BaseCoder implements CompressCoder {

	private CodingPhase beginingPhase;
	private CodeFile rawFile;
	private CodeFile compressFile;
	
	
	public CongruenceCoder(CodingPhase beginingPhase) {
		this.beginingPhase = beginingPhase;
	}
	
	private CongruenceTable generateTable(CodingConfigure config) {
		CongruenceTable table = new CongruenceTable(CongruenceUnitGenerator.generate(config.getModulars(), config.getStatusRange()));
		if(config.getStatusRange() % 2 != 0)
			throw new IllegalArgumentException("status should be a even number");
		table.setSupplement((config.getStatusRange() / 2));
		return table;
	}
	
	
	/*public byte[] getCongruenceCode() {
		
	}*/
	
	

	@Override
	public CodingPhase getFirstPhase(CodingConfigure cofing) {
		// TODO Auto-generated method stub
		return beginingPhase;
	}


	@Override
	public CodingPhase getLastPhase(CodingConfigure config) {
		CodingPhase tmpPhase = beginingPhase;
		while(!tmpPhase.isLastPhase() && tmpPhase.nextPhase() != null) {
			tmpPhase = tmpPhase.nextPhase();
		}
		return tmpPhase;
	}


	@Override
	public CodeFile toCodeFile(File file) {
		rawFile = new NoCompressFile();
		try {
			rawFile.parse(file);
		} catch (CompressException e) {
			rawFile = null;
		}
		return rawFile;
	}

	@Override
	public CodingProgress getProgress(CodingConfigure config) {
		long fileLength = config.getSourceFile().length();
		CodingProgress progress = config.getProgress();
		if(progress == null) {
			progress = new BaseCodingProgress(beginingPhase, 0.0f, fileLength, fileLength * 100);
		}
		else {
			progress.setCurrentPhase(beginingPhase);
			progress.setCurrentRate(0.0f);
		}
		
		return progress;
	}
}
