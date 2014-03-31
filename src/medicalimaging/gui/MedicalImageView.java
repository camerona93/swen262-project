/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.model.ReferenceLine;

/**
 *
 * @author ericlee
 */
public class MedicalImageView extends JPanel implements MouseMotionListener, MouseListener{
    /**
     * Load images into the JPanel
     * @param loadImages ArrayList<MedicalImage> to load
     */
    
    private ArrayList<ArrayList<ReferenceLine>> referenceLines;
    private ArrayList<Image> images;
    private int gridSize;
    
    private boolean dragging;
    private Point dragStickPoint;
    private Rectangle2D dragRect;
    private final Color DRAGGING_BORDER_COLOR = new Color(105, 152, 255);
    
    public MedicalImageView() {
        referenceLines = new ArrayList<ArrayList<ReferenceLine>>();
        images = new ArrayList<Image>();
        gridSize = 1;
        dragging = false;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    protected void loadImages(ArrayList<MedicalImage> loadImages, ArrayList<ArrayList<ReferenceLine>> lines) {
        
        double  sqrt = Math.sqrt(loadImages.size());
        gridSize = (int)Math.ceil(sqrt);
        
        images.clear();
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
                    
                    //Draw images reference boxes
                    g2.setColor(getColorForImageIndex(position));
                    int boxHeight = (int)(imageHeight * .05);
                    g2.fillRect(xPos, yPos, boxHeight, boxHeight);
                    
                    //Draw Image's lines
                    if(position < referenceLines.size()) {
                        ArrayList<ReferenceLine> lines = referenceLines.get(position);
                        for(int i = 0; i < lines.size(); i++) {
                            ReferenceLine line = lines.get(i);
                            Point[] points = line.getScaledStartEnd(imageWidth, imageHeight);
                            g2.setColor(getColorForImageIndex(i));
                            g2.drawLine(xPos + points[0].x, yPos + points[0].y, xPos + points[1].x, yPos + points[1].y);
                        }
                    }
                }
            }
        }
        
        //Draw Dragging rectangle
        if(dragging) {
            g2.setColor(DRAGGING_BORDER_COLOR);
            g2.draw(dragRect);
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
    
    private Color getColorForImageIndex(int index) {
        int colorCode = index % 5;
        switch(colorCode) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.RED;
            case 3:
                return Color.CYAN;
            case 4:
                return Color.MAGENTA;
            default:
                return Color.GREEN;
        }
    }
        
    @Override
    public void mouseDragged(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int rectX = dragStickPoint.x;
        int rectY = dragStickPoint.y;
        
        int width = mouseX - rectX;
        int height = mouseY - rectY;
        
        if(width < 0) {
            width = Math.abs(width);
            rectX -= width;
        }
        
        if(height < 0) {
            height = Math.abs(height);
            rectY -= height;
        }
        
        dragRect.setFrame(rectX, rectY, width, height);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        dragRect = new Rectangle2D.Float(x, y, 0, 0);
        dragStickPoint = new Point(x, y);
        dragging = true;
        
        repaint();
    }
    

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
