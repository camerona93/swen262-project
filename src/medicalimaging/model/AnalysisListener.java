/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author Cameron
 */
public abstract class AnalysisListener {
    
     public abstract void newAnalysis(Study study, Rectangle2D rect);
     
}
