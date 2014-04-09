/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.studyLoaders;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.imageTypes.PreLoadedImage;
import medicalimaging.model.Study;

/**
 * A studyloader that loads a study with image progression along the x-axis
 * @author ericlee
 */
public class ProfileStudyLoader extends ReconStudyLoader{

    /**
     * Constructor
     * @param _studyName desired study name
     * @param render 3D render of a study
     */
    public ProfileStudyLoader(String _studyName, int[][][] render) {
        super(_studyName, render);
    }
    
    @Override
    public Study execute() {
        Study loadStudy = new Study(studyName);
        
        for(int i = 0; i < render.length; i++) {
            BufferedImage bfImage = new BufferedImage(render[0][0].length, render[0].length, BufferedImage.TYPE_INT_ARGB);
            for(int y = 0; y < render[0].length ; y++) {
                int yPoint = (render[0].length - 1) - y;
                for(int z = 0; z < render[0][0].length; z++) {
                    bfImage.setRGB(z, y, render[i][yPoint][z]);
                }
            }
            //Create and add medical image
            MedicalImage studyImage = new PreLoadedImage(new ImageIcon(bfImage));
            loadStudy.addElement(studyImage);
        }
        loadStudy.setSelectedIndex(0);
        loadStudy.orientation = new int[]{1, 0, 0};
        loadStudy.setRender(render);
        return loadStudy;
    }
}
