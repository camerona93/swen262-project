/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import medicalimaging.histogramLibrary.*;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.model.AnalysisListener;
import medicalimaging.model.Study;
import medicalimaging.gui.HistogramView;

/**
 *
 * @author Cameron
 */
public class AnalysisController extends AnalysisListener {

    @Override
    public void newAnalysis(Study study, Rectangle2D rect) {
        AnalysisFrame frame = new AnalysisFrame();
        
        AdaptiveHistogram h = new AdaptiveHistogram();
        int[] pixels = getPixelsInRect(toBufferedImage(((MedicalImage)(study.getElement(study.getSelectedIndex()))).loadImage().getImage()), rect);
        for (int a : pixels ) {
            h.addValue(a);
        }
        
        
        frame.histogramView.setHistogram(h.toTable());
        
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        
    }
    
    private static int[] getPixelsInRect (BufferedImage img, Rectangle2D rect) {
        ArrayList<Integer> pixelAL = new ArrayList<>();
        
        for (int y = (int)rect.getY(); y < rect.getHeight() + (int)rect.getY(); y++) {
            for (int x = (int)rect.getX(); x < rect.getWidth() + (int)rect.getX(); x++) {
                int c = (new Color(img.getRGB(x, y))).getRed();
                
                pixelAL.add(c);
            }
        }
        
        int[] pixels = new int[pixelAL.size()];
        for (int a = 0; a < pixelAL.size(); a++ ) {
            pixels[a] = pixelAL.get(a).intValue();
        }
        
        return pixels;
    }
    
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    
   
    
}
