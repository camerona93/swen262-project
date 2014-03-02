/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.awt.image.BufferedImage;

/**
 *
 * @author ericlee
 */
public class MedicalImage {
    public String imagePath;
    private BufferedImage image;
    
    public MedicalImage(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public BufferedImage getImage(){
        return this.image;
    }
}
