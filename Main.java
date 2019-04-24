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

	readImageIn();
	int imageWidth = image.getWidth();
	int imageHeight = image.getHeight();

	kCenters = new Color[k];
	pixels = new Color[imageHeight][imageWidth];

	for(int i = 0; i < imageHeight; i++)
		for (int j = 0; j <imageWidth; j++){
			pixels[i][j] = new Color(image.getRGB(j, i));
		}
	//printRGB();

	//picks k random centers from the image
	//need to check they aren't all the same 
	for(int i = 0; i < k; i++){
		Random r =  new Random();
		int row = r.nextInt(imageHeight);
		int col = r.nextInt(imageWidth);
		kCenters[i] = pixels[row][col];
		System.out.println("" + kCenters[i].getRed() + " " + kCenters[i].getGreen() + " " + kCenters[i].getBlue());
	}
	
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
}