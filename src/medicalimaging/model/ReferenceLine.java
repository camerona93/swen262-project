/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import java.awt.Color;
import java.awt.Point;

/**
 * Reference line for a study
 * @author ericlee
 */
public class ReferenceLine {
    
    public Color color;
    
    protected Point[] points;
    protected int xRange;
    protected int yRange;
    
    /**
     * Constructor
     * @param points Start and end point
     * @param xRange The width of the total image
     * @param yRange The height of the total image
     */
    public ReferenceLine(Point[] points, int xRange, int yRange) {
        this.points = points;
        this.xRange = xRange;
        this.yRange = yRange;
    }
    
    /**
     * Scales the line to the given height and width
     * @param width (int) width to scale to
     * @param height (int) height to scale to
     * @return (Point[]) start and end point
     */
    public Point[] getScaledStartEnd(int width, int height) {
        Point[] returnPoints = new Point[points.length];
        
        double xMult = width / (double)xRange;
        double yMult = height / (double)yRange;
        
        for(int i = 0; i < points.length; i++) {
            int xVal = (int)(points[i].x * xMult);
            int yVal = (int)(points[i].y * yMult);
            
            returnPoints[i] = new Point(xVal, yVal);
        }
        
        return returnPoints;
    }
}
