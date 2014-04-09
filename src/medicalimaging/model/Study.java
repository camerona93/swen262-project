/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.studyLoaders.StudyLoader;
import java.util.ArrayList;

/**
 * Captures all data associated with a study. 
 * @author ericlee
 */
public class Study extends StudyElement{
    private transient ArrayList<StudyElement> studyElements;
    private transient int indexOfFirstStudy;
    private transient int[][][] render;
    
    protected String name;
    protected int displayMode;
    protected int selectedIndex;
    protected transient ArrayList<StudyUndoableOperation> undoStack;
    
    public int[] orientation;
    public transient ArrayList<AnalysisListener> analysisListeners;
    public transient StudyLoader studyLoader;
    public transient ArrayList<Study> reconStudies;
    public transient ArrayList<Rectangle2D> selectionRects;
    public transient Study windowStudy;
    
    /**
     * Constructor
     * @param _name (String) desired name of a study.
     */
    public Study(String _name) {
        name = _name;
        studyElements = new ArrayList<StudyElement>();
        undoStack = new ArrayList<StudyUndoableOperation>();
        reconStudies = new ArrayList<Study>();
        selectionRects = new ArrayList<Rectangle2D>();
        analysisListeners = new ArrayList<AnalysisListener>();
        displayMode = 1;
        selectedIndex = 0;
        indexOfFirstStudy = 0;
        orientation = new int[]{0, 1, 0};
    }
    
    /**
     * Gets the number of elements in a study.
     * @return (int) number of elements
     */
    public int getElementCount() {
        return this.studyElements.size();
    }
    
    /**
     * Gets the element at an index
     * @param index (int) index of an element
     * @return (StudyElement) element at the index
     */
    public StudyElement getElement(int index) {
        if(index < getElementCount())
            return studyElements.get(index);
        return null;
    }
    
    /**
     * Removes an element at the index
     * @param index (int) index of element to remove
     */
    public void removeElement(int index) {
        if(index < this.getElementCount() && index > -1) {
            Object removedElement = this.studyElements.remove(index);
            if(!(removedElement instanceof Study))
                indexOfFirstStudy--;
            
        }
    }

    /**
     * Add element to the end of the study
     * @param element (StudyElement) element to add
     */
    public void addElement(StudyElement element) {
        if(element instanceof Study)
            studyElements.add(element);
        else {
            studyElements.add(indexOfFirstStudy, element);
            indexOfFirstStudy++;
        }
    }
    
    /**
     * Gets the total image count of the study
     * @return (int) number of images in the study
     */
    public int getImageCount() {
        int counter = 0;
        for(int i = 0; i < getElementCount(); i++) {
            StudyElement currentElement = getElement(i);
            if(currentElement instanceof MedicalImage)
                counter++;
        }
        return counter;
    }
    
    /**
     * Undos the last undoable operation
     */
    public void undoTask() {
        if(undoStack.size() > 0) {
            int lastIndex = undoStack.size() - 1;
            StudyUndoableOperation operation = undoStack.remove(lastIndex);
            operation.undo();
        }
    }
    
    /**
     * Gets the reference line for given study
     * @param study (Study) study to get reference line for.
     * @return (ReferenceLine)
     */
    public ReferenceLine getReferenceLineForStudy(Study study) {
        Point[] line = new Point[2];
        
        int[] referenceEq = new int[]{orientation[0], orientation[1], orientation[2], selectedIndex};
        int[] studyEq = new int[]{study.orientation[0], study.orientation[1], study.orientation[2], study.getSelectedIndex()};
        
        int[] vector = ImageReconUtils.calcCrossProduct(studyEq, referenceEq);
        
        int width = 0;
        int height = 0;
        
        
        //X
        if(Math.abs(vector[0]) == 1) {
            if(study.orientation[2] == 1) {
                width = render.length;
                height = render[0][0].length;
                line[0] = new Point(0, study.selectedIndex);
                line[1] = new Point(render[0][0].length, study.selectedIndex);
            }
            else {
                width = render.length;
                height = render[0].length;
                line[0] = new Point(0, height - study.selectedIndex);
                line[1] = new Point(render[0][0].length, height - study.selectedIndex);
            }
        }
        
        //Y
        else if(Math.abs(vector[1]) == 1) {
           if(study.orientation[2] == 1) {
               line[0] = new Point(study.selectedIndex, 0);
               line[1] = new Point(study.selectedIndex, render[0][0].length);
               width = render.length;
               height = render[0][0].length;
           }
           else if(study.orientation[0] == 1){
                width = render.length;
                height = render[0][0].length;
                line[0] = new Point(height - study.selectedIndex, 0);
                line[1] = new Point(height - study.selectedIndex, render.length);
           }
           else {
               line[0] = new Point(study.selectedIndex, 0);
               line[1] = new Point(study.selectedIndex, render.length);
               width = render.length;
               height = render[0].length;
           }
        }
        
        //Z
        else if(Math.abs(vector[2]) == 1) {
            if(orientation[0] == 0) {
                height = render[0][0].length;
                width = render.length;
                line[0] = new Point(study.selectedIndex, 0);
                line[1] = new Point(study.selectedIndex, render[0][0].length);
            }
            else if(orientation[1] == 0) {
                width = render[0][0].length;
                height = render[0].length;
                line[0] = new Point(0, height - study.selectedIndex);
                line[1] = new Point(render[0][0].length, height - study.selectedIndex);
            }
        }
        
        else {
            line[0] = new Point(0, 0);
            line[1] = new Point(0, 0);
        }
        
        return new ReferenceLine(line, width, height);
    }
    
    /**
     * Adds an undo operation to the stack
     * @param operation (StudyUndoableOperation) operation to add
     */
    public void addUndoTask(StudyUndoableOperation  operation) {
        undoStack.add(operation);
    }
    
    /**
     * Sets the selected index of a study
     * @param index (int) the index of the current selected item
     */
    public void setSelectedIndex(int index) {
        selectedIndex = index;
        
        //Notify listeners
       for ( AnalysisListener a : analysisListeners ) {
                a.update(this);
       }
    }
    
    /**
     * Gets the index of the current selected item
     * @return 
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    /**
     * Gets the current display mode
     * @return (int) display mode constant
     */
    public int getDisplayMode() {
        return displayMode;
    }
    
    /**
     * Set the current display mode of the study
     * @param displayMode (int) Display mode constant
     */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }
    
    /**
     * Get the 3D render for the current study
     * @return 3d array x, y, z
     */
    public int[][][] getRender() {
        if(render == null) {
            render = ImageReconUtils.generate3D(this);
        }
        return render;
    }
    
    /**
     * Set the render of the study
     * @param render 3d int array x, y, z
     */
    public void setRender(int[][][] render) {
        this.render = render;
    }
    
    public String toString() {
        return this.name;
    }
    
    //Display mode constants
    public static final int DISPLAY_MODE_1x1 = 1;
    public static final int DISPLAY_MODE_2x2 = 2;
    public static final int DISPLAY_MODE_RECON = 3;
    public static final int DISPLAY_MODE_INTEN = 4;
}
