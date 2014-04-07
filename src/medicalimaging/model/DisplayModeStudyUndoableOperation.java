/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

/**
 *
 * @author ericlee
 */
public abstract class DisplayModeStudyUndoableOperation extends StudyUndoableOperation{
    protected final int currentMode;
    protected final int previousMode;
    
    public DisplayModeStudyUndoableOperation(Study _study, int changeState) {
        super(_study);
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
    
    @Override
    public abstract void onExecute();
    @Override
    public abstract void onUndo();
}
