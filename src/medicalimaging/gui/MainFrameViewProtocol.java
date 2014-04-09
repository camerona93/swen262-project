/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import medicalimaging.model.StudyElement;

/**
 * Protocol for MainFrameView Listeners.
 * @author ericlee
 */
public interface MainFrameViewProtocol {
    /**
     * Called when the displaymode is changed
     * @param displayMode (int) the new display mode constant. 
     */
    void displayModeChanged(int displayMode);
    
    /**
     * Called when the load study button is pressed.
     */
    void loadStudyButtonPressed();
    
    /**
     * Called when the copy button is pressed.
     */
    void copyButtonPressed();
    /**
     * Called when the next button is pressed.
     */
    void nextButtonPressed();
    
    /**
     * Called when the previous button is pressed
     */
    void previousButtonPressed();
    
    /**
     * Called when a a new value is selected in the JTree
     * @param selectedElement (StudyElement) new selected element
     */
    void selectedImageChanged(StudyElement selectedElement);
    
    /**
     * Occurs when the user scrolls on an image
     * @param magnitude (int) magnitude of scroll
     * @param indexLocation (int) index of image on the grid
     */
    void mouseScrollOnImage(int magnitude, int indexLocation);
    
    /**
     * Occurs when the undo button is pressed
     */
    void undoButtonPressed();
    
    /**
     * Occurs when the window bounds key bindings is pressed
     */
    void refreshKeyTyped();
}
