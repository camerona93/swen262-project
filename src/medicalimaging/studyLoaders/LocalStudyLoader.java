/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.studyLoaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import medicalimaging.imageTypes.AcrImage;
import medicalimaging.model.ImageReconUtils;
import medicalimaging.imageTypes.StandardImage;
import medicalimaging.model.Study;
import medicalimaging.model.StudyElement;
/**
 * Handles all interaction for saving, loading, and coping a study
 * @author ericlee
 */
public class LocalStudyLoader implements StudyLoader{
    protected String loadPath; 
    
    public LocalStudyLoader(String loadPath) {
        this.loadPath = loadPath;
    }
    
    /**
     * Loads a study at the load path from the disk
     * @return Study loaded study
     */
    public Study execute() {
        File rootDir = new File(loadPath);
        File[] subFiles = rootDir.listFiles();
        Study returnStudy = new Study(rootDir.getName());
        
        Study studySettings = this.loadSettings();
        if(studySettings != null) {
            returnStudy.setDisplayMode(studySettings.getDisplayMode());
            returnStudy.setSelectedIndex(studySettings.getSelectedIndex());
        }
        //Loop through all the files in the directory and add them to the study
        for(File currentFile : subFiles) {
            if(!currentFile.isDirectory() && this.fileSupported(currentFile)) {
                String fileType = this.getFileType(currentFile.getName());
                if(fileType.equals(".acr"))
                    returnStudy.addElement(new AcrImage(currentFile.getAbsolutePath()));
                else
                    returnStudy.addElement(new StandardImage(currentFile.getAbsolutePath()));
            }
            else if(currentFile.isDirectory()){
                //Detects a study within a study
                StudyLoader newStudyLoader = new LocalStudyLoader(loadPath + "/" + currentFile.getName());
                Study subStudy = newStudyLoader.execute();
                subStudy.studyLoader = newStudyLoader;
                returnStudy.addElement(subStudy);
            }
        }
        
        //Create Reconstructed Studies
        int[][][] render = ImageReconUtils.generate3D(returnStudy);
        returnStudy.reconStudies.add(new ProfileStudyLoader("Profile", returnStudy.getRender()).execute());
        returnStudy.reconStudies.add(new FrontStudyLoader("Front", returnStudy.getRender()).execute());
        returnStudy.studyLoader = this;
        
        return returnStudy;
    }
    
    /**
     * Saves a study to the disk
     * @param saveStudy Study to save
     */
    public void save(Study saveStudy) {
        try {
            File serializeFile = new File(this.loadPath + "/study.ser");
            serializeFile.createNewFile();
            
            FileOutputStream fileOut = new FileOutputStream(this.loadPath + "/study.ser");
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            output.writeObject(saveStudy);
            output.close();
            
            //Look for substudies
            for(int i = 0; i < saveStudy.getElementCount(); i++) {
                StudyElement currentElement = saveStudy.getElement(i);
                if(currentElement instanceof Study)
                    ((Study)currentElement).studyLoader.save((Study)currentElement);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    /**
     * Copies a given study to the given path
     * @param copyStudy
     * @param copyPath
     * @return 
     */
    public boolean copyStudy(Study copyStudy, String copyPath){
        this.save(copyStudy);
        return this.copyFiles(loadPath, copyPath);
    }
    
    /**
     * Loads the settings of a study
     * @return The study loaded with the settings
     */
    private Study loadSettings() {
        Study study = null;
        try {
            FileInputStream fileIn = new FileInputStream(this.loadPath + "/study.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            study = (Study) in.readObject();
            in.close();
            fileIn.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return study;
    }
    
    /**
     * Checks if a given file is supported
     * @param file File the file to check
     * @return True if supported false if not.
     */
    private boolean fileSupported(File file) {
        String fileName = file.getName();
        String fileType = this.getFileType(fileName);
        return Arrays.asList(this.getSupportedFileTypes()).contains(fileType);
    }
    
    private String getFileType(String fileName) {
        int fileTypeIndex = fileName.lastIndexOf(".");
        return fileName.substring(fileTypeIndex);
    }
    
    /**
     * Gets an array of strings of all supported file types
     * @return String[] of supported file types
     */
    private String[] getSupportedFileTypes() {
        return new String[]{
            ".jpeg",
            ".jpg",
            ".acr"
        };
    }
    
    /**
     * Copies a given directory to the given path
     * @param sourcePath The directory to copy
     * @param copyPath The path to copy to
     * @return True if the operation is a success.
     */
    private boolean copyFiles(String sourcePath, String copyPath) {
        File rootDir = new File(sourcePath);
        
        File[] subFiles = rootDir.listFiles();
        for(File currentFile : subFiles) {
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                if(!currentFile.isDirectory()) {
                    inputChannel = new FileInputStream(currentFile.getAbsoluteFile()).getChannel();
                    outputChannel = new FileOutputStream(copyPath + "/" + currentFile.getName()).getChannel();
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                    
                    inputChannel.close();
                    outputChannel.close();
                }
                
                else {
                    File newDir = new File(copyPath + "/" + currentFile.getName());
                    newDir.mkdirs();
                    return copyFiles(currentFile.getAbsolutePath(), copyPath + "/" + currentFile.getName());
                }
            }
            catch(IOException e) {
                return false;
            }
        }
        return true;
    }
}
