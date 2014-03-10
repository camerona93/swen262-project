/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;
import java.util.ArrayList;

/**
 *
 * @author ericlee
 */
public class Study extends StudyElement{
    private transient ArrayList<StudyElement> studyElements;
    protected String name;
    protected int displayMode;
    protected int selectedIndex;
    protected transient StudyLoader studyLoader;
    
    public static final int DISPLAY_MODE_1x1 = 1;
    public static final int DISPLAY_MODE_2x2 = 2;
    
    public Study(String name) {
        this.name = name;
        this.studyElements = new ArrayList<StudyElement>();
        this.displayMode = 1;
        this.selectedIndex = -1;
    }
    
    public int getElementCount() {
        return this.studyElements.size();
    }
    
    public StudyElement getElement(int index) {
        if(index < this.getElementCount())
            return this.studyElements.get(index);
        return null;
    }
    
    public void removeElement(int index) {
        if(index < this.getElementCount() && index > -1)
            this.studyElements.remove(index);
    }

    public void addElement(StudyElement element) {
        this.studyElements.add(element);
    }
    
    public String toString() {
        return this.name;
    }
}
