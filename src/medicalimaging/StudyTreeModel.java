/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author ericlee
 */
public class StudyTreeModel implements TreeModel{
    private Study rootStudy;
    private ArrayList<TreeModelListener> listeners;
    
    public StudyTreeModel(Study root) {
        this.rootStudy = root;
        listeners = new ArrayList<TreeModelListener>();
    }

    @Override
    public Object getRoot() {
        return this.rootStudy;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if(parent instanceof Study) {
            Study studyParent = (Study)parent;
            return studyParent.getElement(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if(parent instanceof Study) {
            Study studyParent = (Study)parent;
            return studyParent.getElementCount();
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof MedicalImage;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        TreeModelEvent event = new TreeModelEvent(newValue, path);
        for (TreeModelListener listener : listeners) {
            listener.treeStructureChanged(event);
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(parent instanceof Study) {
            Study parentStudy = (Study)parent;
            for(int i = 0; i < parentStudy.getElementCount(); i++) {
                if(child == parentStudy.getElement(i))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        if (l != null && !listeners.contains(l)) {
            listeners.add(l);
        }
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        if (l != null) {
            listeners.remove(l);
        }
    }
    
    public void setRootStudy(Study study) {
        this.rootStudy = study;
        this.valueForPathChanged(new TreePath(this.rootStudy), this.rootStudy);
    }
    
    public Object getParent(StudyElement child, Study startElement) {
        for(int i = 0; i < startElement.getElementCount(); i++) {
            StudyElement currentElement = startElement.getElement(i);
            if(currentElement instanceof Study) {
                Object returnElement = this.getParent(child, (Study)currentElement);
                if(returnElement != null)
                    return returnElement;
            }
            else {
                if(currentElement == child)
                    return startElement;
            }
        }
        return null;
    }
    
    public int getNumRowsForElement(Study start) {
        int rowCounter = 0;
        for(int i = 0; i < start.getElementCount(); i++) {
            Object currentElement = start.getElement(i);
            rowCounter++;
            if(!this.isLeaf(currentElement)) {
                rowCounter += this.getNumRowsForElement((Study)currentElement);
            }
        }
        return rowCounter; 
    }
    
    public int getRowOfElement(Object element, Study start) {
        int rowCounter = 0;
        
        //Check if it root.
        if(element == start)
            return 0;
        
        for(int i = 0; i < start.getElementCount(); i++) {
            Object currentElement = start.getElement(i);
            System.out.println(currentElement.toString());
            rowCounter++;
            if(currentElement == element)
                return rowCounter;
            else if(!this.isLeaf(currentElement)) {
                int checkSubStudy = this.getRowOfElement(element, (Study)currentElement);
                if(checkSubStudy != -1)
                    return checkSubStudy;
                else
                    rowCounter += this.getNumRowsForElement((Study) currentElement);
            }
        }
        return -1;
    }
    
    public int getImageCountForParent(Study study) {
        int counter = 0;
        for(int i = 0; i < this.getChildCount(study); i++) {
            Object currentElement = this.getChild(study, i);
            if(this.isLeaf(currentElement))
                counter++;
        }
        return counter;
    }
}
