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
public abstract class StudyUndoableOperation {
    protected Study study;
    
    public StudyUndoableOperation(Study study) {
        this.study = study;
    }
    
    abstract void execute();
    abstract void undo();
    abstract void onExecute();
    abstract void onUndo();
}
