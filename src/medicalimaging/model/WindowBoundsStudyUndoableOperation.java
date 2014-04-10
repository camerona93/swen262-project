/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import medicalimaging.studyLoaders.IntensityStudyLoader;

/**
 * Undoable Operation for setting/changing the window bounds.
 * @author ericlee
 */
public abstract class WindowBoundsStudyUndoableOperation extends StudyUndoableOperation{
    private int newHigh;
    private int newLow;
    private int prevHigh;
    private int prevLow;
    
    /**
     * Constructor
     * @param _study Study the study the bounds is changing on
     * @param _high int the high window value
     * @param _low int the low window value
     */
    public WindowBoundsStudyUndoableOperation(Study _study, int _high, int _low) {
        super(_study);
        
        newHigh = _high;
        newLow = _low;
        
        if(study.windowStudy != null) {
            prevHigh = ((IntensityStudyLoader)study.windowStudy.studyLoader).getHighVal();
            prevLow = ((IntensityStudyLoader)study.windowStudy.studyLoader).getLowVal();
        }
        else {
            prevHigh = newHigh;
            prevLow = newLow;
        }
    }
    
    /**
     * Performs the window bounds change
     */
    @Override
    public void execute() {
        Study newStudy = new IntensityStudyLoader(study, "Window Mode", newLow, newHigh).execute();
        study.windowStudy = newStudy;
        onExecute();
    }
    
    /**
     * Undos the window bounds change
     */
    @Override
    public void undo() {
        Study newStudy = new IntensityStudyLoader(study, "Window Mode", prevLow, prevHigh).execute();
        study.windowStudy = newStudy;
        onUndo();
    }
    
    /**
     * Performed when the operation is executed.
     */
    @Override
    public abstract void onExecute();
    
    /**
     * Performed when the operation is undone.
     */
    @Override
    public abstract void onUndo();
}
