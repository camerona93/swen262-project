/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.gui;

import java.awt.GridLayout;
import medicalimaging.imageTypes.MedicalImage;
import medicalimaging.studyLoaders.StudyLoader;
import medicalimaging.studyLoaders.LocalStudyLoader;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;
import medicalimaging.model.DisplayModeStudyUndoableOperation;
import medicalimaging.model.ReferenceLine;
import medicalimaging.model.Study;
import medicalimaging.model.StudyElement;
import medicalimaging.model.StudyTreeModel;
import medicalimaging.model.StudyUndoableOperation;
import medicalimaging.studyLoaders.IntensityStudyLoader;

/**
 *
 * @author ericlee
 */
public class MainFrameController implements MainFrameViewProtocol, MedicalImageViewProtocol{
    private StudyTreeModel treeModel;
    private JFileChooser fileChooser = new JFileChooser();
    private ArrayList<StudyUndoableOperation> operationStack = new ArrayList<StudyUndoableOperation>();
    private MainFrame view;
    
    public MainFrameController() {
        view = new MainFrame();
        view.delegate = this;
        
        treeModel = new StudyTreeModel(new Study("Study"));
        view.setTreeModel(treeModel);
        view.displayModeSelect.setEnabled(false);
        view.setVisible(true);
        
        //Add window close listener
        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(getCurrentStudy().studyLoader != null)
                    saveStudy();
            }
        });
        view.imagePanel.addSelectionListener(this);
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
            
            view.studyTree.scrollPathToVisible(view.studyTree.getPathForRow(treeModel.getRowOfElement(selectedImage, currentStudy)));
            
            //Save selected index
            int selectedIndex = treeModel.getIndexOfChild(currentStudy, selectedImage);
            currentStudy.setSelectedIndex(selectedIndex);
            
            //Create list of images and reference lines to display
            ArrayList<MedicalImage> loadImages = new ArrayList<MedicalImage>();
            ArrayList<ArrayList<ReferenceLine>> lines = new ArrayList<ArrayList<ReferenceLine>>();
            
            if(currentStudy.getDisplayMode() == Study.DISPLAY_MODE_1x1) {
                loadImages.add(selectedImage);
            }
            else if(currentStudy.getDisplayMode() == Study.DISPLAY_MODE_2x2) {
                loadImages = getImagesFor2x2DisplayMode(currentStudy);
            }
            else if(currentStudy.getDisplayMode() == Study.DISPLAY_MODE_RECON) {
                lines = getReferenceLinesForStudy(currentStudy);
                loadImages = getImagesForReconDisplayMode(currentStudy);
            }
            else if(currentStudy.getDisplayMode() == Study.DISPLAY_MODE_INTEN) {
               Study windowStudy = currentStudy.windowStudy;
               loadImages.add((MedicalImage) windowStudy.getElement(currentStudy.getSelectedIndex()));
            }
            
            //Update GUI
            view.updateGUIForState(currentStudy.getDisplayMode());
            view.imagePanel.loadImages(loadImages, lines);
        }
    }
    
    @Override
    public void mouseScrollOnImage(int magnitude, int indexLocation) {
        Study currentStudy = getCurrentStudy();
        if(currentStudy.getDisplayMode() == Study.DISPLAY_MODE_RECON) {
            if(indexLocation == 0) {
                mouseScrollTree(magnitude);
            }
            else {
                Study reconStudy = currentStudy.reconStudies.get(indexLocation - 1);
                if(magnitude > 0 && reconStudy.getSelectedIndex() < reconStudy.getImageCount() - 1) {
                    reconStudy.setSelectedIndex(reconStudy.getSelectedIndex() + 1);
                    view.refreshImages();
                }
                else if(reconStudy.getSelectedIndex() > 0) {
                    reconStudy.setSelectedIndex(reconStudy.getSelectedIndex() - 1);
                    view.refreshImages();
                }
            }
        }
        else {
            mouseScrollTree(magnitude);
        }
    }
    
    private void mouseScrollTree(int magnitude) {
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
        int parentStartIndex = treeModel.getRowOfElement(parent, (Study)treeModel.getRoot());
        if(parentStartIndex >= 0) {
            for(int i = 0; i < parent.getElementCount(); i++) {
                StudyElement currentElement = parent.getElement(i);
                if(treeModel.isLeaf(currentElement)) {
                    TreePath firstPath = view.studyTree.getPathForRow(parentStartIndex + i + 1);
                    view.studyTree.setSelectionPath(firstPath);
                    return view.studyTree.getLastSelectedPathComponent();
                }
            }
        }
        return null;
    }
    
    @Override
    public void undoButtonPressed() {
        Study currentStudy = getCurrentStudy();
        currentStudy.undoTask();
    }
    
    private Object selectCurrentStudySavedIndex() {
        Study currentStudy = getCurrentStudy();
        Object selectedElement;
        if(currentStudy.getSelectedIndex() == -1) {
               selectedElement = this.selectFirstElement(currentStudy);
               Object parent = treeModel.getParent((StudyElement) selectedElement, currentStudy);
               currentStudy.setSelectedIndex(treeModel.getIndexOfChild(parent, selectedElement));
        }
        else {
               selectedElement = treeModel.getChild(currentStudy, currentStudy.getSelectedIndex());
               int selectedRow = treeModel.getRowOfElement(selectedElement, (Study)treeModel.getRoot());
               TreePath selectedImagePath = view.studyTree.getPathForRow(selectedRow);
               view.studyTree.setSelectionPath(selectedImagePath);
        }
        return selectedElement;
    }
    
    public void displayModeChanged(int displayMode) {
        view.imagePanel.clearImageSelection();
        Study currentStudy = getCurrentStudy();
        if(displayMode == Study.DISPLAY_MODE_INTEN && currentStudy.windowStudy == null) {
            int[] values = this.getWindowValues(0, 0);
            if(values[0] > -1)
                currentStudy.windowStudy = new IntensityStudyLoader(currentStudy, "Window", values[0], values[1]).execute();
            else
                displayMode = currentStudy.getDisplayMode();
        }
        
        if(displayMode != currentStudy.getDisplayMode()) {
            //Update the model
            DisplayModeStudyUndoableOperation operation = new DisplayModeStudyUndoableOperation(currentStudy, displayMode) {
                @Override
                public void onExecute() {
                    view.updateGUIForState(currentMode);
                    view.refreshImages();
                }
                @Override
                public void onUndo() {
                    view.updateGUIForState(previousMode);
                    view.refreshImages();
                }
            };
            operation.execute();
            currentStudy.addUndoTask(operation);

        
            view.refreshImages();
        }
    }
    
     @Override
    public void rectSelected(int image, Rectangle2D rect) {
        //TODO: Implement what happens on selection.
    }
    
    /**
     * Saves all the studies loaded within the frame
     */
    private void saveStudy() {
        Study rootStudy = (Study)treeModel.getRoot();
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
            saveStudy();
        }
        
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int fcReturn = fileChooser.showDialog(view, null);
        
        if(fcReturn == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String studyPath = selectedFile.getAbsolutePath();
            StudyLoader newLoader = new LocalStudyLoader(studyPath);
            Study loadStudy = newLoader.execute();
            if(loadStudy.getDisplayMode() == Study.DISPLAY_MODE_INTEN) {
                int[] values = getWindowValues(fcReturn, fcReturn);
                if(values[0] > -1)
                    loadStudy.windowStudy = new IntensityStudyLoader(loadStudy, "Window", values[0], values[1]).execute();
                else
                    loadStudy.setDisplayMode(Study.DISPLAY_MODE_1x1);
            }
            
            this.treeModel.setRootStudy(loadStudy);
            selectCurrentStudySavedIndex();
            view.displayModeSelect.setEnabled(true);
            view.updateGUIForState(loadStudy.getDisplayMode());
        }
        
        //Update view
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
    
    private int[] getWindowValues(int defaultLow, int defaultHight) {
        int[] values = new int[]{-1, -1};
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField lowTextField = new JTextField(Integer.toString(defaultLow), 3);
        JTextField highTextField = new JTextField(Integer.toString(defaultHight), 3);
        
        panel.add(new JLabel("Low: "));
        panel.add(lowTextField);
        panel.add(new JLabel("High: "));
        panel.add(highTextField);
        
        String message = "Please enter values for the window mode: ";
        int result = JOptionPane.showConfirmDialog(null, panel, message, JOptionPane.OK_CANCEL_OPTION);
        if(result != JOptionPane.CANCEL_OPTION) {
            int low = Integer.parseInt(lowTextField.getText());
            int high = Integer.parseInt(highTextField.getText());
            if(low >= high) {
                JOptionPane.showMessageDialog(null, "Invalid input. Must be a value between 0 - 255", "", JOptionPane.ERROR_MESSAGE);
                getWindowValues(low, high);
            }
            else {
                values[0] = low;
                values[1] = high;
            }
        }
        
        return values;
    }
    
    private ArrayList<MedicalImage> getImagesFor2x2DisplayMode(Study currentStudy) {
        int childCount = currentStudy.getImageCount();
        int selectedIndex = currentStudy.getSelectedIndex();
        ArrayList<MedicalImage> loadImages = new ArrayList<MedicalImage>();
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
        return loadImages;
    }
    
    private ArrayList<MedicalImage> getImagesForReconDisplayMode(Study currentStudy) {
        ArrayList<MedicalImage> loadImages = new ArrayList<MedicalImage>();
        ArrayList<Study> reconStudies = currentStudy.reconStudies;
        
        loadImages.add((MedicalImage)currentStudy.getElement(currentStudy.getSelectedIndex()));
        for(int i = 0; i < reconStudies.size(); i++) {
            Study reconStudy = reconStudies.get(i);
            loadImages.add((MedicalImage)reconStudy.getElement(reconStudy.getSelectedIndex()));
        }
        return loadImages;
    }
    
    private ArrayList<ArrayList<ReferenceLine>> getReferenceLinesForStudy(Study currentStudy) {
        ArrayList<ArrayList<ReferenceLine>> lines = new ArrayList<ArrayList<ReferenceLine>>();
        for(int i = 0; i <= currentStudy.reconStudies.size(); i++) {
            ArrayList<ReferenceLine> currentLines = new ArrayList<ReferenceLine>();
            if(i > 0) {
                Study reconStudy = currentStudy.reconStudies.get(i - 1);
                currentLines.add(reconStudy.getReferenceLineForStudy(currentStudy));
                for(int k = 0; k < currentStudy.reconStudies.size(); k++) {
                    Study currentRecon = currentStudy.reconStudies.get(k);
                    currentLines.add(reconStudy.getReferenceLineForStudy(currentRecon));
                }
            }
            else {
                currentLines.add(currentStudy.getReferenceLineForStudy(currentStudy));
                for(int k = 0; k < currentStudy.reconStudies.size(); k++) {
                    currentLines.add(currentStudy.getReferenceLineForStudy(currentStudy.reconStudies.get(k)));
                }
            }
            lines.add(currentLines);
        }
        return lines;
    }
}
