/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;
/**
 *
 * @author ericlee
 */
public class MedicalImage extends StudyElement{
    public String imagePath;
    
    public MedicalImage(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String toString() {
        return this.imagePath.substring(this.imagePath.lastIndexOf("/") + 1);
    }
}
