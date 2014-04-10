/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import medicalimaging.histogramLibrary.*;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.model.AnalysisListener;
import medicalimaging.model.Study;

/**
 * Controller for the Histogram analysis window
 * @author Cameron
 */
public class AnalysisController implements AnalysisListener, WindowListener {
    private AnalysisFrame frame;
    private Rectangle2D rect;
    private MedicalImageViewProtocol delegate;
    
    
    /** Generates a new analysis. Spawns a new AnalysisFrame.
     * 
     * @param astudy - Study to analyze
     * @param arect - rectangle defining the bounds of the selected image.
     * @param adelegate - the delegate to notify when the window is closed.
     */
    @Override
    public void newAnalysis(Study astudy, Rectangle2D arect, MedicalImageViewProtocol adelegate) {
        frame = new AnalysisFrame();
        frame.addWindowListener(this);
        rect = arect;
        delegate = adelegate;

        update(astudy);
        
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        
    }
    
    /** The update method updates the histogram based on the study passed.
     * 
     * @param astudy 
     */
    @Override
    public void update(Study astudy) {
        AdaptiveHistogram h = new AdaptiveHistogram();
        float average = 0;
        int[] pixels = getPixelsInRect(toBufferedImage(((MedicalImage)(astudy.getElement(astudy.getSelectedIndex()))).loadImage().getImage()), rect);
        for (int a : pixels ) {
            average += a;
            h.addValue(a);
        }
        
        average /= pixels.length;
        
        
        frame.setHistogram(h.internalMap, average);
    }
    
    /** Returns an array of pixels in a specified rectangle of an image.
     * 
     * @param img   - image to grab pixels from
     * @param rect  - bounding box
     * @return int[] pixels - list of pixels
     */
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
    
    /** Helper class to convert an Image object to a BufferedImage
     * 
     * @param img
     * @return BufferedImage
     */
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

    @Override
    public void windowOpened(WindowEvent e) {    }

    @Override
    public void windowClosing(WindowEvent e) {    }

    /** 
     * The windowClosed event needs to notify the MainFrameController that it has been closed
     */
    @Override
    public void windowClosed(WindowEvent e) {
        delegate.analysisWindowClosed(rect, this);
    }

    @Override
    public void windowIconified(WindowEvent e) {    }

    @Override
    public void windowDeiconified(WindowEvent e) {    }

    @Override
    public void windowActivated(WindowEvent e) {    }

    @Override
    public void windowDeactivated(WindowEvent e) {    }
    
    
   
    
}
