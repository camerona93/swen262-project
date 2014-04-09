/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.imageTypes;

import javax.swing.ImageIcon;

/**
 * Stores an image loaded in memory
 * @author ericlee
 */
public class PreLoadedImage extends MedicalImage{
    private ImageIcon image;
    
    /**
     * Constructor
     * @param _image (ImageIcon) representation of the image 
     */
    public PreLoadedImage(ImageIcon _image) {
        image = _image;
    }
    
    /**
     * Loads the image from memory
     * @return (ImageIcon) the image
     */
    public ImageIcon loadImage() {
        return image;
    }
}
