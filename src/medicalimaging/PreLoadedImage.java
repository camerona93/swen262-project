/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import javax.swing.ImageIcon;

/**
 *
 * @author ericlee
 */
public class PreLoadedImage extends MedicalImage{
    private ImageIcon image;
    
    public PreLoadedImage(ImageIcon _image) {
        image = _image;
    }
    
    public ImageIcon loadImage() {
        return image;
    }
}
