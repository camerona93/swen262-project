/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author ericlee
 */
public class MainFrame extends javax.swing.JFrame implements TreeSelectionListener{

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        //Load model
        this.treeModel = new StudyTreeModel(new Study("Study"));
        initComponents();
        
        //Configure study tree
        this.studyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.studyTree.addTreeSelectionListener(this);
        this.displayModeButton.setEnabled(false);
        
        //Configure image panel
        this.imagePanel.setLayout(new GridLayout(0,1));
        JLabel placeHolder = new JLabel("");
        placeHolder.setPreferredSize(new Dimension(imagePanel.getWidth(), imagePanel.getWidth()));
        this.imagePanel.add(placeHolder);
        
        //Add window close listener
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(getCurrentStudy().studyLoader != null)
                    saveStudy();
            }
        });
    }
    
    /**
     * Load images into the JPanel
     * @param loadImages ArrayList<MedicalImage> to load
     */
    private void loadImages(ArrayList<MedicalImage> loadImages) {
        this.clearImagePanel();
        int gridSize = (int)Math.ceil(Math.sqrt(loadImages.size()));
        this.imagePanel.setLayout(new GridLayout(gridSize, gridSize));
        
        int gridDivider = 1;
        if(getCurrentStudy().displayMode == Study.DISPLAY_MODE_2x2)
            gridDivider = 2;
        for(MedicalImage loadImage : loadImages) {
            int imageWidth = this.imagePanel.getWidth() / gridDivider;
            int imageHeight = this.imagePanel.getHeight() / gridDivider;
            ImageIcon tempIcon = loadImage.loadImage();
            Image image = tempIcon.getImage();
            Image scaledImage = image.getScaledInstance(imageWidth, imageHeight, 0);
            Icon imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon);
            this.imagePanel.add(imageLabel);
        }
        
        this.imagePanel.revalidate();
    }
    
    /**
     * Removes all images from the image JPanel
     */
    private void clearImagePanel() {
        Component[] components = this.imagePanel.getComponents();
        for(Component curComponent : components) {
            this.imagePanel.remove(curComponent);
        }
    }
    
    /**
     * Executes when the value of the JTree is changed
     * TODO: This method should be cleaned up and sized down.
     * @param e 
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        Study currentStudy = getCurrentStudy();
        if(this.studyTree.getLastSelectedPathComponent() instanceof Study) {
            this.selectFirstElement(currentStudy);
        }
        else {
            MedicalImage selectedImage = (MedicalImage) this.studyTree.getLastSelectedPathComponent();
            if(selectedImage == null){
                selectedImage = (MedicalImage) this.selectFirstElement(currentStudy);
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
            this.loadImages(loadImages);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        studyTreePanel = new javax.swing.JScrollPane();
        studyTree = new javax.swing.JTree(this.treeModel);
        imagePanel = new javax.swing.JPanel();
        displayModeButton = new javax.swing.JButton();
        toolbar = new javax.swing.JToolBar();
        openButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        studyTreePanel.setViewportView(studyTree);

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        displayModeButton.setText("4x4");
        displayModeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayModeButtonActionPerformed(evt);
            }
        });

        toolbar.setRollover(true);

        openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medicalimaging/open.gif"))); // NOI18N
        openButton.setFocusable(false);
        openButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });
        toolbar.add(openButton);

        copyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medicalimaging/copy.gif"))); // NOI18N
        copyButton.setFocusable(false);
        copyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        copyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });
        toolbar.add(copyButton);

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medicalimaging/previous.gif"))); // NOI18N
        previousButton.setFocusable(false);
        previousButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        previousButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        toolbar.add(previousButton);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medicalimaging/next.gif"))); // NOI18N
        nextButton.setFocusable(false);
        nextButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nextButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        toolbar.add(nextButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studyTreePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(displayModeButton)
                        .addGap(24, 24, 24))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(studyTreePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(displayModeButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void displayModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayModeButtonActionPerformed
        Study currentStudy = getCurrentStudy();
        if(currentStudy.displayMode == Study.DISPLAY_MODE_1x1) {
            this.displayModeButton.setText("1x1");
            currentStudy.displayMode = Study.DISPLAY_MODE_2x2;
        }
        else {
            this.displayModeButton.setText("2x2");
            currentStudy.displayMode = Study.DISPLAY_MODE_1x1;
        }
        this.valueChanged(null);
    }//GEN-LAST:event_displayModeButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        this.selectPreviousElement();
    }//GEN-LAST:event_previousButtonActionPerformed

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        loadStudy();
    }//GEN-LAST:event_openButtonActionPerformed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        this.copyCurrentStudy();
    }//GEN-LAST:event_copyButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        this.selectNextElement();
    }//GEN-LAST:event_nextButtonActionPerformed
    
    
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
                    TreePath firstPath = this.studyTree.getPathForRow(parentStartIndex + i + 1);
                    this.studyTree.setSelectionPath(firstPath);
                    return this.studyTree.getLastSelectedPathComponent();
                }
            }
        }
        return null;
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
            int fcReturn = fileChooser.showDialog(this, null);
            if(fcReturn == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if(currentStudy.studyLoader.copyStudy(currentStudy, selectedFile.getAbsolutePath())) {
                    //Open new Window.
                    MainFrame newWindow = new MainFrame();
                    StudyLoader newLoader = new LocalStudyLoader(selectedFile.getAbsolutePath());
                    Study newStudy = newLoader.execute();
                    newWindow.treeModel.setRootStudy(newStudy);
                    newWindow.setVisible(true);
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
        int fcReturn = fileChooser.showDialog(this, null);
        
        if(fcReturn == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String studyPath = selectedFile.getAbsolutePath();
            StudyLoader newLoader = new LocalStudyLoader(studyPath);
            Study loadStudy = newLoader.execute();
            loadStudy.studyLoader = newLoader;
            this.treeModel.setRootStudy(loadStudy);
         
            if(loadStudy.selectedIndex == -1) {
               Object selectedElement = this.selectFirstElement(loadStudy);
               Object parent = treeModel.getParent((StudyElement) selectedElement, loadStudy);
               loadStudy.selectedIndex = treeModel.getIndexOfChild(parent, selectedElement); 
            }
            else {
                Object selectedElement = treeModel.getChild(loadStudy, loadStudy.selectedIndex);
                int selectedRow = treeModel.getRowOfElement(selectedElement, (Study)treeModel.getRoot());
                TreePath selectedImagePath = studyTree.getPathForRow(selectedRow);
                studyTree.setSelectionPath(selectedImagePath);
            }
            
            this.displayModeButton.setEnabled(true);
        }
    }
    
    /**
     * Selects the previous item in the current study.
     */
    private void selectPreviousElement() {
        int[] selectedRow = studyTree.getSelectionRows();
        if(selectedRow.length == 0)
            this.selectFirstElement(getCurrentStudy());
        else if(selectedRow[0] > 1) {
            TreePath selectionPath = studyTree.getPathForRow(--selectedRow[0]);
            studyTree.setSelectionPath(selectionPath);
        }
    }
    
    /**
     * Selects the next item in the current study.
     */
    private void selectNextElement() {
        int[] selectedRow = studyTree.getSelectionRows();
        int rowCount = studyTree.getRowCount();
        if(selectedRow.length == 0)
            this.selectFirstElement(getCurrentStudy());
        else if(selectedRow[0] < rowCount) {
            TreePath selectionPath = studyTree.getPathForRow(++selectedRow[0]);
            studyTree.setSelectionPath(selectionPath);
        }
    }
    
    private Study getCurrentStudy() {
        StudyElement currentSelectedElement = (StudyElement)studyTree.getLastSelectedPathComponent();
        if(currentSelectedElement != null) {
            if(currentSelectedElement instanceof Study)
                return (Study)currentSelectedElement;
            else 
                return (Study)treeModel.getParent(currentSelectedElement, (Study)treeModel.getRoot());
        }
        return (Study)treeModel.getRoot();
    }
    
    private StudyTreeModel treeModel;
    private JFileChooser fileChooser = new JFileChooser();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyButton;
    private javax.swing.JButton displayModeButton;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton openButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JTree studyTree;
    private javax.swing.JScrollPane studyTreePanel;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

}
