package org.aztec.autumn.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageUtils {

	public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

	public final static void scale(String srcImageFile, String result, int scale, boolean flag) throws IOException {
		BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
		int width = src.getWidth(); // 得到源图宽
		int height = src.getHeight(); // 得到源图长
		if (flag) {// 放大
			width = width * scale;
			height = height * scale;
		} else {// 缩小
			width = width / scale;
			height = height / scale;
		}
		Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.dispose();
		
		writeAsNoCompression(tag, result);
		//ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		//ImageReader ir = ImageIO.getImageReadersByFormatName("JPEG").next();
		//ir.getStreamMetadata();
		//ImageIO.write(tag,  "JPEG", output);
	}
	
	private static void writeAsNoCompression(BufferedImage img,String result) throws IOException{
		File outputFile = new File(result);
		outputFile.delete();
		ImageWriter iw = ImageIO.getImageWritersByFormatName("JPG").next();
		ImageWriteParam writeParam = iw.getDefaultWriteParam();
		writeParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);
		writeParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
		//writeParam.setTilingMode(ImageWriteParam.MODE_DISABLED);
		//writeParam.setCompressionQuality(1f);
		ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile);
		iw.setOutput(ios);
		iw.write(img);
		ios.flush();
		ios.close();
	}

	public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb)
			throws IOException {
		double ratio = 0.0; // 缩放比例
		File f = new File(srcImageFile);
		BufferedImage bi = ImageIO.read(f);
		Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		// 计算比例
		if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
			if (bi.getHeight() > bi.getWidth()) {
				ratio = (new Integer(height)).doubleValue() / bi.getHeight();
			} else {
				ratio = (new Integer(width)).doubleValue() / bi.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			itemp = op.filter(bi, null);
		}
		if (bb) {// 补白
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			if (width == itemp.getWidth(null))
				g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null),
						Color.white, null);
			else
				g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null),
						Color.white, null);
			g.dispose();
			itemp = image;
		}
		writeAsNoCompression((BufferedImage)itemp,result);
		//ImageIO.write((BufferedImage) itemp, "PNG", new File(result));
	}

	public final static void cut(String srcImageFile, String result, int x, int y, int width, int height)
			throws IOException {
		// 读取源图像
		BufferedImage bi = ImageIO.read(new File(srcImageFile));
		int srcWidth = bi.getHeight(); // 源图宽度
		int srcHeight = bi.getWidth(); // 源图高度
		if (srcWidth > 0 && srcHeight > 0) {
			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH);
			// 四个参数分别为图像起点坐标和宽高
			// 即: CropImageFilter(int x,int y,int width,int height)
			ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
			Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
			g.dispose();
			// 输出为文件
			ImageIO.write(tag, "JPEG", new File(result));
		}
	}

	public final static void cutAsCircula(String srcImageFile, String result, int x, int y, int width, int height)
			throws IOException {
		BufferedImage bi = ImageIO.read(new File(srcImageFile));
		int srcWidth = bi.getWidth(); // 源图宽度
		int srcHeight = bi.getHeight(); // 源图高度
		if (srcWidth > 0 && srcHeight > 0) {
			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH);
			// 四个参数分别为图像起点坐标和宽高
			// 即: CropImageFilter(int x,int y,int width,int height)
			ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
			Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			//g.drawRect(img, 0, 0, width, height, null);
			Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, width, height);
			g.setClip(shape);
			g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
			// g.drawRoundRect(0, 0, width, height, width , height);
			// g.setClip();
			// g.setClip(new Circ);
			// g.fillArc(0, 0, width, height, 0, 360);
			g.dispose();
			// 输出为文件

			writeAsNoCompression(tag, result);
			//ImageIO.write(tag, "JPEG", new File(result));
		}
	}
	
	public final static void cutAsCircula(String bgImg,String srcImageFile, String result, int posX, int posY,int x, int y, int width, int height)
			throws IOException {
		BufferedImage bi = ImageIO.read(new File(srcImageFile));
		int srcWidth = bi.getWidth(); // 源图宽度
		int srcHeight = bi.getHeight(); // 源图高度
		
		
		BufferedImage bgBuffer = ImageIO.read(new File(bgImg));
		int srcWidth2 = bgBuffer.getWidth(); // 源图宽度
		int srcHeight2 = bgBuffer.getHeight(); // 源图高度
		if (srcWidth > 0 && srcHeight > 0) {
			Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			// 四个参数分别为图像起点坐标和宽高
			// 即: CropImageFilter(int x,int y,int width,int height)
			ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
			Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			Image bgImage = bgBuffer.getScaledInstance(srcWidth2, srcHeight2, Image.SCALE_SMOOTH);
			ImageFilter cropFilter2 = new CropImageFilter(0, 0, srcWidth2, srcHeight2);
			Image img2 = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(bgImage.getSource(), cropFilter2));
			BufferedImage tag = new BufferedImage(srcWidth2, srcHeight2, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img2, 0, 0, null);
			//g.drawRect(img, 0, 0, width, height, null);
			Ellipse2D.Double shape = new Ellipse2D.Double(posX, posY, width, height);
			g.setClip(shape);
			g.drawImage(img, posX,posY, width, height, null); // 绘制切割后的图
			// g.drawRoundRect(0, 0, width, height, width , height);
			// g.setClip();
			// g.setClip(new Circ);
			// g.fillArc(0, 0, width, height, 0, 360);
			g.dispose();
			// 输出为文件

			writeAsNoCompression(tag, result);
			//ImageIO.write(tag, "JPG", new File(result));
		}
	}

	public final static void combine(String imgFile1, String imgFile2,String result, int x, int y, int width, int height) throws IOException {

		BufferedImage bi = ImageIO.read(new File(imgFile1));
		int srcWidth = bi.getWidth(); // 源图宽度
		int srcHeight = bi.getHeight(); // 源图高度

		BufferedImage bi2 = ImageIO.read(new File(imgFile2));
		//File tempFile = File.createTempFile("tmp_", "_img_file");
		//scale2(imgFile2, tempFile.getPath(), height, width,true);
		if (srcWidth > 0 && srcHeight > 0) {
			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH);

			ImageFilter cropFilter = new CropImageFilter(0, 0, srcWidth, srcHeight);
			Image image2 = bi2.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageFilter cropFilter2 = new CropImageFilter(0, 0, width, height);
			Image img1 = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			Image img2 = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image2.getSource(), cropFilter2));
			BufferedImage tag = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img1, 0, 0, srcWidth, srcHeight, null);
			g.drawImage(img2, x, y, width, height, null);
			g.dispose();
			// 输出为文件
			//writeAsNoCompression(tag, result);
			ImageIO.write(tag, "PNG", new File(result));
		}
		//tempFile.delete();
	}

	public final static void cut2(String srcImageFile, String descDir, int rows, int cols) throws IOException {
		if (rows <= 0 || rows > 20)
			rows = 2; // 切片行数
		if (cols <= 0 || cols > 20)
			cols = 2; // 切片列数
		// 读取源图像
		BufferedImage bi = ImageIO.read(new File(srcImageFile));
		int srcWidth = bi.getHeight(); // 源图宽度
		int srcHeight = bi.getWidth(); // 源图高度
		if (srcWidth > 0 && srcHeight > 0) {
			Image img;
			ImageFilter cropFilter;
			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH);
			int destWidth = srcWidth; // 每张切片的宽度
			int destHeight = srcHeight; // 每张切片的高度
			// 计算切片的宽度和高度
			if (srcWidth % cols == 0) {
				destWidth = srcWidth / cols;
			} else {
				destWidth = (int) Math.floor(srcWidth / cols) + 1;
			}
			if (srcHeight % rows == 0) {
				destHeight = srcHeight / rows;
			} else {
				destHeight = (int) Math.floor(srcWidth / rows) + 1;
			}
			// 循环建立切片
			// 改进的想法:是否可用多线程加快切割速度
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					// 四个参数分别为图像起点坐标和宽高
					// 即: CropImageFilter(int x,int y,int width,int height)
					cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
					img = Toolkit.getDefaultToolkit()
							.createImage(new FilteredImageSource(image.getSource(), cropFilter));
					BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
					Graphics g = tag.getGraphics();
					g.drawImage(img, 0, 0, null); // 绘制缩小后的图
					g.dispose();
					// 输出为文件
					ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
				}
			}
		}
	}

	public final static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight)
			throws IOException {
		if (destWidth <= 0)
			destWidth = 200; // 切片宽度
		if (destHeight <= 0)
			destHeight = 150; // 切片高度
		// 读取源图像
		BufferedImage bi = ImageIO.read(new File(srcImageFile));
		int srcWidth = bi.getHeight(); // 源图宽度
		int srcHeight = bi.getWidth(); // 源图高度
		if (srcWidth > destWidth && srcHeight > destHeight) {
			Image img;
			ImageFilter cropFilter;
			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH);
			int cols = 0; // 切片横向数量
			int rows = 0; // 切片纵向数量
			// 计算切片的横向和纵向数量
			if (srcWidth % destWidth == 0) {
				cols = srcWidth / destWidth;
			} else {
				cols = (int) Math.floor(srcWidth / destWidth) + 1;
			}
			if (srcHeight % destHeight == 0) {
				rows = srcHeight / destHeight;
			} else {
				rows = (int) Math.floor(srcHeight / destHeight) + 1;
			}
			// 循环建立切片
			// 改进的想法:是否可用多线程加快切割速度
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					// 四个参数分别为图像起点坐标和宽高
					// 即: CropImageFilter(int x,int y,int width,int height)
					cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);

					img = Toolkit.getDefaultToolkit()
							.createImage(new FilteredImageSource(image.getSource(), cropFilter));
					BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
					Graphics g = tag.getGraphics();
					g.drawImage(img, 0, 0, null); // 绘制缩小后的图
					g.dispose();
					// 输出为文件
					ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
				}
			}
		}
	}

	public final static void convert(String srcImageFile, String formatName, String destImageFile) throws IOException {
		File f = new File(srcImageFile);
		f.canRead();
		f.canWrite();
		BufferedImage src = ImageIO.read(f);
		ImageIO.write(src, formatName, new File(destImageFile));
	}

	public final static void gray(String srcImageFile, String destImageFile) throws IOException {

		BufferedImage src = ImageIO.read(new File(srcImageFile));
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		src = op.filter(src, null);
		ImageIO.write(src, "JPEG", new File(destImageFile));
	}

	public final static void printText(String pressText, String srcImageFile, String destImageFile, String fontName,
			int fontStyle, Color color, int fontSize, int x, int y, float alpha) throws IOException {
		File img = new File(srcImageFile);
		Image src = ImageIO.read(img);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(src, 0, 0, width, height, null);
		g.setColor(color);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 在指定坐标绘制水印文字
		g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
		g.dispose();
		ImageIO.write((BufferedImage) image, "PNG", new File(destImageFile));// 输出到文件流
	}
	
	public static void printTexts(List<TextPrintInfo> printInfos, String srcImageFile, String destImageFile) throws IOException{
		File img = new File(srcImageFile);
		Image src = ImageIO.read(img);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(src, 0, 0, width, height, null);
		for(TextPrintInfo tpi : printInfos){
			g.setColor(tpi.getColor());
			g.setFont(new Font(tpi.getFontName(), tpi.getFontStyle(), tpi.getFontSize()));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, tpi.getAlpha()));
			// 在指定坐标绘制水印文字
			g.drawString(tpi.getText(), (width - (getLength(tpi.getText()) * tpi.getFontSize())) / 2 + tpi.getX(), (height - tpi.getFontSize()) / 2 + tpi.getY());
		}
		g.dispose();
		//writeAsNoCompression((BufferedImage) image, destImageFile);
		ImageIO.write((BufferedImage) image, "PNG", new File(destImageFile));
	}
	
	public static class TextPrintInfo{
		
		private String text;
		private String fontName;
		private int fontStyle;
		private Color color;
		private int fontSize;
		private int x;
		private int y;
		private float alpha;
		
		public TextPrintInfo(String text, String fontName, int fontStyle, Color color, int fontSize, int x, int y,
				float alpha) {
			super();
			this.text = text;
			this.fontName = fontName;
			this.fontStyle = fontStyle;
			this.color = color;
			this.fontSize = fontSize;
			this.x = x;
			this.y = y;
			this.alpha = alpha;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getFontName() {
			return fontName;
		}
		public void setFontName(String fontName) {
			this.fontName = fontName;
		}
		public int getFontStyle() {
			return fontStyle;
		}
		public void setFontStyle(int fontStyle) {
			this.fontStyle = fontStyle;
		}
		public Color getColor() {
			return color;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		public int getFontSize() {
			return fontSize;
		}
		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public float getAlpha() {
			return alpha;
		}
		public void setAlpha(float alpha) {
			this.alpha = alpha;
		}
		
	}


	public final static void pressImage(String pressImg, String srcImageFile, String destImageFile, int x, int y,
			float alpha) throws IOException {
		File img = new File(srcImageFile);
		Image src = ImageIO.read(img);
		int wideth = src.getWidth(null);
		int height = src.getHeight(null);
		BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(src, 0, 0, wideth, height, null);
		// 水印文件
		Image src_biao = ImageIO.read(new File(pressImg));
		int wideth_biao = src_biao.getWidth(null);
		int height_biao = src_biao.getHeight(null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao, null);
		// 水印文件结束
		g.dispose();
		ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));
	}

	public final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}

}