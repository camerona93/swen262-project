/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

/**
 *
 * @author ericlee
 */
abstract class DisplayModeStudyUndoableOperation implements StudyUndoableOperation{
    private Study study;
    protected final int currentMode;
    protected final int previousMode;
    
    public DisplayModeStudyUndoableOperation(Study _study, int changeState) {
        study = _study;
        currentMode = changeState;
        previousMode = study.displayMode;
    }
    
    public void execute() {
        study.displayMode = currentMode;
        onExecute();
    }
    
    public void undo() {
        study.displayMode = previousMode;
        onUndo();
    }
    
    abstract void onExecute();
    abstract void onUndo();
}
