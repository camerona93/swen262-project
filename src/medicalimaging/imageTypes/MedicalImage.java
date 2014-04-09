/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.imageTypes;

import javax.swing.ImageIcon;
import medicalimaging.model.StudyElement;


/**
 * Class to represent a image from a study
 * @author ericlee
 */
public abstract class MedicalImage extends StudyElement{
    public String imagePath;
    
    /**
     * Return an image icon of the image
     * @return 
     */
    public abstract ImageIcon loadImage();
    
    
    public String toString() {
        return this.imagePath.substring(this.imagePath.lastIndexOf("/") + 1);
    }
}
