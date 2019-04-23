import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main{
    int k;
    String fileName;
    BufferedImage image;

    //Pass in as command line argument the file being processed
    //java Main <location of file> <k>
    public static void main(String [] args){
        Main m = new Main();
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
    }

    public void readImageIn(){
        try{
            image = ImageIO.read(new File(fileName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}