/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.imageTypes;

import javax.swing.ImageIcon;


/**
 * Class for images that are supported by java(jpegs, pngs, etc.)
 * @author ericlee
 */
public class StandardImage extends MedicalImage{

    /**
     * Constructor
     * @param imagePath (String) path to image 
     */
    public StandardImage(String imagePath) {
        this.imagePath = imagePath;
    }
    
    /**
     * Loads the image into memory
     * @return (ImageIcon) representation of the image
     */
    @Override
    public ImageIcon loadImage() {
        return new ImageIcon(this.imagePath);
    }
    
}
