/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author ericlee
 */
public class MedicalImage {
    public String imagePath;
    private BufferedImage image;
    
    public MedicalImage(String imagePath) {
        this.imagePath = imagePath;
        Image im = null;
        try{
            im = ImageIO.read(new File(this.imagePath));
        }
        catch(IOException io){
            System.out.println("Image doesn't exist");
        }
        this.image = (BufferedImage)im;
    }
    
    public BufferedImage getImage(){
        return this.image;
    }
}
