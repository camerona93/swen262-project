/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.studyLoaders.StudyLoader;
import java.util.ArrayList;

/**
 *
 * @author ericlee
 */
public class Study extends StudyElement{
    private transient ArrayList<StudyElement> studyElements;
    private transient int indexOfFirstStudy;
    
    protected String name;
    protected int displayMode;
    protected int selectedIndex;
    protected transient ArrayList<StudyUndoableOperation> undoStack;
    public transient StudyLoader studyLoader;
    public transient ArrayList<Study> reconStudies;
    
    public static final int DISPLAY_MODE_1x1 = 1;
    public static final int DISPLAY_MODE_2x2 = 2;
    public static final int DISPLAY_MODE_RECON = 3;
    
    public Study(String _name) {
        name = _name;
        studyElements = new ArrayList<StudyElement>();
        undoStack = new ArrayList<StudyUndoableOperation>();
        reconStudies = new ArrayList<Study>();
        displayMode = 1;
        selectedIndex = -1;
        indexOfFirstStudy = 0;
    }
    
    public int getElementCount() {
        return this.studyElements.size();
    }
    
    public StudyElement getElement(int index) {
        if(index < getElementCount())
            return studyElements.get(index);
        return null;
    }
    
    public void removeElement(int index) {
        if(index < this.getElementCount() && index > -1) {
            Object removedElement = this.studyElements.remove(index);
            if(!(removedElement instanceof Study))
                indexOfFirstStudy--;
            
        }
    }

    public void addElement(StudyElement element) {
        if(element instanceof Study)
            studyElements.add(element);
        else {
            studyElements.add(indexOfFirstStudy, element);
            indexOfFirstStudy++;
        }
    }
    
    public int getImageCount() {
        int counter = 0;
        for(int i = 0; i < getElementCount(); i++) {
            StudyElement currentElement = getElement(i);
            if(currentElement instanceof MedicalImage)
                counter++;
        }
        return counter;
    }
    
    public void undoTask() {
        if(undoStack.size() > 0) {
            StudyUndoableOperation operation = undoStack.remove(0);
            operation.undo();
        }
    }
    
    public void addUndoTask(StudyUndoableOperation  operation) {
        undoStack.add(operation);
    }
    
    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public int getDisplayMode() {
        return displayMode;
    }
    
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }
    
    public String toString() {
        return this.name;
    }
}
