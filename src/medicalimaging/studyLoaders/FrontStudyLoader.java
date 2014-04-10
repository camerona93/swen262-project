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
 * Generates a reconstructed along the y-axis
 * @author ericlee
 */
public class FrontStudyLoader extends ReconStudyLoader{

    /**
     * Constructor
     * @param _studyName the desired name of the generated study
     * @param _render 3D render of the study
     */
    public FrontStudyLoader(String _studyName, int[][][] _render) {
        super(_studyName, _render);
    }
    
    /**
     * Loads the study
     * @return Study the generated study.
     */
    public Study execute() {
        Study loadStudy = new Study(studyName);
        for(int z = 0; z < render[0][0].length; z++) {
            BufferedImage bfImage = new BufferedImage(render.length, render[0].length, BufferedImage.TYPE_INT_ARGB);
            for(int y = 0; y < render[0].length; y++) {
                int yPoint = (render[0].length - 1) - y;
                for(int x = 0; x < render.length; x++) {
                    bfImage.setRGB(x, y, render[x][yPoint][z]);
                }
            }
            MedicalImage image = new PreLoadedImage(new ImageIcon(bfImage));
            loadStudy.addElement(image);
        }
        loadStudy.setSelectedIndex(0);
        loadStudy.orientation = new int[]{0, 0, 1};
        loadStudy.setRender(render);
        loadStudy.studyLoader = this;
        return loadStudy;
    }
    
}
