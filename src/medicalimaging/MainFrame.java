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
import javax.swing.JButton;
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
        this.currentStudy = new Study("Study");
        this.treeModel = new StudyTreeModel(this.currentStudy);
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
                if(currentStudyLoader != null)
                    currentStudyLoader.save(currentStudy);
            }
        });
    }
    
    private void loadImages(ArrayList<MedicalImage> loadImages) {
        this.clearImagePanel();
        int gridSize = (int)Math.ceil(Math.sqrt(loadImages.size()));
        this.imagePanel.setLayout(new GridLayout(gridSize, gridSize));
        
        for(MedicalImage loadImage : loadImages) {
            int imageWidth = this.imagePanel.getWidth() / this.currentStudy.displayMode;
            int imageHeight = this.imagePanel.getHeight() / this.currentStudy.displayMode;
            ImageIcon tempIcon = new ImageIcon(loadImage.imagePath);
            Image image = tempIcon.getImage();
            Image scaledImage = image.getScaledInstance(imageWidth, imageHeight, 0);
            Icon imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon);
            this.imagePanel.add(imageLabel);
        }
        
        this.imagePanel.revalidate();
    }
    
    private void clearImagePanel() {
        Component[] components = this.imagePanel.getComponents();
        for(Component curComponent : components) {
            this.imagePanel.remove(curComponent);
        }
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if(this.studyTree.getLastSelectedPathComponent() instanceof Study) {
            this.selectFirstElement();
        }
        else {
            MedicalImage selectedImage = (MedicalImage) this.studyTree.getLastSelectedPathComponent();
            if(selectedImage == null){
                selectedImage = (MedicalImage) this.selectFirstElement();
            }
            
            //Save selected index
            int selectedIndex = this.treeModel.getIndexOfChild(this.currentStudy, selectedImage);
            this.currentStudy.selectedIndex = selectedIndex;
            
            //Create list of images to display
            ArrayList<MedicalImage> loadImages = new ArrayList<MedicalImage>();
            if(this.currentStudy.displayMode == this.DISPLAY_MODE_1x1)
                loadImages.add(selectedImage);
            else if(this.currentStudy.displayMode == this.DISPLAY_MODE_2x2) {
                int childCount = this.treeModel.getChildCount(this.currentStudy);
                if(selectedIndex > -1) {
                    if(childCount <= 4) {
                        for(int i = 0; i < childCount; i++) {
                            loadImages.add((MedicalImage)this.treeModel.getChild(this.currentStudy, i));
                        }
                    }
                    else if(selectedIndex >= childCount - 4) {
                        for(int i = childCount - 4; i < childCount; i++) {
                            loadImages.add((MedicalImage)this.treeModel.getChild(this.currentStudy, i));
                        }
                    }
                    else {
                        for(int i = selectedIndex; i < selectedIndex + 4; i++) {
                            loadImages.add((MedicalImage)this.treeModel.getChild(this.currentStudy, i));
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
        loadDiskButton = new javax.swing.JButton();
        displayModeButton = new javax.swing.JButton();
        toolbar = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();

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

        loadDiskButton.setText("Load Study");
        loadDiskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDiskButtonActionPerformed(evt);
            }
        });

        displayModeButton.setText("4x4");
        displayModeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayModeButtonActionPerformed(evt);
            }
        });

        toolbar.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medicalimaging/copy.gif"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(jButton1);

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
                        .addComponent(loadDiskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(loadDiskButton)
                            .addComponent(displayModeButton))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadDiskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDiskButtonActionPerformed
        //Save the current Study
        if(this.currentStudyLoader != null)
            this.currentStudyLoader.save(currentStudy);
        
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int fcReturn = fileChooser.showDialog(this, null);
        
        if(fcReturn == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String studyPath = selectedFile.getAbsolutePath();
            this.currentStudyLoader = new LocalStudyLoader(studyPath);
            this.currentStudy = this.currentStudyLoader.execute();
            this.treeModel.setRootStudy(currentStudy);
            
            TreePath selectedImage = this.studyTree.getPathForRow(this.currentStudy.selectedIndex + 1);
            if(selectedImage != null)
                this.studyTree.setSelectionPath(selectedImage);
            else
                this.selectFirstElement();
            this.displayModeButton.setEnabled(true);
        }
    }//GEN-LAST:event_loadDiskButtonActionPerformed

    private void displayModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayModeButtonActionPerformed
        if(this.currentStudy.displayMode == this.DISPLAY_MODE_1x1) {
            this.displayModeButton.setText("1x1");
            this.currentStudy.displayMode = this.DISPLAY_MODE_2x2;
        }
        else {
            this.displayModeButton.setText("2x2");
            this.currentStudy.displayMode = this.DISPLAY_MODE_1x1;
        }
        this.valueChanged(null);
    }//GEN-LAST:event_displayModeButtonActionPerformed
    
    
    private Object selectFirstElement() {
        TreePath firstPath = this.studyTree.getPathForRow(1);
        if(firstPath != null) {
            this.studyTree.setSelectionPath(firstPath);
            return this.studyTree.getLastSelectedPathComponent();
        }
        return null;
    }
    
    private void copyCurrentStudy() {
        System.out.println("COPY!");
    }
    
    private Study currentStudy;
    private StudyLoader currentStudyLoader;
    private StudyTreeModel treeModel;
    private final int DISPLAY_MODE_1x1 = 1;
    private final int DISPLAY_MODE_2x2 = 2;
    private JFileChooser fileChooser = new JFileChooser();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton displayModeButton;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton loadDiskButton;
    private javax.swing.JTree studyTree;
    private javax.swing.JScrollPane studyTreePanel;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

}
