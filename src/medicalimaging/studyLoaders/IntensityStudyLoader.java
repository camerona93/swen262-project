/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.studyLoaders;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.imageTypes.PreLoadedImage;
import medicalimaging.model.ImageReconUtils;
import medicalimaging.model.Study;

/**
 * Loads a study with images manipulated to window bounds.
 * @author ericlee
 */
public class IntensityStudyLoader implements StudyLoader{

    private Color low;
    private Color high;
    private Study study;
    private String studyName;
    
    /**
     * Constructor
     * @param study Base Study to window images
     * @param studyName Name for generated study
     * @param lowValue Low window value
     * @param highValue High window value
     */
    public IntensityStudyLoader(Study study, String studyName, int lowValue, int highValue) {
        this.study = study;
        this.studyName = studyName;
        low = new Color(lowValue, lowValue, lowValue);
        high = new Color(highValue, highValue, highValue);
    }
    
    @Override
    /**
     * Loads the study
     * @return Study the generated study.
     */
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
    /**
     * Saves the given study. No functionality. 
     */
    public void save(Study saveStudy) {
        
    }

    /**
     * Copys the study.
     * No functionality.
     * @param copyStudy
     * @param copyPath
     * @return 
     */
    @Override
    public boolean copyStudy(Study copyStudy, String copyPath) {
        return true;
    }
    
    /**
     * Gets the low window color value(0-255) for the generated study
     * @return int color value 
     */
    public int getLowVal() {
        return low.getBlue();
    }
    
    /**
     * Gets the high window color value(0-255) for generated study.
     * @return 
     */
    public int getHighVal() {
        return high.getBlue();
    }
}
