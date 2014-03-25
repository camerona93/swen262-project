/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.studyLoaders;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.imageTypes.PreLoadedImage;
import medicalimaging.model.ImageReconUtils;
import medicalimaging.model.Study;

/**
 *
 * @author ericlee
 */
public class IntensityStudyLoader implements StudyLoader{

    private Color low;
    private Color high;
    private Study study;
    private String studyName;
    
    public IntensityStudyLoader(Study study, String studyName, int lowValue, int highValue) {
        this.study = study;
        this.studyName = studyName;
        low = new Color(lowValue, lowValue, lowValue);
        high = new Color(highValue, highValue, highValue);
    }
    
    @Override
    public Study execute() {
        Study returnStudy = new Study(studyName);
        
        for(int i = 0; i < study.getImageCount(); i++) {
            Image image = ((MedicalImage)study.getElement(i)).loadImage().getImage();
            BufferedImage bfImage = ImageReconUtils.getBufferedImageFromImage(image);
            BufferedImage outputImage = new BufferedImage(bfImage.getWidth(null), bfImage.getWidth(null), BufferedImage.TYPE_INT_ARGB);
            
            for(int y = 0; y < bfImage.getHeight() - 1; y++) {
                for(int x = 0; x < bfImage.getWidth() - 1; x++) {
                    int currentColor = bfImage.getRGB(x, y);
                    if(currentColor > high.getRGB())
                        outputImage.setRGB(x, y, Color.WHITE.getRGB());
                    else if(currentColor < low.getRGB())
                        outputImage.setRGB(x, y, Color.BLACK.getRGB());
                    else {
                        int newColor = generateScaledColor(new Color(currentColor, false).getBlue());
                        outputImage.setRGB(x, y, new Color(newColor, newColor, newColor).getRGB());
                    }
                }
            }
            
            MedicalImage newImage = new PreLoadedImage(new ImageIcon(outputImage));
            returnStudy.addElement(newImage);
        }
        
        returnStudy.setSelectedIndex(study.getSelectedIndex());
        returnStudy.studyLoader = this;
        return returnStudy;
    }

    @Override
    public void save(Study saveStudy) {
        
    }

    @Override
    public boolean copyStudy(Study copyStudy, String copyPath) {
        return true;
    }
    
    private int generateScaledColor(int color) {
        double mult = color / high.getBlue();
        return (int)mult * 255;
    }
    
}
