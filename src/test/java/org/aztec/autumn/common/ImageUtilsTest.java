package org.aztec.autumn.common;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import org.aztec.autumn.common.utils.ImageUtils;

public class ImageUtilsTest {

	public ImageUtilsTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		
		//ImageUtils.pressImage("test/images/Chrysanthemum.jpg", "test/images/Desert.jpg", "test/images/Chrysanthemum.jpg", 100, 100, 0.6f);

		ImageUtils.printText("5", "test/images/Chrysanthemum.jpg", "test/images/generate_img.jpg", Font.SERIF, Font.ITALIC, Color.black, 640, 100, 100, 1f);
	}

}
