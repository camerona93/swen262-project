/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cameron
 */
public class SystemController {
    
    protected static final String STUDY_PATH = new File("").getAbsolutePath().concat("\\Studies\\");
    protected static ArrayList<Study> studies = new ArrayList<>();
    
    public static void main(String[] args) {
        loadStudies();
    }
    
    private static void loadStudies() {
        String[] directoryList = new File(STUDY_PATH).list();
        
        studies.clear();
        
        for (String f : directoryList) {
            studies.add(deserializeStudy(f));
        }
        
        System.out.println(studies);
    }
    
    private static Study deserializeStudy(String studyName) {
        Study s = null;
        
        try(
            InputStream file = new FileInputStream(STUDY_PATH.concat(studyName));
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            ){
                s = (Study)input.readObject();
            }
            catch(ClassNotFoundException ex){
            }
            catch(IOException ex){
            }
        
        return s;
    }
}