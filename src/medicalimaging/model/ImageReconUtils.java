/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import medicalimaging.imageTypes.MedicalImage;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author ericlee
 */
public class ImageReconUtils {
    public static int[][][] generate3D(Study study) {
        int imageCount = study.getImageCount();
        int height = 0;
        if(imageCount > 0)
            height = ((MedicalImage)study.getElement(0)).loadImage().getImage().getHeight(null) - 1;
        
        int[][][] reconImage = new int[height][imageCount][height]; 
        for(int i = study.getImageCount() - 1; i >= 0; i--) {
            Image image = ((MedicalImage)study.getElement(i)).loadImage().getImage();
            
            BufferedImage bfImage = getBufferedImageFromImage(image);
            
            //Cycle through image pixels
            for(int h = 0; h < bfImage.getHeight()-1; h++) {
                for(int w = 0; w < bfImage.getWidth()-1; w++) {
                    reconImage[w][i][h] = bfImage.getRGB(w, h);
                }
            }
        }
        
        return reconImage;
    }
    
    public static BufferedImage getBufferedImageFromImage(Image image) {
        BufferedImage bfImage = new BufferedImage(image.getHeight(null), image.getWidth(null), BufferedImage.TYPE_INT_ARGB);
            
        Graphics2D bGr = bfImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        
        return bfImage;
    }
}
