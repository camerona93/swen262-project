/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
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
                returnStudy.addElement(new MedicalImage(currentFile.getAbsolutePath()));
            }
            else if(currentFile.isDirectory()){
                //TODO: I Think we may need to have the studyloader belong to the study
                StudyLoader newStudyLoader = new LocalStudyLoader(loadPath + "/" + currentFile.getName());
                Study subStudy = newStudyLoader.execute();
                subStudy.studyLoader = newStudyLoader;
                //Create new study loader. Execute and add substudy as an element
                returnStudy.addElement(subStudy);
            }
        }
        return returnStudy;
    }
    
    public void save(Study saveStudy) {
        try {
            System.out.println("fire");
            File serializeFile = new File(this.loadPath + "/study.ser");
            serializeFile.createNewFile();
            
            FileOutputStream fileOut = new FileOutputStream(this.loadPath + "/study.ser");
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            output.writeObject(saveStudy);
            output.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public boolean copyStudy(Study copyStudy, String copyPath){
        this.save(copyStudy);
        
        File rootDir = new File(this.loadPath);
        
        File[] subFiles = rootDir.listFiles();
        for(File currentFile : subFiles) {
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                if(!currentFile.isDirectory()) {
                    inputChannel = new FileInputStream(currentFile.getAbsoluteFile()).getChannel();
                    outputChannel = new FileOutputStream(copyPath + "/" + this.getFileName(currentFile.getAbsolutePath())).getChannel();
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                    
                    inputChannel.close();
                    outputChannel.close();
                }
            }
            catch(IOException e) {
                return false;
            }
        }
        return true;
    }
    
    private String getFileName(String file) {
        int fileNameStartIndex = file.lastIndexOf("/") + 1;
        return file.substring(fileNameStartIndex);
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
            System.out.println(e);
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
