package org.aztec.autumn.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


public class CompressUtils {

	public CompressUtils() {
		// TODO Auto-generated constructor stub
	}

	public static File extractZipByJdk(ZipFile zipFile, String extractPath) throws ZipException, IOException {
		
		File extractDir = new File(extractPath);
		if (extractDir.isFile())
			throw new IllegalArgumentException("This path[" + extractPath + "] pointing to a file!");
		if (!extractDir.exists())
			extractDir.mkdir();
		Enumeration<? extends java.util.zip.ZipEntry> enumer = zipFile.entries();
		while (enumer.hasMoreElements()) {
			java.util.zip.ZipEntry entry = enumer.nextElement();
			String name = entry.getName();
			String newFilePath = extractDir.getAbsolutePath() + "/" + name;
			File newFile = new File(newFilePath);
			if (name.endsWith("/") || name.endsWith("\\") ) {
				if (!newFile.exists())
					newFile.mkdirs();
			} else {
				if (newFile.getParentFile() == null || !newFile.getParentFile().exists()) {
					File parentDir = new File(newFile.getParent());
					parentDir.mkdirs();
				}
				newFile.createNewFile();
				InputStream is = zipFile.getInputStream(entry); 
				IOUtils.copyFromStream(is,newFile, is.available());
			}

		}
		return extractDir;
	}
	
	public static File extractZipByAnt(File targetFile, String extractPath) throws ZipException, IOException {
		org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(targetFile);
		File extractDir = new File(extractPath);
		if (extractDir.isFile())
			throw new IllegalArgumentException("This path[" + extractPath + "] pointing to a file!");
		if (!extractDir.exists())
			extractDir.mkdir();
		Enumeration<? extends org.apache.tools.zip.ZipEntry> enumer = zipFile.getEntries();
		while (enumer.hasMoreElements()) {
			org.apache.tools.zip.ZipEntry entry = enumer.nextElement();
			String name = entry.getName();
			String newFilePath = extractDir.getAbsolutePath() + "/" + name;
			File newFile = new File(newFilePath);
			if (name.endsWith("/") || name.endsWith("\\") ) {
				if (!newFile.exists())
					newFile.mkdirs();
			} else {
				if (newFile.getParentFile() == null || !newFile.getParentFile().exists()) {
					File parentDir = new File(newFile.getParent());
					parentDir.mkdirs();
				}
				newFile.createNewFile();
				InputStream is = zipFile.getInputStream(entry); 
				IOUtils.copyFromStream(is,newFile, entry.getSize());
			}

		}
		return extractDir;
	}
		
	
	public static void zip(String srcPath, String zipPath,int bufferSize,boolean override) throws Exception{
		if(StringUtils.isBlank(srcPath))
			throw new IllegalArgumentException("target file path can't empty!");
        File srcFile = new File(srcPath);
        Long fileLength = srcFile.length();
        File zipFile = new File(zipPath);
        RandomAccessFile raf = new RandomAccessFile(srcFile,"rw");
        if(srcFile != null && srcFile.exists()) {
        	ZipOutputStream zos = new ZipOutputStream(zipFile);
        	File outputFile = new File(zipPath);
        	if(!override && outputFile.exists())
            	throw new FileNotFoundException("Target file has be found!");
        	zos.setEncoding("UTF-8");
            ZipEntry entry = new ZipEntry(srcFile.getName());    
            zos.putNextEntry(entry);  
        	int round = 0;
    		int readSize = bufferSize;
    		while(round * bufferSize < fileLength) {
    			if((round + 1) * bufferSize > fileLength) {
    				readSize = (int) (fileLength - round * bufferSize);
    			}
    			byte[] writeBuff = new byte[readSize];
    			raf.read(writeBuff);
            	zos.write(writeBuff);
    			round ++;
    		}
        	zos.flush();
        	zos.close();
        }
        else {
        	throw new FileNotFoundException("source file not found!");
        }
    }

}
