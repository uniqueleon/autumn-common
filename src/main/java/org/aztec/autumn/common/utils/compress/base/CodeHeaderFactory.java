package org.aztec.autumn.common.utils.compress.base;

import java.util.Queue;

import org.aztec.autumn.common.utils.compress.FileHeader;
import org.aztec.autumn.common.utils.compress.CodingConfigure;

import com.google.common.collect.Queues;

public class CodeHeaderFactory {

	
	
	public FileHeader buildChain(String codeFileType){
		return null;
	}
	
	private Queue<FileHeader> getPureCongruenceCodeHeader(){

		Queue<FileHeader> headerChain = Queues.newArrayBlockingQueue(1);
		//headerChain.add(new Congurence)
		return headerChain;
	}
}
