/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import java.awt.Color;
import medicalimaging.imageTypes.MedicalImage;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author ericlee
 */
public class ImageReconUtils {
    public static int[][][] generate3D(Study study) {
        int imageCount = study.getImageCount();
        int height = 0;
        if(imageCount > 0)
            height = ((MedicalImage)study.getElement(0)).loadImage().getImage().getHeight(null) - 1;
        
        int[][][] reconImage = new int[height][imageCount][height]; 
        for(int i = study.getImageCount() - 1; i >= 0; i--) {
            Image image = ((MedicalImage)study.getElement(i)).loadImage().getImage();
            
            BufferedImage bfImage = getBufferedImageFromImage(image);
            
            //Cycle through image pixels
            for(int h = 0; h < bfImage.getHeight()-1; h++) {
                for(int w = 0; w < bfImage.getWidth()-1; w++) {
                    reconImage[w][i][h] = bfImage.getRGB(w, h);
                }
            }
        }
        
        return reconImage;
    }
    
    public static BufferedImage getBufferedImageFromImage(Image image) {
        BufferedImage bfImage = new BufferedImage(image.getHeight(null), image.getWidth(null), BufferedImage.TYPE_INT_ARGB);
            
        Graphics2D bGr = bfImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        
        return bfImage;
    }
    
    public static Image windowImage(Image image, int low, int high) {
        BufferedImage bfImage = ImageReconUtils.getBufferedImageFromImage(image);
            BufferedImage outputImage = new BufferedImage(bfImage.getWidth(null), bfImage.getWidth(null), BufferedImage.TYPE_INT_ARGB);
            
            for(int y = 0; y < bfImage.getHeight() - 1; y++) {
                for(int x = 0; x < bfImage.getWidth() - 1; x++) {
                    Color currentColor = new Color(bfImage.getRGB(x, y), false);
                    if(currentColor.getBlue() > high)
                        outputImage.setRGB(x, y, Color.WHITE.getRGB());
                    else if(currentColor.getBlue() < low)
                        outputImage.setRGB(x, y, Color.BLACK.getRGB());
                    else {
                        int newColor = generateWindowedScaledColor(currentColor.getBlue(), low, high);
                        outputImage.setRGB(x, y, new Color(newColor, newColor, newColor).getRGB());
                    }
                }
            }
            
            return outputImage;
    }
    
    public static int[] calcCrossProduct(int[] a, int[] b) {
        int[] returnVector = new int[3];
        
        returnVector[0] = (a[1] * b[2]) - (a[2] * b[1]);
        returnVector[1] = (a[2] * b[0]) - (a[0] * b[2]);
        returnVector[2] = (a[0] * b[1]) - (a[1] * b[0]);
        
        return returnVector;
    }
    
    /**
     * 
     * @param e1 index order a, b, c, d
     * @param e2
     * @return 
     */
    public static int[] solveSystemEquations(int[] e1, int[] e2) {
        int[] solution = new int[3];
        
        //Create individual value arrays from equations
        int[] a = new int[]{e1[0], e2[0]};
        int[] b = new int[]{e1[1], e2[1]};
        int[] c = new int[]{e1[2], e2[2]};
        int[] d = new int[]{e1[3], e2[3]};
        
        //Create matrices
        int[][] abcMatrix = {Arrays.copyOfRange(e1, 0, 3), Arrays.copyOfRange(e2, 0, 3)};
        int[][] xMatrix = new int[2][3];
        int[][] yMatrix = new int[2][3];
        int[][] zMatrix = new int[2][3];
        
        //Populate matrices
        for(int i = 0; i < xMatrix.length; i++) {
            xMatrix[i][0] = d[i];
            xMatrix[i][1] = b[i];
            xMatrix[i][2] = c[i];
            
            yMatrix[i][0] = a[i];
            yMatrix[i][1] = d[i];
            yMatrix[i][2] = c[i];
            
            zMatrix[i][0] = a[i];
            zMatrix[i][1] = b[i];
            zMatrix[i][2] = d[i];
        }
        
        //Compute values
        int delta = determinant(abcMatrix);
        solution[0] = determinant(xMatrix) / delta;
        solution[1] = determinant(yMatrix) / delta;
        solution[2] = determinant(zMatrix) / delta; 
        
        return solution;
    }
    
    /**
     * @param matrix
     * @return 
     * SOURCE: http://professorjava.weebly.com/matrix-determinant.html
     */
    private static int determinant(int[][] matrix) {
        int sum=0; 
        int s;
        if(matrix.length==1){ 
            return(matrix[0][0]);
        }
        
        for(int i=0;i<matrix.length;i++){ 
            int[][]smaller= new int[matrix.length-1][matrix.length-1]; 
            for(int a=1;a<matrix.length;a++){
                for(int b=0;b<matrix.length;b++){
                    if(b<i){
                        smaller[a-1][b]=matrix[a][b];
                    }
                    else if(b>i){
                        smaller[a-1][b-1]=matrix[a][b];
                    }
                }
            }
            
            if(i%2==0){ 
                s=1;
            }
            else{
                s=-1;
            }
            sum+=s*matrix[0][i]*(determinant(smaller)); 
        }
        
        return(sum); 
    }
    
    private static int generateWindowedScaledColor(int color, int low, int high) {
        double slope = 255 / (high - low);
        int returnValue =  (int)(slope * (color - low));
        return returnValue;
    }
    
}
