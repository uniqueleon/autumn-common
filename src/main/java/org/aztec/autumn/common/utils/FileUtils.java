package org.aztec.autumn.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtils {

	private FileUtils() {
		// TODO Auto-generated constructor stub
	}

	public static List<File> listLocalFiles(String fileDir, String fileNamePattern, boolean isRecursive)
			throws FileNotFoundException {
		File targetDir = new File(fileDir);
		if (!targetDir.exists())
			throw new FileNotFoundException("The file[path=" + fileDir + "] is not found!");
		if (!targetDir.isDirectory())
			throw new IllegalArgumentException("The file[path=" + fileDir + "] is not a directory!");

		List<File> retFiles = new ArrayList<File>();
		for (File subFile : targetDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				Pattern pattern = Pattern.compile(fileNamePattern);

				return pattern.matcher(name).find();
			}
		})) {
			if (subFile.isFile())
				retFiles.add(subFile);
			if (isRecursive && subFile.isDirectory())
				retFiles.addAll(listLocalFiles(fileDir, fileNamePattern, isRecursive));
		}
		return retFiles;
	}
	

	public static File rename(File sourceFile,String newName) throws IOException{
		String parent = sourceFile.getParent();
		String newPath = (parent != null ? parent + "/" : "") + newName;
		File dest = new File(newPath);
		if(!sourceFile.renameTo(dest)){
			IOUtils.copyFile(sourceFile, dest);
			sourceFile.delete();
		}
		return dest;
	}
	
	public static void cleanUp(File dir,boolean recursive,boolean delete){
		if(!dir.exists() || !dir.isDirectory())
			throw new IllegalArgumentException("The file[" + dir.getAbsolutePath()  + "] is not exists or not a directory!");
		for(File subFile : dir.listFiles()){
			if(subFile.isFile())
				subFile.delete();
			else if(subFile.isDirectory()){
				if(recursive)
					cleanUp(subFile, recursive,true);
			}
		}
		if(delete)dir.delete();
	}
	
	public static String readFileAsString(File targetFile) throws IOException{
		if(!targetFile.exists() || targetFile.isDirectory())
			throw new IllegalArgumentException("The target file must be readable file!");
		FileInputStream fis = new FileInputStream(targetFile);
		long availableBytes = SystemUtils.getAvailableMemory();
		Integer maxAcceptableSize = Integer.parseInt("" + availableBytes) / 10;
		if(fis.available() > maxAcceptableSize){
			throw new IllegalArgumentException("The target file is too large!");
		}
		byte[] bytebuffered = new byte[fis.available()];
		fis.read(bytebuffered);
		return new String(bytebuffered,"UTF-8");
		//if(fis.available() > GlobalConst.MAX_BUFFER_SIZE)
	}
	
	public static void writeFile(File targetFile,byte[] writeBytes)   throws IOException{
		FileOutputStream fos = new FileOutputStream(targetFile);
		fos.write(writeBytes);
		fos.flush();
		fos.close();
		
	}
	
	public static ByteBuffer readFile(File dataFile,long startPosition,int readSize,ByteBuffer buffer) throws IOException {
		if(dataFile == null || !dataFile.exists()) {
			throw new IllegalArgumentException("The data file is not exists!");
		}
		if(buffer == null) {
			buffer = ByteBuffer.allocate(readSize);
		}
		buffer.clear();
		RandomAccessFile raf = new RandomAccessFile(dataFile, "r");
		FileChannel channel = raf.getChannel();
		channel.position(startPosition);
		channel.read(buffer);
		raf.close();
		return buffer;
	}
	
}
