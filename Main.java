import java.io.*;
import java.awt.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main{
    int k;
    String fileName;
    BufferedImage image;
    Color[] kCenters;
    Color[][] pixels;
    int[][] assignedCluster;
    int imageHeight, imageWidth;

    //Pass in as command line argument the file being processed
    //java Main <location of file> <k>
    public static void main(String [] args){
        Main m = new Main(args);
    }

    public Main(String [] args){
        if(args.length == 0){
            System.out.println("ERROR! Usage: Main <location of file> <k>");
            System.exit(-1);
        }
        else if(args.length <= 2){
            k = 2;
            fileName = args[0];
        }
        else if(args.length == 2){
            k = Integer.parseInt(args[1]);
        }

        // Read in image file
        readImageIn();
        // Find the images width and height(in pixels)
	    imageWidth = image.getWidth();
        imageHeight = image.getHeight();

	    kCenters = new Color[k];
        pixels = new Color[imageHeight][imageWidth];
        assignedCluster = new int[imageHeight][imageWidth];

        // Fill the pixel array with each pixel(as a color)
	    for(int i = 0; i < imageHeight; i++)
		    for (int j = 0; j <imageWidth; j++){
			    pixels[i][j] = new Color(image.getRGB(j, i));
            }
	    //printRGB();

	    //picks k random centers from the image
	    for(int i = 0; i < k; i++){
		    boolean flag;
		    do{
			    flag = false;
			    Random r =  new Random();
			    int row = r.nextInt(imageHeight);
			    int col = r.nextInt(imageWidth);
			    kCenters[i] = pixels[row][col];

                for(int j = 0; j < i; j++)
                    //Check if the newly chosen color matches any other newly chosen color
				    if ((kCenters[i].getRed() == kCenters[j].getRed()) && (kCenters[i].getGreen() == kCenters[j].getGreen()) && (kCenters[i].getBlue() == kCenters[j].getBlue()))
					    flag = true;
		    }while(flag);			
		    System.out.println("" + kCenters[i].getRed() + " " + kCenters[i].getGreen() + " " + kCenters[i].getBlue());
        }

        System.out.println("Choosing initial random centers done!");		

        assignPixelsToClusters();
    }

    public double diff(Color pixel1, Color pixel2){
	    double diffR = Math.abs(pixel1.getRed() - pixel2.getRed());
	    double diffG = Math.abs(pixel1.getGreen() - pixel2.getGreen());
	    double diffB = Math.abs(pixel1.getBlue() - pixel2.getBlue());
	    double diff = diffR + diffG + diffB;
	    return diff;		
    }

    public void readImageIn(){
        try{
            image = ImageIO.read(new File(fileName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printRGB(){
	    System.out.println("Row Col Red Green Blue");
	    for(int i = 0; i < image.getHeight(); i++)
		    for(int j = 0; j < image.getWidth(); j++)
			    //if (pixels[i][j].getGreen() != 255) //(to print pixels from the circle)
				    System.out.println("" + i + " " + j + " " + pixels[i][j].getRed() + " " + pixels[i][j].getGreen() + " " + pixels[i][j].getBlue());
    }

    // This method will assign all pixels to their nearest cluster
    // There are k clusters, ranging from 0 - (k-1)
    public void assignPixelsToClusters(){

        for(int i = 0; i < imageHeight; i++)
            for(int j = 0; j < imageWidth; j++){
                //System.out.println("pixel[" + i + "][" + j + "]"); // Used for some debugging

                //Find the cluster color that is closest to the current pixel color
                int closestCluster = -1; // Holds the cluster that the pixel is closest to in color
                double closestDistance = Double.MAX_VALUE;// Initiate to the a max value
                for(int x = 0; x < kCenters.length; x++){
                    if(diff(pixels[i][j], kCenters[x]) < closestDistance){
                        closestCluster = x;
                        closestDistance = diff(pixels[i][j], kCenters[x]);
                        //Add the coordinates of this pixel to some arrayList of arrayList (a cluster arraylist)???
                    }
                        
                } // Done iterating through all kCenters and should have the closest
                if(closestCluster == -1){
                    System.out.println("Error, no closest cluster found");
                    System.exit(-1);
                }
                assignedCluster[i][j] = closestCluster;
            }
    }   
}