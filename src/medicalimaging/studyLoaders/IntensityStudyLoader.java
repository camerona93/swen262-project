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
            
            Image outputImage = ImageReconUtils.windowImage(image, low.getBlue(), high.getBlue());
            
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
    
    public int getLowVal() {
        return low.getBlue();
    }
    
    public int getHighVal() {
        return high.getBlue();
    }
}
