package org.aztec.autumn.common.utils.compress.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.aztec.autumn.common.utils.ByteBufferUtils;
import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.code.HuffmanCode;
import org.aztec.autumn.common.utils.compress.code.LZ77Code;
import org.aztec.autumn.common.utils.compress.code.LZ77Code.Header;
import org.aztec.autumn.common.utils.compress.code.LZ77CodeUnit;

import com.beust.jcommander.internal.Lists;

public class LZ77Coder {
	
	public LZ77Code extract(byte[] content,CodingConfigure configure) throws UnsupportedEncodingException, CharacterCodingException{
		 
		ByteBuffer dictBuffer = ByteBuffer.allocate(configure.getDictionarySize());
		ByteBuffer lookBuffer = ByteBuffer.allocate(configure.getLookaheadBufferSize());
		List<LZ77CodeUnit> histories = Lists.newArrayList();
		read(content, dictBuffer, lookBuffer,histories,configure);
		LZ77Code finalCode = new LZ77Code(new Header(content.length));
		finalCode.setCodes(histories);
		return finalCode;
	}
	
	
	public byte[] decode(LZ77Code lz77Code) {
		ByteBuffer buffer = ByteBuffer.allocate(lz77Code.getHeader().getTotalLength());
		List<LZ77CodeUnit> codes = lz77Code.getCodes();
		for(LZ77CodeUnit code : codes) {
			if(code.getOffset() == 0) {
				buffer.put(code.getAsByteArray());
			}
			else {
				int count = 0;
				int curPos = buffer.position();
				for(int i = code.getOffset();count < code.getMatchLength();i--) {
					buffer.put(buffer.get(curPos - i));
					count ++;
				}
			}
		}
		return buffer.array();
	}
	
	
	private List<LZ77CodeUnit> checkOutOfDictionary(byte[] content,ByteBuffer dictBuff,ByteBuffer lookBuff,
			List<LZ77CodeUnit> historyCodes,
			CodingConfigure configure) throws UnsupportedEncodingException, CharacterCodingException{
		//Integer cursor = dictBuff.capacity();
		Integer cursor = 0;
		ByteBuffer tmpBuffer = lookBuff.duplicate();
		while (cursor < content.length) {
			int readSize = content.length - cursor >= lookBuff.capacity() ? lookBuff.capacity() : content.length - cursor;
			for(int i = 0;i < readSize;i++) {
				lookBuff.put(content[cursor + i]);
			}
			Boolean similar = isSimilar(dictBuff, lookBuff);
			if(!similar) {
				//ByteBuffer tmpBuffer = ByteBufferUtils.clone(lookBuff);
				//ByteBuffer tmpBuffer = lookBuff;
				//ByteBufferUtils.copy(lookBuff, tmpBuffer);
				tmpBuffer.position(lookBuff.position());
				int index = -1;
				while(tmpBuffer.position() > configure.getLeastBufferSize()) {
					index = ByteBufferUtils.lastIndexOf(dictBuff, tmpBuffer);
					if(index != -1) {

						//historyCodes.add(new LZ77CodeUnit(tmpBuffer.array(),dictBuff.position() - index,tmpBuffer.position()));
						ByteBufferUtils.append(dictBuff, tmpBuffer);
						ByteBufferUtils.clean(lookBuff);
						cursor += tmpBuffer.position();
						break;
					}
					ByteBufferUtils.backward(tmpBuffer, 1);
				}
				if(index  == -1) {
					//historyCodes.add(new LZ77CodeUnit(new byte[] {content[cursor]},0,0));
					ByteBufferUtils.append(dictBuff, new byte[] {content[cursor]});
					cursor++;
					ByteBufferUtils.clean(lookBuff);
					continue;
				}
				tmpBuffer.clear();
				
			}
			else  {
				cursor += lookBuff.position();
				int index = ByteBufferUtils.lastIndexOf(dictBuff, lookBuff);
				historyCodes.add(new LZ77CodeUnit(lookBuff.array(),(dictBuff.position() - index),lookBuff.position()));
				ByteBufferUtils.append(dictBuff, lookBuff);
				ByteBufferUtils.clean(lookBuff);
			}
			System.out.println("cursor:" + cursor);
		}
		return historyCodes;
	}
	
	
	public List<LZ77CodeUnit> read(byte[] content,ByteBuffer dictBuff,ByteBuffer lookBuff,
			List<LZ77CodeUnit> historyCodes,
			CodingConfigure configure) throws UnsupportedEncodingException, CharacterCodingException {
		checkOutOfDictionary(content, dictBuff, lookBuff, historyCodes, configure);
		return historyCodes;
	}
	
	
	
	public boolean isSimilar(ByteBuffer dictBuff,ByteBuffer lookBuff) throws UnsupportedEncodingException, CharacterCodingException {
		return ByteBufferUtils.contains(dictBuff, lookBuff) ;
	}
	
	
	public LZ77CodeUnit findMatchCode(List<LZ77CodeUnit> histories,LZ77CodeUnit newCode) {
		for(LZ77CodeUnit oldCode : histories) {
			if(oldCode.equals(newCode)) {
				return oldCode;
			}
		}
		return null;
	}
	
	public Boolean isSimilar(List<LZ77CodeUnit> histories,LZ77CodeUnit newCode) {
		for(LZ77CodeUnit oldCode : histories) {
			if(oldCode.isContains(newCode)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		/*LZ77Code newCode = new LZ77Code(new byte[] {11}, 0l);
		LZ77Code thisCode = new LZ77Code(new byte[] {3}, 0l);
		System.out.println(newCode.isContains(thisCode));*/
		String testText = "nadfasdfadfawnonwoffuck!aabbbcccaabbabacbaacabcbacbac";
		//String testText = "aabbbcccabbabacbaacabcbacbac";
		try {
			//byte[] testByte = FileUtils.readFileToByteArray(new File("test/test1.pdf"));
			byte[] testByte = testText.getBytes("UTF-8");
			CodingConfigure configure = new CodingConfigure(20,5,"UTF-8","");
			configure.setCharset("UTF-8");
			
			LZ77Coder extracter = new LZ77Coder();
			System.out.println(testText);
			LZ77Code lz77Codes = extracter.extract(testByte, configure);
			System.out.println(lz77Codes);
			//String decodeStr = new String(extracter.decode(lz77Codes),"UTF-8");
			//System.out.println(new String(decodeStr));
			int count = 0;
			/*for (LZ77CodeUnit lz77Code : lz77Codes.getCodes()) {
				System.out.println(count + "_" + lz77Code.getAsString() + ":" + lz77Code.getOffset() + "-"
						+ lz77Code.getMatchLength());
				count++;
			}*/
			List<HuffmanCode> huffCodes = lz77Codes.toHuffmanCodes(256);
			for(HuffmanCode hCode : huffCodes) {
				System.out.println(hCode.getOldCode().getAsString() + "-" + hCode.getOldCode().getFrequence());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
