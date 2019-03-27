package org.aztec.autumn.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.aztec.autumn.common.GlobalConst;

public class IOUtils {

	public final static ByteBuffer readBuffer = ByteBuffer.allocate(GlobalConst.MAX_BUFFER_SIZE);
	public final static ByteBuffer writeBuffer = ByteBuffer.allocate(GlobalConst.MAX_BUFFER_SIZE);

	private IOUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String readString(BufferedInputStream is, int offset, int length)
			throws IOException, InterruptedException {
		StringBuilder builder = new StringBuilder();
		do {
			if (is.available() == 0)
				return "";
			byte[] buffer = new byte[length];
			is.skip(offset);
			is.read(buffer);
			builder.append(new String(buffer));
		} while (is.available() > 0);
		return builder.toString().trim();
	}

	public static void write(Socket socket, byte[] content, boolean flush) throws IOException {
		OutputStream os = socket.getOutputStream();
		os.write(content);
		if (flush)
			os.flush();
	}

	public synchronized static String read(SocketChannel channel, String charset) throws IOException {
		synchronized (readBuffer) {
			cleanBuffer(readBuffer);
			channel.read(readBuffer);
			if (readBuffer.remaining() == 0)
				return "";
			byte[] bytes = compactBytes(readBuffer);
			return new String(readBuffer.array(), charset).trim();
		}
		// return new String(content.toString().getBytes(charset));
	}

	private static final int MAX_ZERO_BYTE_TOLENRANT = 1024;

	private static byte[] compactBytes(final ByteBuffer buffer) {
		byte[] bytes = buffer.array();
		int zeroCount = 0;
		int contentLength = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == 0) {
				zeroCount++;
				if (zeroCount >= MAX_ZERO_BYTE_TOLENRANT)
					break;
			} else {
				contentLength = i;
				zeroCount = 0;
			}
		}
		byte[] newByte = new byte[contentLength];
		for (int i = 0; i < contentLength; i++) {
			newByte[i] = bytes[i];
		}
		return newByte;
	}

	private static void cleanBuffer(final ByteBuffer buffer) {
		buffer.clear();
		for (int i = 0; i < buffer.limit(); i++) {
			buffer.put((byte) 0);
		}
		buffer.clear();
	}

	public synchronized static void response(SocketChannel channel, String content, String charset) throws IOException {
		// byteBuffer = ByteBuffer.allocate(GlobalConfig.getMaxBufferSize());
		byte[] utf8Bytes = content.getBytes(charset);
		writeBuffer.clear();
		writeBuffer.put(utf8Bytes);
		writeBuffer.flip();
		while (writeBuffer.hasRemaining()) {
			channel.write(writeBuffer);
		}
		// channel.shutdownOutput();
	}

	public static File copyFromStream(InputStream is, File targetFile,int availableBytes) throws IOException {
		if (!targetFile.exists() || targetFile.isDirectory())
			throw new IllegalArgumentException("File not exists or not a file!");
		FileOutputStream fos = new FileOutputStream(targetFile);
		copyFromStream(is, fos, availableBytes);
		return targetFile;
	}
	
	public static void copyFromStream(InputStream is,OutputStream fos,int availableBytes) throws IOException{
		if (availableBytes < GlobalConst.DEFAULT_BUFFER_SIZE) {
			readAndWrite(is, fos, 0, availableBytes);
			fos.flush();
			fos.close();
		} else {
			int cursor = 0;
			while (cursor < availableBytes) {
				cursor = readAndWrite(is, fos, cursor, availableBytes);
			}
		}
	}
	
	
	public static File copyFromStream(InputStream is, File targetFile,long availableBytes) throws IOException {
		return copyFromStream(is, targetFile, Integer.parseInt("" + availableBytes));
	}
	

	public static File copyFile(File thisFile, File targetFile) throws FileNotFoundException, IOException {
		if(!targetFile.exists())targetFile.createNewFile();
		FileInputStream fis = new FileInputStream(thisFile);
		return copyFromStream(fis, targetFile,fis.available());
	}

	public static int readAndWrite(InputStream is, OutputStream os, int cursor, int totalAavailable)
			throws IOException {

		byte[] buffer = null;
		int readLength = totalAavailable - cursor;
		if (readLength < GlobalConst.DEFAULT_BUFFER_SIZE)
			buffer = new byte[readLength];
		else
			buffer = new byte[GlobalConst.DEFAULT_BUFFER_SIZE];
		int reads = 0;
		int bufferCursor = 0;
		while (reads != -1) {
			int remainBytes = buffer.length - bufferCursor;
			if (bufferCursor >= buffer.length)
				break;
			reads = is.read(buffer, bufferCursor, remainBytes);
			if (reads != -1)
				bufferCursor += reads;
			else
				bufferCursor += remainBytes;
		}
		;
		os.write(buffer);
		return cursor + bufferCursor;
	}

}
