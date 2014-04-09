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
 * The view for displaying the MedivalImages
 * @author ericlee
 */
public class MedicalImageView extends JPanel implements MouseMotionListener, MouseListener{
    /**
     * Load images into the JPanel
     * @param loadImages ArrayList<MedicalImage> to load
     */
    
    private ArrayList<ArrayList<ReferenceLine>> referenceLines;
    private ArrayList<MedicalImageViewProtocol> selectionListeners;
    private ArrayList<Image> images;
    private int gridSize;
    
    private boolean dragging;
    private Point dragStickPoint;
    private Rectangle2D dragRect;
    private ArrayList<Rectangle2D> dragRectList;
    private final Color DRAGGING_BORDER_COLOR = new Color(105, 152, 255);
    private final Color DRAGGING_FILL_COLOR = new Color(105, 152, 255, 150);
    
    public MedicalImageView() {
        referenceLines = new ArrayList<ArrayList<ReferenceLine>>();
        images = new ArrayList<Image>();
        selectionListeners = new ArrayList<MedicalImageViewProtocol>();
        gridSize = 1;
        dragging = false;
        dragRectList = new ArrayList<Rectangle2D>();
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    /**
     * Images to display
     * @param loadImages (ArrayList<MedicalImage>) images to display
     * @param lines (ArrayList<ArrayList<ReferenceLine>>) List of lines to draw on
     * each image
     */
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
    
    /**
     * Custom override of the paint method
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Draw images
        for(int y = 0; y < gridSize; y++) {
            for(int x = 0; x < gridSize; x++) {
                int position = (y * gridSize) + x;
                this.drawImage(position, x, y, g2);
            }
        }
        //Draw Dragging rectangle
        if(dragging) {
            drawDragRect(dragRect, g2);
        }
        //Draw Previous drag rects
        for(int i = 0; i < dragRectList.size(); i++) {
            drawDragRect(dragRectList.get(i), g2);
        }
    }
    
    /**
     * Gets the index of the image on the image grid at a given pixel point
     * @param xPos (int) x coordinate
     * @param yPos (int) y coordinate
     * @return (int) index of image
     */
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
    
    /**
     * Occurs when mouse is dragging
     * @param e 
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if(images.size() > 0) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int rectX = dragStickPoint.x;
            int rectY = dragStickPoint.y;
            int imageIndex = getGridIndexOfImageAt(dragStickPoint.x, dragStickPoint.y);

            //Check bounds of image
            Point imagePoint = getLocationOfImageIndex(imageIndex);
            if(mouseX < imagePoint.x)
                mouseX = imagePoint.x;
            else if(mouseX > imagePoint.x + getImageWidth())
                mouseX = imagePoint.x + getImageWidth();

            if(mouseY < imagePoint.y)
                mouseY = imagePoint.y;
            else if(mouseY > imagePoint.y + getImageHeight())
                mouseY = imagePoint.y + getImageHeight();

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
    }

    /**
     * Occurs when mouse is pressed down
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(images.size() > 0) {
            int x = e.getX();
            int y = e.getY();
        
            dragRect = new Rectangle2D.Float(x, y, 0, 0);
            dragStickPoint = new Point(x, y);
            dragging = true;
        
            repaint();
        }
    }
    
    /**
     * Occurs when mouse button is released
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(images.size() > 0) {
            dragRectList.add(dragRect);
            dragging = false;
            
            int imagePos = this.getGridIndexOfImageAt(dragStickPoint.x, dragStickPoint.y);
            Rectangle2D scaledRect = scaleRectToModel(dragRect, imagePos);
            this.notifySelectionListeners(imagePos, scaledRect);
        }
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
    
    /**
     * Clears all images from view
     */
    public void clearImageSelection() {
        dragRectList.clear();
        repaint();
    }
    
    /**
     * Draw the given selection rects
     * @param rects (ArrayList<ArrayList<Rectangle2D>> list of rects for each image index
     * Each rect can be scaled to the model and will upscale or down scale based
     * on the size of this panel.
     */
    public void setSelectionRects(ArrayList<ArrayList<Rectangle2D>> rects) {
        dragRectList.clear();
        for(int i = 0; i < rects.size(); i++) {
            ArrayList<Rectangle2D> imageRects = rects.get(i);
            for(int k = 0; k < imageRects.size(); k++) {
                Rectangle2D rect = imageRects.get(k);
                if(i < images.size())
                    dragRectList.add(this.scaleRectToView(rect, i));
            }
        }
        repaint();
    }
    
    /**
     * Add listener
     * @param observer 
     */
    public void addSelectionListener(MedicalImageViewProtocol observer) {
        selectionListeners.add(observer);
    }
    
    /**
     * Remove a listener
     * @param observer 
     */
    public void removeSelectionListener(MedicalImageViewProtocol observer) {
        selectionListeners.remove(observer);
    }
    
    /**
     * Notify all listeners of a rect selection 
     * @param imageIndex (int) index of image selected
     * @param rect (Rectangle2D) bounds of rect. This will scale it to the model.
     */
    private void notifySelectionListeners(int imageIndex, Rectangle2D rect) {
        for(int i = 0; i < selectionListeners.size(); i++) {
            selectionListeners.get(i).rectSelected(imageIndex, rect);
        }
    } 
    
    /**
     * Gets the color identifier for each grid index
     * @param index (int) index of image on the grid
     * @return (Color) color corresponding to the index 
     */
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
    
    /**
     * Gets the coordinates of the upper left corner of an image by index
     * @param index (int) grid index of image
     * @return (Point) coordinates of image
     */
    private Point getLocationOfImageIndex(int index) {
        int imageWidth = getImageWidth();
        int imageHeight = getImageHeight();
        
        int yPos = index / gridSize;
        int xPos = index % gridSize;
        
        return new Point(xPos * imageWidth, yPos * imageHeight);
    }
    
    /**
     * Gets the current image width
     * @return (int) current image width
     */
    private int getImageWidth() {
        return this.getWidth() / gridSize;
    }
    
    /**
     * Gets the current image height
     * @return (int) current image height
     */
    private int getImageHeight() {
        return this.getHeight() / gridSize;
    }
    
    /**
     * Check the color bounds (if the color value is 0-255)
     * @param colorVal (int) color value to check
     * @return (int) adjusted color value
     */
    private int checkColorBounds(int colorVal) {
        if(colorVal > 255)
            return 255;
        else if(colorVal < 0)
            return 0;
        return colorVal;
    }
    
    /**
     * Draws a drag rect on the screen.
     * @param rect (Rectangle2D) rect to draw
     * @param g2 (Graphics2D) graphics to draw with
     */
    private void drawDragRect(Rectangle2D rect, Graphics2D g2) {
        g2.setColor(DRAGGING_BORDER_COLOR);
            g2.draw(rect);
            int rectX = (int)rect.getX();
            int rectY = (int)rect.getY();
            
            Rectangle2D fillRect = new Rectangle2D.Float(rectX + 1, rectY + 1, (int)rect.getWidth() - 1, (int)rect.getHeight() - 1);
            g2.setColor(DRAGGING_FILL_COLOR);
            g2.fill(fillRect);
    }
    
    /**
     * Draws an image on the screen
     * @param imageIndex (int) index of image on the grid
     * @param gridX (int) x coordinate of upper left most corner of the image
     * @param gridY (int) y coordinate of upper left most corner of the image
     * @param g2 (Graphics2D) graphics to draw with
     */
    private void drawImage(int imageIndex, int gridX, int gridY, Graphics2D g2) {
        int imageWidth = this.getWidth() / gridSize;
        int imageHeight = this.getHeight() / gridSize;
        if(imageIndex < images.size()) {
            int xPos = gridX * imageWidth;
            int yPos = gridY * imageHeight;
            g2.drawImage(images.get(imageIndex), xPos, yPos, imageWidth, imageHeight, null);

            //Draw images reference boxes
            g2.setColor(getColorForImageIndex(imageIndex));
            int boxHeight = (int)(imageHeight * .05);
            g2.fillRect(xPos, yPos, boxHeight, boxHeight);

            //Draw Image's lines
            if(imageIndex < referenceLines.size()) {
                ArrayList<ReferenceLine> lines = referenceLines.get(imageIndex);
                for(int i = 0; i < lines.size(); i++) {
                    ReferenceLine line = lines.get(i);
                    Point[] points = line.getScaledStartEnd(imageWidth, imageHeight);
                    g2.setColor(getColorForImageIndex(i));
                    g2.drawLine(xPos + points[0].x, yPos + points[0].y, xPos + points[1].x, yPos + points[1].y);
                }
            }
        }
    }
    
    /**
     * Scales the given rect to the model image's dimensions.
     * @param rect (Rectangle2D) rect to scale
     * @param referenceImageIndex (int) index of a model's reference image
     * @return (Rectangle2D) scaled rect
     */
    private Rectangle2D scaleRectToModel(Rectangle2D rect, int referenceImageIndex) {
        int imageXPos = referenceImageIndex % gridSize;
        int imageYPos = referenceImageIndex / gridSize;
        double xMult = images.get(referenceImageIndex).getWidth(null) / (double)getImageWidth();
        double yMult = images.get(referenceImageIndex).getHeight(null) / (double)getImageHeight();

        double rectX = (rect.getX() - (imageXPos * getImageWidth())) * xMult;
        double rectY = (rect.getY() - (imageYPos * getImageHeight())) * yMult;
        double rectWidth = rect.getWidth() * xMult;
        double rectHeight = rect.getHeight() * yMult;

        return new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
    }
    
    /**
     * Scales a given rect to this panel dimensions.
     * @param rect (Rectangle2D) rect to scale
     * @param referenceImageIndex (int) index of reference image
     * @return (Rectangle2D) scaled rect
     */
    private Rectangle2D scaleRectToView(Rectangle2D rect, int referenceImageIndex) {
        int imageXPos = referenceImageIndex % 2;
        int imageYPos = referenceImageIndex / gridSize;
        double xMult = (double)getImageWidth() / images.get(referenceImageIndex).getWidth(null);
        double yMult = (double)getImageHeight() / images.get(referenceImageIndex).getHeight(null);
        
        double rectX = (rect.getX() * xMult) + (imageXPos * getImageWidth());
        double rectY = (rect.getY() * yMult) + (imageYPos * getImageHeight());
        double rectWidth = rect.getWidth() * xMult;
        double rectHeight = rect.getHeight() * yMult;
        
        return new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
    }
}
