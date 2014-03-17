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
public class StandardImage extends MedicalImage{

    public StandardImage(String imagePath) {
        this.imagePath = imagePath;
    }
    @Override
    public ImageIcon loadImage() {
        return new ImageIcon(this.imagePath);
    }
    
}
