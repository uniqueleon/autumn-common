package org.aztec.autumn.common.utils.bytes;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.aztec.autumn.common.utils.FileUtils;
import org.aztec.autumn.common.utils.bytes.impl.DefaultByteCounter;
import org.aztec.autumn.common.utils.bytes.impl.NeighborChecker;

public class ByteDataAnalyzer {

	public ByteDataAnalyzer() {
		// TODO Auto-generated constructor stub
	}

	public static ByteStatisticsResult count(ByteAnalyzeConfig config, ByteCounter analyzer) throws IOException {
		File dataFile = config.getDataFile();
		if (dataFile == null || !dataFile.exists()) {
			throw new IllegalArgumentException("The data file is not exists!");
		}
		long startPosition = 0l;
		ByteStatisticsResult result = new ByteStatisticsResult();
		ByteBuffer buffer = FileUtils.readFile(dataFile, startPosition, config.getBufferSize(), null);
		while (startPosition + buffer.position() <= dataFile.length() && buffer.position() > 0) {
			long cursor = startPosition;
			int readSize = buffer.position();
			buffer.flip();
			if (buffer.hasArray()) {
				byte[] byteData = buffer.array();
				for (int i = 0; i < readSize; i++) {
					AnalyzerConfig aConfig = new AnalyzerConfig(result, i, byteData, config.getBackward(),
							config.getForward());
					analyzer.count(aConfig);
				}
			}
			buffer.clear();
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			ByteAnalyzeConfig bac = new ByteAnalyzeConfig();
			bac.setDataFile(new File("test/compress/jellyfish_2.dat"));
			ByteStatisticsResult bsr = count(bac, new DefaultByteCounter());
			// System.out.println(bsr);
			NeighborChecker checker = new NeighborChecker();
			System.out.println(checker.isSatisfied(bsr));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static ByteCounting
}
