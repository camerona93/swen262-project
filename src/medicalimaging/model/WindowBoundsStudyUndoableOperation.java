/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import medicalimaging.studyLoaders.IntensityStudyLoader;

/**
 *
 * @author ericlee
 */
public abstract class WindowBoundsStudyUndoableOperation extends StudyUndoableOperation{
    private int newHigh;
    private int newLow;
    private int prevHigh;
    private int prevLow;
    
    public WindowBoundsStudyUndoableOperation(Study _study, int _high, int _low) {
        super(_study);
        
        newHigh = _high;
        newLow = _low;
        
        if(study.windowStudy != null) {
            prevHigh = ((IntensityStudyLoader)study.windowStudy.studyLoader).getHighVal();
            prevLow = ((IntensityStudyLoader)study.windowStudy.studyLoader).getHighVal();
        }
        else {
            prevHigh = newHigh;
            prevLow = newLow;
        }
    }
    
    @Override
    public void execute() {
        Study newStudy = new IntensityStudyLoader(study, "Window Mode", newLow, newHigh).execute();
        study.windowStudy = newStudy;
        onExecute();
    }
    
    @Override
    public void undo() {
        Study newStudy = new IntensityStudyLoader(study, "Window Mode", prevLow, prevHigh).execute();
        study.windowStudy = newStudy;
        onUndo();
    }
    
    @Override
    public abstract void onExecute();
    @Override
    public abstract void onUndo();
}
