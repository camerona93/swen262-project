/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

/**
 * Outlines all methods for an operation that can be undone.
 * @author ericlee
 */
public abstract class StudyUndoableOperation {
    protected Study study;
    
    /**
     * Constructor
     * @param study the study for the operation to manipulate 
     */
    public StudyUndoableOperation(Study study) {
        this.study = study;
    }
    
    /**
     * The operation to perform
     */
    abstract void execute();
    
    /**
     * The process for undoing the operation.
     */
    abstract void undo();
    
    /**
     * Called when the operation is executed.
     */
    abstract void onExecute();
    
    /**
     * Called when the operation is undone.
     */
    abstract void onUndo();
}
