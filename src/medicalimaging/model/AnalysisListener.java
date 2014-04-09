/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import java.awt.geom.Rectangle2D;
import medicalimaging.gui.MedicalImageViewProtocol;

/**
 * Interface for Listener to see when the selected index changes
 * @author Cameron
 */
public interface AnalysisListener {
     public void newAnalysis(Study astudy, Rectangle2D arect, MedicalImageViewProtocol delegate);
     public void update(Study study);
}
