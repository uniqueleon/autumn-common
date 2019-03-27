package org.aztec.autumn.common.utils.compress.code.phase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.compress.CodeFile;
import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingPhase;
import org.aztec.autumn.common.utils.compress.CodingProgress;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.base.HuffmanCoder;
import org.aztec.autumn.common.utils.compress.code.HuffmanCode;
import org.aztec.autumn.common.utils.compress.io.CongruenceCodeFile;
import org.aztec.autumn.common.utils.compress.io.CongruenceSortTableFile;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

public class HuffmanCodingPhase implements CodingPhase {
	
	
	private static final int MAX_WRITE_COUNT = 8;

	public HuffmanCodingPhase() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<CodeFile> encode(CodingConfigure config, List<CodeFile> input, CodingProgress progress)
			throws CompressException {
		
		try {
			CongruenceSortTableFile sortFile = input.get(1).adapt();
			HuffmanCoder coder = new HuffmanCoder();
			List<HuffmanCode> codes = Lists.newArrayList();
			List<Long> frequences = sortFile.getFrequences();
			for(int i = 0;i < frequences.size();i++) {
				codes.add(new HuffmanCode(i, frequences.get(i)));
			}
			List<HuffmanCode> newCodes = coder.transfer(codes);
			Long targetLength = 0l;
			Long frequeceTotal = 0l;
			for(HuffmanCode newCode : newCodes) {
				targetLength += newCode.getOldCode().getFrequence() * newCode.getNewCode().getLength();
				frequeceTotal += newCode.getOldCode().getFrequence();
			}
			if(frequeceTotal != sortFile.getLength()) {
				System.out.println("fre :" + frequeceTotal + " <> length:" + sortFile.getLength() );
				System.exit(-1);
			}
			Long realLength = sortFile.getLength() * 8l;
			System.out.println(realLength +  ":" + targetLength);
			System.out.println("reduce:" + (realLength - targetLength));
			List<CodeFile> files = Lists.newArrayList();
			files.add(encode(toMap(newCodes), sortFile, config.getSourceFile(), config));
			return files;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private Map<Byte,HuffmanCode> toMap(List<HuffmanCode> codes) throws UnsupportedEncodingException{
		Map<Byte,HuffmanCode> codeMaps = Maps.newHashMap();
		for(HuffmanCode code : codes) {
			codeMaps.put(new Byte(code.getOldCode().getAsByteArray()[0]), code);
		}
		return codeMaps;
	}
	
	
	private CongruenceCodeFile encode(Map<Byte,HuffmanCode> dictionary, CongruenceSortTableFile cstf,
			File dataFile,CodingConfigure config) throws IOException, CompressException {
		CongruenceCodeFile ccf = new CongruenceCodeFile(config,
				cstf.getHeader().getSectors().get(0).getContent());
		Integer writeCount = 0, cursor = 0;
		RandomAccessFile rFile = new RandomAccessFile(dataFile,"r");
		FileChannel fc = rFile.getChannel();
		fc.position(cursor);
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
		BitSet tmpBs = BitSet.valueOf(new byte[0]);
		Map<String,Object> workingContext = Maps.newHashMap();
		while(fc.read(buffer) != -1) {
			buffer.flip();
			writeCount ++;
			System.out.println("write:" + writeCount + " mb!");
			while(buffer.hasRemaining()) {
				byte readByte = buffer.get();
				int unsignedInt = (int) readByte;
				
				byte[] splitBytes = cstf.splitBytes(buffer.get());
				HuffmanCode code1 = dictionary.get(new Byte(splitBytes[0]));
				HuffmanCode code2 = dictionary.get(new Byte(splitBytes[1]));
				BitSet bs1 = code1.getNewCode().getCode();
				BitSet bs2 = code2.getNewCode().getCode();
				cursor = writeBitSet(tmpBs, bs1, code1.getNewCode().getLength(),cursor,ccf);
				cursor = writeBitSet(tmpBs, bs2, code2.getNewCode().getLength(), cursor,ccf);
			}
			buffer.clear();
		}
		if(!tmpBs.isEmpty()) {
			ccf.write(tmpBs.toByteArray());
		}
		ccf.flush();
		rFile.close();
		return ccf;
	}
	
	
	private int writeBitSet(BitSet workingSet ,BitSet targetSet,long codeLength,
			int cursor,CongruenceCodeFile ccf) 
			throws CompressException {
		int nextIndex = targetSet.nextSetBit(0);
		if(codeLength < 4) {
			System.out.println("good");
		}
		else if(codeLength > 4){

			System.out.println("suck!");
		}
		while(nextIndex != -1) {
			workingSet.set(cursor + nextIndex);
			nextIndex = targetSet.nextSetBit(nextIndex + 1);
		}
		cursor += codeLength;
		if(cursor % (8 * MAX_WRITE_COUNT )== 0) {
			ccf.write(workingSet.toByteArray());
			workingSet.clear();
			cursor = 0;
		}
		return cursor;
	}
	

	@Override
	public List<CodeFile> decode(CodingConfigure config, List<CodeFile> input, CodingProgress progress)
			throws CompressException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingPhase nextPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingPhase previousPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLastPhase() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFirstPhase() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
