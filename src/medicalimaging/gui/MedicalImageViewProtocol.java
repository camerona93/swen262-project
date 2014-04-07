/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author ericlee
 */
public interface MedicalImageViewProtocol {
    public void rectSelected(int image, Rectangle2D rect);
    public void rectDeselected();
}
