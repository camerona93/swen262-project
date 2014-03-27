/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.model.ReferenceLine;

/**
 *
 * @author ericlee
 */
public class MedicalImageView extends JPanel{
    /**
     * Load images into the JPanel
     * @param loadImages ArrayList<MedicalImage> to load
     */
    
    private ArrayList<ArrayList<ReferenceLine>> referenceLines;
    private ArrayList<Image> images;
    private int gridSize;
    
    public MedicalImageView() {
        referenceLines = new ArrayList<ArrayList<ReferenceLine>>();
        images = new ArrayList<Image>();
        gridSize = 1;
    }
    
    protected void loadImages(ArrayList<MedicalImage> loadImages, ArrayList<ArrayList<ReferenceLine>> lines) {
        //this.clearImagePanel();
        /*this.removeAll();
        double  sqrt = Math.sqrt(loadImages.size());
        gridSize = (int)Math.ceil(sqrt);
        this.setLayout(new GridLayout(gridSize, gridSize));
        
        
        for(MedicalImage loadImage : loadImages) {
            int imageWidth = this.getWidth() / gridSize;
            int imageHeight = this.getHeight() / gridSize;
            ImageIcon tempIcon = loadImage.loadImage();
            Image image = tempIcon.getImage();
            Image scaledImage = image.getScaledInstance(imageWidth, imageHeight, 0);
            Icon imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon);
            this.add(imageLabel);
        }
        this.revalidate();
        this.repaint();*/
        
        double  sqrt = Math.sqrt(loadImages.size());
        gridSize = (int)Math.ceil(sqrt);
        
        images.clear();
        int imageWidth = this.getWidth() / gridSize;
        int imageHeight = this.getHeight() / gridSize;
        for(MedicalImage loadImage : loadImages) {
            images.add(loadImage.loadImage().getImage());
        }
        
        referenceLines.clear();
        referenceLines = lines;
        this.repaint();
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        int imageWidth = this.getWidth() / gridSize;
        int imageHeight = this.getHeight() / gridSize;
        for(int y = 0; y < gridSize; y++) {
            for(int x = 0; x < gridSize; x++) {
                int position = (y * gridSize) + x;
                //Draw image.
                if(position < images.size()) {
                    int xPos = x * imageWidth;
                    int yPos = y * imageHeight;
                    g2.drawImage(images.get(position), xPos, yPos, imageWidth, imageHeight, null);
                    
                    //Draw Image's lines
                    if(position < referenceLines.size()) {
                        ArrayList<ReferenceLine> lines = referenceLines.get(x);
                        for(int i = 0; i < lines.size(); i++) {
                            ReferenceLine line = lines.get(i);
                            Point[] points = line.getScaledStartEnd(imageWidth, imageHeight);
                            g2.setColor(Color.GREEN);
                            g2.drawLine(xPos + points[0].x, yPos + points[0].y, xPos + points[1].x, yPos + points[1].y);
                        }
                    }
                }
            }
        }
    }
    
    public int getGridIndexOfImageAt(int xPos, int yPos) {
        int imageWidth = this.getWidth() / gridSize;
        int imageHeight = this.getHeight() / gridSize;
        for(int y = 0; y < gridSize; y++) {
            for(int x = 0; x < gridSize; x++) {
                int xImgPos = x * imageWidth;
                int yImgPos = y * imageHeight;
                if((xPos >= xImgPos && xPos < xImgPos + imageWidth) && yPos >= yImgPos && yPos < yImgPos + imageHeight)
                    return (y * gridSize) + x;
            }
        }
        return -1;
    }
}
