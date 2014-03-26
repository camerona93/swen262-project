/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import medicalimaging.gui.MainFrameController;
import medicalimaging.model.ImageReconUtils;

/**
 *
 * @author ericlee
 */
public class MedicalImaging {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        MainFrameController controller = new MainFrameController();
        
        int[] e1 = {2, 3, 1, 2};
        int[] e2 = {-1, 2, 3, -1};
        int[] e3 = {-3, -3, 1, 0};
        
        int[] solution = ImageReconUtils.solveSystemEquations(e1, e2);
        
        for(int i = 0; i < solution.length; i++) {
            System.out.println(solution[i]);
        }
    }
    
}
