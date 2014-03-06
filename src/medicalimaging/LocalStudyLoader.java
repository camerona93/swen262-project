/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.io.File;
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
        
        //Loop through all the files in the directory and add them to the study
        for(File currentFile : subFiles) {
            if(!currentFile.isDirectory() && this.fileSupported(currentFile)) {
                returnStudy.addImage(new MedicalImage(currentFile.getAbsolutePath()));
            }
        }
        return returnStudy;
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
