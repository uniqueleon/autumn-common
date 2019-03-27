package org.aztec.autumn.common.utils.compress.io.header;

import java.io.File;
import java.util.List;

import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.code.BaseCodeHeader;
import org.aztec.autumn.common.utils.compress.io.header.meta.AlgorithmInfo;
import org.aztec.autumn.common.utils.compress.io.header.meta.ByteDictionary;
import org.aztec.autumn.common.utils.compress.io.header.meta.FileLengthInfo;

import com.google.common.collect.Lists;

public class CongruenceCodeHeader extends BaseCodeHeader{

	private List<CodingMetaData> metaDatas = Lists.newArrayList();

	public CongruenceCodeHeader(CodingConfigure config,byte[] dictionary) throws CompressException {
		super();
		File sourceFile = config.getSourceFile();
		this.metaDatas.add(new AlgorithmInfo(config.getAlgorithmInfos()));
		this.metaDatas.add(new FileLengthInfo(sourceFile.length()));
		this.metaDatas.add(new ByteDictionary(dictionary));
		this.outputFile = config.getTargetFile();
	}

	@Override
	public List<CodingMetaData> getSectors() {
		
		return metaDatas;
	}

	@Override
	public String getName() {
		return "CongruenceCode";
	}

	
}
