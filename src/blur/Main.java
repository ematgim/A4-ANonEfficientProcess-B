package blur;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
/**
 * This is the main method of the program from here it takes the im ages that are included on a folder and blures them
 * @author Emilio Javier Mateo Gimenez
 *
 */

public class Main {
	private static ExecutorService mExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	public static void main(String[] args) {
		
		long ini = System.currentTimeMillis(); //Getting the time on start
		ArrayList<BufferedImage> images = readDirectory("ImageSource");
		startComputations(images, "ImageBlured");//Starting to blur the iages
		
		awaitTerminationAfterShutdown(mExecutor);
		
		
		long fin = System.currentTimeMillis();	//Getting the time on finish
		
		System.out.println("Time to process: "  + (fin-ini) +" ms");
		
		
		
	}
	
	/**
	 * This method waits until all the threads are finished and then shutdown the Thread pool
	 * @param threadPool The pool of threads to await to finish
	 */
	private static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
		threadPool.shutdown();//Cuts the input of new threads

		try {
			if(threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)){//Wait until all threads are finished or the time limit is finished
				
				threadPool.shutdownNow();//Shutdowns all the threads and the thread pool
				System.out.println("ALL COMPUTATIONS FINISHED");
			}
			
			
		} catch (InterruptedException e) {

		 System.err.println("THE THREADS HAD NOT FINISHED CORRECTLY");
		}
	}
	/**
	 * This method reads a directory and add all the .png to an ArrayList of BufferedImages
	 * @param path Path of the folder to read
	 * @return An array list of the BufferedImages of the path folder
	 */
	private static ArrayList<BufferedImage> readDirectory(String path){
		
		System.out.println("Reading the folder...");
		ArrayList<BufferedImage> images = new ArrayList<>();
		
		File imageSource = new File(path);//Reading the folder
		if(imageSource.isDirectory()){
			for (File img : imageSource.listFiles()) {
				if(img.isFile()){
					if(isAnImage(img)){
						//If is an image is read and saved into the ArrayList
						try {
							BufferedImage i = ImageIO.read(img); 
							images.add(i);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						System.out.println(img.getName() + " Is not an image");
					}
					
				}
			}
		}
		System.out.println("Finish, " + images.size() + " images readed.");
		return images;
	}
	/**
	 * 
	 * @param file The file to check if is an image
	 * @return If is true the file is an image
	 */
	private static boolean isAnImage(File file){
		String mimetype= new MimetypesFileTypeMap().getContentType(file);
        String type = mimetype.split("/")[0];
        if(type.equals("image"))
            return true;
        else 
            return false;
    }
	
	
	/**
	 * This method add all the threads to the pool.
	 * @param images The Array of buffered images to process.
	 * @param savePath The path were the images will be saved
	 */
	private static void startComputations(ArrayList<BufferedImage> images,String savePath) {
		
		System.out.println("STARTING COMPUTATIONS");

		for (int i=0;i<images.size();i++) {
	
			mExecutor.execute(new Blur(images.get(i),savePath));
		
		}


	}
}
