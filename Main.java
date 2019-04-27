import java.io.*;
import java.awt.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main{
    int k;
    String fileName;
    String imageFileType;
    BufferedImage image;
    BufferedImage newImage;
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
        if(args.length == 2){
            k = Integer.parseInt(args[1]);
        }

        System.out.println("K = " + k);

        // Read in image file
        readImageIn();
        // Find the images width and height(in pixels)
	    imageWidth = image.getWidth();
        imageHeight = image.getHeight();

        imageFileType = getImgFileType(fileName);
        // Confirm that the fileType is not null
        if(imageFileType == null){
            System.out.println("ERROR getting the image file type");
            System.exit(-1);
        }

        System.out.println("ImageWidth: " + imageWidth);
        System.out.println("ImageHeight: " + imageHeight);
        System.out.println("Image file type: " + getImgFileType(fileName));

        //System.exit(1);

	    kCenters = new Color[k];
        pixels = new Color[imageHeight][imageWidth];
        assignedCluster = new int[imageHeight][imageWidth];

        System.out.println("Filling pixels array...\n");

        // Fill the pixel array with each pixel(as a color)
	    for(int i = 0; i < imageHeight; i++)
		    for (int j = 0; j <imageWidth; j++){
			    pixels[i][j] = new Color(image.getRGB(j, i));
            }
	    //printRGB();

        //picks k random centers from the image
        
        System.out.println("Picking k random centers...\n");

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
		    // System.out.println("    " + kCenters[i].getRed() + " " + kCenters[i].getGreen() + " " + kCenters[i].getBlue()); // Keep this in or out?
        }

        System.out.println("Choosing initial random centers done!\n");		

        assignPixelsToClusters();

        System.out.println("Finding new centers...\n");

        findNewCenters();

        System.out.println("Creating new Image...\n");

        createNewImageFile();

        System.out.println("New " + k + " colored image has been created!");
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
				    System.out.println("    " + i + " " + j + " " + pixels[i][j].getRed() + " " + pixels[i][j].getGreen() + " " + pixels[i][j].getBlue());
    }

    // This method will assign all pixels to their nearest cluster
    // There are k clusters, ranging from 0 - (k-1)
    public void assignPixelsToClusters(){

        for(int i = 0; i < imageHeight; i++){
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

    public void findNewCenters(){
        for(int i = 0; i < kCenters.length; i++){
            kCenters[i] = findClusterAvgColor(i);
            //System.out.println("    " + kCenters[i].getRed() + " " + kCenters[i].getGreen() + " " + kCenters[i].getBlue());// Keep this in or out
        }
    }

    public Color findClusterAvgColor(int cluster){
        // Check if the input is valid, cluster numbers range from 0 - (k-1)
        if(cluster >= k){
            System.out.println("ERROR, no such cluster exists!");
            System.exit(-1);
        }

        int rSum = 0;
        int gSum = 0;
        int bSum = 0;
        int numPixels = 0;

        //Sum up all the values of each pixels R, G and B in the cluster
        for(int i = 0; i < imageHeight; i++){
            for(int j = 0; j < imageWidth; j++){
                if(assignedCluster[i][j] == cluster){
                    rSum += pixels[i][j].getRed();
                    gSum += pixels[i][j].getGreen();
                    bSum += pixels[i][j].getBlue();
                    numPixels++;
                }
            }
        } // All pixels have been summed at this point

        return new Color(rSum/numPixels, gSum/numPixels, bSum/numPixels);
    }

    public void createNewImageFile(){
        //Create the new bufferedImage and fill the image with the appropriate pixels
        fillNewImage();

        try {
            ImageIO.write(newImage, imageFileType, new File(k + "_Colored_Image." + imageFileType));
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
    
    public void fillNewImage(){
        // Create a new BufferedImage, to be set with the pixels
        newImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // Fill the new image with the pixels
        for(int i = 0; i < imageHeight; i++)
            for(int j = 0; j < imageWidth; j++){
                //System.out.println("pixel[" + i + "][" + j + "]");
                newImage.setRGB(j, i, kCenters[assignedCluster[i][j]].getRGB());
                //newImage.setRGB(j, i, kCenters[0].getRGB());
            }
    }

    public String getImgFileType(String fileName){
        if(fileName.length() > 4){
            return fileName.substring(fileName.length() - 3);
        }
        else    
            return null;
    }
}