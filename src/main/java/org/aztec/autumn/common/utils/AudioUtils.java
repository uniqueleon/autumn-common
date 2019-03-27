package org.aztec.autumn.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class AudioUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(AudioUtils.class);

	public AudioUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static long getPlayTime(String path) {
		try {
			File file = new File(path);
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			clip.open(ais);
			return clip.getMicrosecondLength() / 1000000l;
		} catch (Exception e) {
			LOG.warn(e.getMessage(),e);
			return 0l;
		}
	}
	
	public static File combine(File file1,File file2) throws LineUnavailableException, UnsupportedAudioFileException, IOException{
		
		InputStream is1 = new FileInputStream(file1);
		InputStream is2 = new FileInputStream(file2);
		
		File newFile = new File(file1.getParent() + "/" + RandomUtils.nextLong() + ".mp3");
		newFile.createNewFile();
		OutputStream os = new FileOutputStream(newFile);
		int readResult = 0;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		do{
			readResult = is1.read(buffer, 0, bufferSize);
			if(readResult == -1)
				break;
			byte[] tmpByte = buffer;
			os.write(tmpByte, 0, bufferSize);
		}while(readResult != -1);
		do{
			readResult = is2.read(buffer, 0, bufferSize);
			if(readResult == -1)
				break;
			byte[] tmpByte = buffer;
			os.write(tmpByte, 0, bufferSize);
		}while(readResult != -1);
		is1.close();
		is2.close();
		os.flush();
		os.close();
		return newFile;
		/*Clip clip = AudioSystem.getClip();
		AudioInputStream ais = AudioSystem.getAudioInputStream(file1);
		clip.open(ais);
		Control[] controls = clip.getControls();
		for(Control contrl : controls){
			System.out.println(contrl.getClass().getName());
		}
		return null;*/
	}
	
	public static File playMP3(File mp3File) throws FileNotFoundException, JavaLayerException{
		Player player = new Player(new FileInputStream(mp3File));
		player.play();
		return mp3File;
	}

	public static void main(String[] args)  {

		//System.out.println(getPlayTime(""));
		try {
			/*List<File> wavFiles = FileUtils.listLocalFiles("webapps/audio", "[.wmi|.mp3|.wav]$", true);
			for(File wavFile : wavFiles){
				System.out.println(getPlayTime(wavFile.getAbsolutePath()));
			}*/
			File mp3File1 = new File("audio/t1.mp3");
			//combine(new File("audio/t1.mp3"),new File("audio/t2.mp3"));
			playMP3(mp3File1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
