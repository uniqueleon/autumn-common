package org.aztec.autumn.common.utils.cache.impl.fast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

import org.aztec.autumn.common.utils.SerializationUtils;

import com.google.common.collect.Maps;

public class FileCache {
	
	File cacheFile;
	RandomAccessFile raf;
	Map<Long,Long> dataIndexs = Maps.newHashMap();
	private Integer dataLength;

	public FileCache(File cacheFile) throws IOException{
		if(!cacheFile.exists())
			cacheFile.createNewFile();
		this.cacheFile = cacheFile;
		raf = new RandomAccessFile(cacheFile, "rw");
	}
	
	public void put(Long key,Object object) throws IOException{
		//raf = new RandomAccessFile(cacheFile, "rw");
		FileChannel channel = raf.getChannel();
		Long curPos = dataIndexs.get(key);
		if(curPos == null){
			curPos = channel.position();
			dataIndexs.put(key, curPos);	
		}

		channel.position(curPos);
		byte[] bytes = SerializationUtils.serialize(object);
		dataLength = bytes.length;
		channel.write(ByteBuffer.wrap(SerializationUtils.serialize(object)));
		//if(channel.position())
		/*channel.close();
		raf.close();*/
	}
	
	public Object get(Long key) throws IOException, ClassNotFoundException{
		//raf = new RandomAccessFile(cacheFile, "rw");
		FileChannel channel = raf.getChannel();
		//Object object = 
		Long curPos = dataIndexs.get(key);
		if(dataLength == null)
			return null;
		channel.position(curPos);
		ByteBuffer buffer = ByteBuffer.allocate(dataLength);
		channel.read(buffer, curPos);
		Object dataObj = SerializationUtils.deserialize(buffer.array());
		/*channel.close();
		raf.close();*/
		return dataObj;
	}
	
	public void close() throws IOException{
		raf.close();
	}
	
}
