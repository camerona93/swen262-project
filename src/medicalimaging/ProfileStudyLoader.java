/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author ericlee
 */
public class ProfileStudyLoader extends ReconStudyLoader{

    public ProfileStudyLoader(String _studyName, int[][][] _render) {
        super(_studyName, _render);
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
        loadStudy.selectedIndex = 0;
        return loadStudy;
    }
}
