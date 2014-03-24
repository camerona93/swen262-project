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
public class FrontStudyLoader extends ReconStudyLoader{

    public FrontStudyLoader(String _studyName, int[][][] _render) {
        super(_studyName, _render);
    }

    public Study execute() {
        Study loadStudy = new Study(studyName);
        for(int z = 0; z < render[0][0].length; z++) {
            BufferedImage bfImage = new BufferedImage(render.length, render[0].length, BufferedImage.TYPE_INT_ARGB);
            for(int y = 0; y < render[0].length; y++) {
                int yPoint = (render[0].length - 1) - y;
                for(int x = 0; x < render.length; x++) {
                    bfImage.setRGB(x, y, render[x][y][z]);
                }
            }
            MedicalImage image = new PreLoadedImage(new ImageIcon(bfImage));
            loadStudy.addElement(image);
        }
        loadStudy.selectedIndex = 0;
        return loadStudy;
    }
    
}
