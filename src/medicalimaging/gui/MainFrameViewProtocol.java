/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import medicalimaging.model.StudyElement;

/**
 *
 * @author ericlee
 */
public interface MainFrameViewProtocol {
    void displayModeChanged(int displayMode);
    void loadStudyButtonPressed();
    void copyButtonPressed();
    void nextButtonPressed();
    void previousButtonPressed();
    void selectedImageChanged(StudyElement selectedElement);
    void mouseScrollOnImage(int magnitude, int indexLocation);
    void undoButtonPressed();
    void refreshKeyTyped();
}
