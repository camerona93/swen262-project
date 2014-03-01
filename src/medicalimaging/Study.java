/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;
import java.util.ArrayList;

/**
 *
 * @author ericlee
 */
public class Study implements java.io.Serializable {
    private ArrayList<MedicalImage> images;
    protected String name;
    
    public Study(String name) {
        this.name = name;
        this.images = new ArrayList<MedicalImage>();
    }
    
    public int getImageCount() {
        return this.images.size();
    }
    
    public MedicalImage getImage(int index) {
        if(index < this.getImageCount())
            return this.images.get(index);
        return null;
    }
    
    public void removeImage(int index) {
        if(index < this.getImageCount())
            this.images.remove(index);
    }

    public void addImage(MedicalImage medicalImage) {
        this.images.add(medicalImage);
    }
    
    public String toString() {
        String returnString = this.name + "'s Images:\n";
        for(MedicalImage image : this.images) {
            returnString += image.imagePath + "\n";
        }
        return returnString;
    }
}
