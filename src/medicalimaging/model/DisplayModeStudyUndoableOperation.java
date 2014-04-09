/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

/**
 * Operation for performing and undoing a display change
 * @author ericlee
 */
public abstract class DisplayModeStudyUndoableOperation extends StudyUndoableOperation{
    protected final int currentMode;
    protected final int previousMode;
    
    /**
     * Constructor
     * @param _study (Study) study to perform the operation on
     * @param changeState (int) display state constant to change to
     */
    public DisplayModeStudyUndoableOperation(Study _study, int changeState) {
        super(_study);
        currentMode = changeState;
        previousMode = study.displayMode;
    }
    
    /**
     * Performs the display change
     */
    public void execute() {
        study.displayMode = currentMode;
        onExecute();
    }
    
    /**
     * Reverts the display change
     */
    public void undo() {
        study.displayMode = previousMode;
        onUndo();
    }
    
    /**
     * Performed when the operation is executed
     */
    @Override
    public abstract void onExecute();
    
    /**
     * Performed when the operation is undone
     */
    @Override
    public abstract void onUndo();
}
