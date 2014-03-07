/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
/**
 * Loads study
 * @author ericlee
 */
public class LocalStudyLoader implements StudyLoader{
    protected String loadPath;
    
    public LocalStudyLoader(String loadPath) {
        this.loadPath = loadPath;
    }
    
    public Study execute() {
        File rootDir = new File(loadPath);
        File[] subFiles = rootDir.listFiles();
        Study returnStudy = new Study(rootDir.getName());
        
        Study studySettings = this.loadSettings();
        if(studySettings != null) {
            returnStudy.displayMode = studySettings.displayMode;
            returnStudy.selectedIndex = studySettings.selectedIndex;
        }
        //Loop through all the files in the directory and add them to the study
        for(File currentFile : subFiles) {
            if(!currentFile.isDirectory() && this.fileSupported(currentFile)) {
                returnStudy.addImage(new MedicalImage(currentFile.getAbsolutePath()));
            }
        }
        return returnStudy;
    }
    
    public void save(Study saveStudy) {
        try {
            FileOutputStream fileOut = new FileOutputStream(this.loadPath + "/study.ser");
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            output.writeObject(saveStudy);
            output.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
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
            System.out.println("error");
        }
        return study;
    }
    
    private boolean fileSupported(File file) {
        String fileName = file.getName();
        int fileTypeIndex = fileName.lastIndexOf(".");
        String fileType = fileName.substring(fileTypeIndex);
        return Arrays.asList(this.getSupportedFileTypes()).contains(fileType);
    }
    
    private String[] getSupportedFileTypes() {
        return new String[]{
            ".jpeg",
            ".jpg"
        };
    }
}
