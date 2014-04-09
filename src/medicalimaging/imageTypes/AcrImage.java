/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.imageTypes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

/**
 * Loads an ACR image from the disk
 * @author ericlee
 */
public class AcrImage extends MedicalImage{
    public static final int HEADER_OFFSET = 0x2000;
    
    /**
     * Constructor
     * @param imagePath (String) path to image 
     */
    public AcrImage(String imagePath) {
        this.imagePath = imagePath;
    }
    
    /**
     * Loads the image from disk
     * @return (ImageIcon) icon representation of the image
     */
    public ImageIcon loadImage() {
        FileImageInputStream imageFile = null;
	try {
	    imageFile = new FileImageInputStream(new File(imagePath));
	    imageFile.seek(HEADER_OFFSET);
	}
	catch (FileNotFoundException e) {
	    System.err.print("Error opening file: ");
	    System.err.println(e.getMessage());
	    System.exit(2);
	}
	catch (IOException e) {
	    System.err.print("IO error on file: ");
	    System.err.println(e.getMessage());
	    System.exit(2);
	}

	int sliceWidth = 256;
	int sliceHeight = 256;
	    
	BufferedImage sliceBuffer = 
	    new BufferedImage( sliceWidth,sliceHeight,
			       BufferedImage.TYPE_USHORT_GRAY );

	for ( int i = 0; i < sliceBuffer.getHeight(); i++ ) {
	    for ( int j = 0; j < sliceBuffer.getWidth(); j++ ) {

		int pixelHigh = 0;
		int pixelLow = 0;
		int pixel;
		
		try {
		    pixelHigh = imageFile.read();
		    pixelLow = imageFile.read();
		    pixel = pixelHigh << 4 | pixelLow >> 4;
		    sliceBuffer.setRGB( j, i,
				      pixel << 16 | pixel << 8 | pixel);

		}
		catch (IOException e) {
		    System.err.print("IO error readin byte: ");
		    System.err.println(e.getMessage());
		}
	    }
	}
        return new ImageIcon(sliceBuffer);
    }
}
