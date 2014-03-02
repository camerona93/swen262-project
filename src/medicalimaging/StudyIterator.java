/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.awt.Image;

/**
 *
 * @author Zach
 */
public interface StudyIterator {
    /**
     * Move index to next image if it exists
     * @return true if image exists or false if index is currently at 
     * the last image
     */
    public abstract boolean next();
    /**
     * Move index to previous image if it exists
     * @return true if image exists or false if index is currently at 0
     */
    public abstract boolean prev();
    /**
     * Returns the current list of medical images
     * @return list of medical images of object, MedicalImage
     */
    public abstract Image[] getImages();
    /**
     * @return current index
     */
    public abstract int getIndex();
}
