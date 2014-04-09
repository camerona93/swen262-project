/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.model;

import medicalimaging.imageTypes.MedicalImage;
import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Model of the study for the JTree
 * @author ericlee
 */
public class StudyTreeModel implements TreeModel{
    private Study rootStudy;
    private ArrayList<TreeModelListener> listeners;
    
    /**
     * Constructor
     * @param root Study that represents the root of the tree. 
     */
    public StudyTreeModel(Study root) {
        this.rootStudy = root;
        listeners = new ArrayList<TreeModelListener>();
    }

    /**
     * Gets the root of the tree
     * @return (Study) the root study
     */
    @Override
    public Object getRoot() {
        return this.rootStudy;
    }

    /**
     * Gets the child of the parent at the index
     * @param parent (Study) 
     * @param index index of child
     * @return (StudyElement) the child at the index
     */
    @Override
    public Object getChild(Object parent, int index) {
        if(parent instanceof Study) {
            Study studyParent = (Study)parent;
            return studyParent.getElement(index);
        }
        return null;
    }

    /**
     * Counts the children for a parent
     * @param parent (StudyElement) parent to get the count for.
     * @return (int) the count of children
     */
    @Override
    public int getChildCount(Object parent) {
        if(parent instanceof Study) {
            Study studyParent = (Study)parent;
            return studyParent.getElementCount();
        }
        return 0;
    }

    /**
     * Checks if StudyElement is a leaf of the tree.
     * @param node (StudyElement) the object to check
     * @return (boolean) True is leaf false if not
     */
    @Override
    public boolean isLeaf(Object node) {
        return node instanceof MedicalImage;
    }

    /**
     * Called when the the tree changes
     * @param path
     * @param newValue 
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        TreeModelEvent event = new TreeModelEvent(newValue, path);
        for (TreeModelListener listener : listeners) {
            listener.treeStructureChanged(event);
        }
    }

    /**
     * Gets the index of the child within the parent
     * @param parent (StudyElement) parent to search
     * @param child (StudyElement) child to find
     * @return (int) the index of the child
     */
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
    
    /**
     * Sets the root of the tree.
     * @param study (Study) the study to set as the root.
     */
    public void setRootStudy(Study study) {
        this.rootStudy = study;
        this.valueForPathChanged(new TreePath(this.rootStudy), this.rootStudy);
    }
    
    /**
     * Gets the parent of the given element
     * @param child (StudyElement) Element to find
     * @param startElement (Study) Study to begin the search
     * @return 
     */
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
    
    /**
     * Gets the total number of rows for an element
     * @param start (Study) element to get rows for.
     * @return 
     */
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
    
    /**
     * Gets the row number of an element
     * @param element (StudyElement) element to get row of
     * @param start (StudyElement) element to start the search
     * @return 
     */
    public int getRowOfElement(Object element, Study start) {
        int rowCounter = 0;
        
        //Check if it root.
        if(element == start)
            return 0;
        
        for(int i = 0; i < start.getElementCount(); i++) {
            Object currentElement = start.getElement(i);
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
}
