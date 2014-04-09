/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.geom.Rectangle2D;

/**
 * Protocol for listeners of the image view
 * @author ericlee
 */
public interface MedicalImageViewProtocol {
    /**
     * Called when a rect on an image is selected
     * @param image (int) index of image on the image grid
     * @param rect (Rectangle2D) bounds of the selected rectangle scaled to the
     * model
     */
    public void rectSelected(int image, Rectangle2D rect);
    
    /**
     * Called when a rectangle is deselected
     */
    public void rectDeselected();
}
