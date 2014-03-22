/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.tree.TreePath;

/**
 *
 * @author ericlee
 */
public class MainFrameController implements MainFrameViewProtocol{
    private StudyTreeModel treeModel;
    private JFileChooser fileChooser = new JFileChooser();
    private MainFrame view;
    
    public MainFrameController() {
        view = new MainFrame();
        view.delegate = this;
        
        treeModel = new StudyTreeModel(new Study("Study"));
        view.setTreeModel(treeModel);
        view.setVisible(true);
        
        //Add window close listener
        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(getCurrentStudy().studyLoader != null)
                    saveStudy();
            }
        });
    }
    
    @Override
    public void displayModeButtonPressed() {
        Study currentStudy = getCurrentStudy();
        if(currentStudy.displayMode == Study.DISPLAY_MODE_1x1) {
            view.displayModeButton.setText("1x1");
            currentStudy.displayMode = Study.DISPLAY_MODE_2x2;
        }
        else {
            view.displayModeButton.setText("2x2");
            currentStudy.displayMode = Study.DISPLAY_MODE_1x1;
        }
        view.refreshImages();
    }

    @Override
    public void loadStudyButtonPressed() {
        loadStudy();
    }

    @Override
    public void copyButtonPressed() {
        copyCurrentStudy();
    }

    @Override
    public void nextButtonPressed() {
        selectNextElement();
    }

    @Override
    public void previousButtonPressed() {
        selectPreviousElement();
    }
    
    @Override
    public void selectedImageChanged(StudyElement selectedElement) {
        Study currentStudy = getCurrentStudy();
        if(selectedElement instanceof Study) {
            this.selectFirstElement(currentStudy);
        }
        else {
            MedicalImage selectedImage = (MedicalImage)selectedElement;
            if(selectedImage == null){
                selectedImage = (MedicalImage)selectCurrentStudySavedIndex();
            }
            
            //Save selected index
            int selectedIndex = this.treeModel.getIndexOfChild(currentStudy, selectedImage);
            currentStudy.selectedIndex = selectedIndex;
            
            //Create list of images to display
            ArrayList<MedicalImage> loadImages = new ArrayList<MedicalImage>();
            if(currentStudy.displayMode == Study.DISPLAY_MODE_1x1)
                loadImages.add(selectedImage);
            else if(currentStudy.displayMode == Study.DISPLAY_MODE_2x2) {
                int childCount = this.treeModel.getImageCountForParent(currentStudy);
                System.out.println(childCount);
                if(selectedIndex > -1) {
                    if(childCount <= 4) {
                        for(int i = 0; i < childCount; i++) {
                            loadImages.add((MedicalImage)this.treeModel.getChild(currentStudy, i));
                        }
                    }
                    else if(selectedIndex >= childCount - 4) {
                        for(int i = childCount - 4; i < childCount; i++) {
                            loadImages.add((MedicalImage)this.treeModel.getChild(currentStudy, i));
                        }
                    }
                    else {
                        for(int i = selectedIndex; i < selectedIndex + 4; i++) {
                            loadImages.add((MedicalImage)this.treeModel.getChild(currentStudy, i));
                        }
                    }
                }
            }
            view.loadImages(loadImages);
        }
    }
    
    @Override
    public void mouseScrollOnImage(int magnitude, int indexLocation) {
        if(magnitude > 0)
            selectNextElement();
        else
            selectPreviousElement();
    }
    
    /**
     * Selects the first element of a given study
     * @param parent
     * @return 
     */
    private Object selectFirstElement(Study parent) {
        System.out.println("Find: " + parent.toString());
        int parentStartIndex = treeModel.getRowOfElement(parent, (Study)treeModel.getRoot());
        System.out.println(parentStartIndex);
        if(parentStartIndex >= 0) {
            for(int i = 0; i < parent.getElementCount(); i++) {
                StudyElement currentElement = parent.getElement(i);
                if(treeModel.isLeaf(currentElement)) {
                    System.out.println(parentStartIndex + i + 1);
                    TreePath firstPath = view.studyTree.getPathForRow(parentStartIndex + i + 1);
                    view.studyTree.setSelectionPath(firstPath);
                    return view.studyTree.getLastSelectedPathComponent();
                }
            }
        }
        return null;
    }
    
    private Object selectCurrentStudySavedIndex() {
        Study currentStudy = getCurrentStudy();
        Object selectedElement;
        if(currentStudy.selectedIndex == -1) {
               selectedElement = this.selectFirstElement(currentStudy);
               Object parent = treeModel.getParent((StudyElement) selectedElement, currentStudy);
               currentStudy.selectedIndex = treeModel.getIndexOfChild(parent, selectedElement);
        }
        else {
               selectedElement = treeModel.getChild(currentStudy, currentStudy.selectedIndex);
               int selectedRow = treeModel.getRowOfElement(selectedElement, (Study)treeModel.getRoot());
               TreePath selectedImagePath = view.studyTree.getPathForRow(selectedRow);
               view.studyTree.setSelectionPath(selectedImagePath);
        }
        return selectedElement;
    }
    
    /**
     * Saves all the studies loaded within the frame
     */
    private void saveStudy() {
        Study rootStudy = (Study)treeModel.getRoot();
        System.out.println("Save Selected Index: " + rootStudy.selectedIndex);
        rootStudy.studyLoader.save(rootStudy);
    }
    
    private void copyCurrentStudy() {
        Study currentStudy = getCurrentStudy();
        if(currentStudy.studyLoader != null) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int fcReturn = fileChooser.showDialog(view, null);
            if(fcReturn == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if(currentStudy.studyLoader.copyStudy(currentStudy, selectedFile.getAbsolutePath())) {
                    //Open new Window.
                    MainFrameController newController = new MainFrameController();
                    StudyLoader newLoader = new LocalStudyLoader(selectedFile.getAbsolutePath());
                    Study newStudy = newLoader.execute();
                    newController.treeModel.setRootStudy(newStudy);
                }
            }
        }
    }
    
    /**
     * Loads a study to display in the frame
     */
    private void loadStudy() {
        //Save the current Study
        if(getCurrentStudy().studyLoader != null) {
            System.out.println("Saving study");
            saveStudy();
        }
        
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int fcReturn = fileChooser.showDialog(view, null);
        
        if(fcReturn == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String studyPath = selectedFile.getAbsolutePath();
            StudyLoader newLoader = new LocalStudyLoader(studyPath);
            Study loadStudy = newLoader.execute();
            this.treeModel.setRootStudy(loadStudy);
            selectCurrentStudySavedIndex();
            view.displayModeButton.setEnabled(true);
        }
    }
    
    /**
     * Selects the previous item in the current study.
     */
    private void selectPreviousElement() {
        int[] selectedRow = view.studyTree.getSelectionRows();
        if(selectedRow.length == 0)
            this.selectFirstElement(getCurrentStudy());
        else if(selectedRow[0] > 1) {
            TreePath selectionPath = view.studyTree.getPathForRow(--selectedRow[0]);
            view.studyTree.setSelectionPath(selectionPath);
        }
    }
    
    /**
     * Selects the next item in the current study.
     */
    private void selectNextElement() {
        int[] selectedRow = view.studyTree.getSelectionRows();
        int rowCount = view.studyTree.getRowCount();
        if(selectedRow.length == 0)
            this.selectFirstElement(getCurrentStudy());
        else if(selectedRow[0] < rowCount) {
            TreePath selectionPath = view.studyTree.getPathForRow(++selectedRow[0]);
            view.studyTree.setSelectionPath(selectionPath);
        }
    }
    
    private Study getCurrentStudy() {
        StudyElement currentSelectedElement = (StudyElement)view.studyTree.getLastSelectedPathComponent();
        if(currentSelectedElement != null) {
            if(currentSelectedElement instanceof Study)
                return (Study)currentSelectedElement;
            else 
                return (Study)treeModel.getParent(currentSelectedElement, (Study)treeModel.getRoot());
        }
        return (Study)treeModel.getRoot();
    }
}
