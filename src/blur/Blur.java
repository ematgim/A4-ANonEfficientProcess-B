package blur;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * This class is a Runnable class, this class will blur and will save it on a path
 * @author Emilio Javier Mateo Gimenez
 *
 */
public class Blur implements Runnable{
	private BufferedImage imageSource;
	private String savePath;
	private final String extension = "png";
	/**
	 * 
	 * @param bufferedImage The source image that you are going to blur
	 * @param savePath The save path
	 */
	public Blur(BufferedImage bufferedImage, String savePath){
	
	imageSource = bufferedImage;
	this.savePath = savePath;
	}
	
	
	/**
	 * This method starts the tread where the image is going to be bluered and saved
	 */
	
	@Override
	public void run() {	
		BufferedImage imageBlured = processImage(imageSource);//First we blur the image
		saveImage(imageBlured);//Then we save it.
		}
	
	
	/**
	 * This method saves the image on the given path
	 * @param image The image source where the image is going to be saved
	 */
	
	private void saveImage(BufferedImage image){
		try {
		    BufferedImage bi = image;
		    File outputfile = new File(savePath+"/"+randomName(10, extension));//We create the output file with the random name in the path
		    ImageIO.write(bi, extension, outputfile);//We write the image on the file system
		} catch (IOException e) {

		}
	}
	/**
	 * This method creates a new random name with numbers for the image.
	 * @param lenght Length of the new name
	 * @param extension The extension that will use
	 * @return Random name for a file.
	 */
	private String randomName(int lenght , String extension){
		String result ="";
		
		Random r = new Random();
		
		//Creates a string of random numbers and with a size
		for(int i = 0; i<lenght; i++){
			result+= r.nextInt(9);
		}
		
		result+= "."+extension;//We add the extension of the file that we want
		
		return result;
	}
	/**
	 * This method blurs the image and return the blured image
	 * @param image The image that we want to blur
	 * @return Returns the bured mage
	 */
	private BufferedImage processImage(BufferedImage image) {

		float[] matrix = new float[25];
		for (int i = 0; i < 25; i++)
			matrix[i] = 1.0f/25.0f;
		BufferedImageOp blurFilter = new ConvolveOp(new Kernel(5, 5, matrix),
				ConvolveOp.EDGE_NO_OP, null);
		return blurFilter.filter(image, null);
	}
	

}
